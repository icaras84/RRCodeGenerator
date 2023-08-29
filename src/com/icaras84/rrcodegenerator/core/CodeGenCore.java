package com.icaras84.rrcodegenerator.core;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.OutputPanelLogic;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.window.MainWindow;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.OutputPanel;
import com.icaras84.rrcodegenerator.core.utils.RunOnce;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Vector;

public class CodeGenCore {

    private static boolean created = false;
    private static volatile boolean swingUiCreated = false;
    private static boolean pendingProgramExit = false;

    private static Timer vsyncTimer;
    private static final int VSYNC_FPS = 60;
    private static final int FIXED_UPDATE_FPS = 30;
    private static final float FIXED_UPDATE_THRESH_MS = 1000f / FIXED_UPDATE_FPS;
    private static final float FIXED_UPDATE_THRESH_SEC = FIXED_UPDATE_THRESH_MS / 1000f;

    private static Vector<CoreUpdate> updates;

    public static void initUpdateList(){
        updates = new Vector<>();
    }

    public static void submitUpdatable(CoreUpdate update){
        updates.add(update);
    }
    public static void run(){

        if (!created){

            if (SystemInfo.isMacOS) {
                try { //reflection hackery to avoid importing com.apple.eawt.Application since that is platform specific
                    Object app = Class.forName("com.apple.eawt.Application")
                            .getMethod("getApplication")
                            .invoke(null);
                    app.getClass()
                            .getMethod("setDockIconImage", java.awt.Image.class)
                            .invoke(
                                    app,
                                    Toolkit.getDefaultToolkit()
                                            .getImage(
                                                    CodeGenCore.class.getResource("/window_icon/RRCodeGenIcon128.png")
                                            )
                            );
                } catch (Exception ignored){}

                System.setProperty( "apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.application.name", MainWindow.TITLE);
                System.setProperty("apple.awt.application.icon", "/window_icon/RRCodeGenIcon128.png");
            }
            created = true;

            if (SystemInfo.isLinux){
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
            }

            SwingUtilities.invokeLater(() -> {
                IntelliJTheme.setup(CodeGenCore.class.getResourceAsStream("/flatlaf_themes/arc_dark_orange/arc_theme_dark_orange.theme.json"));
                MainWindow.init(1280, 720);
                CanvasRenderer.init();
                swingUiCreated = true;
                vsyncTimer = new Timer(1000 / VSYNC_FPS, e -> MainWindow.repaint());
                vsyncTimer.setRepeats(true);
                vsyncTimer.start();
            });

        }


        RunOnce renderTask = new RunOnce(true, () -> {
            CodeGenCore.render(FIXED_UPDATE_THRESH_MS, FIXED_UPDATE_THRESH_SEC);
            //System.out.println("Rendered!");
            OutputPanelLogic.queuedRender = false;
        });
        Runnable fixedUpdateTask = () -> fixedUpdate(FIXED_UPDATE_THRESH_MS, FIXED_UPDATE_THRESH_SEC);

        Timer renderDelay = new Timer((int) FIXED_UPDATE_THRESH_MS, e -> renderTask.reset()); //tab switching has some intrinsic delay in rendering
        renderDelay.setRepeats(true);

        Timer fixedUpdateDelay = new Timer((int) FIXED_UPDATE_THRESH_MS, e -> fixedUpdateTask.run());
        fixedUpdateDelay.setRepeats(true);
        fixedUpdateDelay.start();


        OutputPanel.TAB_SELECTION lastSelection, currentSelection = null;
        boolean lastResizeState, currResizeState = true;

        while (!swingUiCreated){}

        for (CoreUpdate update : updates) {
            update.lateInit();
        }

        while (!pendingProgramExit){
            pendingProgramExit = MainWindow.isClosing();

            //canRender controls if OutputPanel has properly been created
            //Tab Selection is to prevent the GPU drawing over other tabs' content, consequently optimizing it


                lastSelection = currentSelection;
                currentSelection = OutputPanel.getTabSelection();

                lastResizeState = currResizeState;
                currResizeState = MainWindow.isResizing();

                if (currentSelection == OutputPanel.TAB_SELECTION.VIEWPORT && !MainWindow.isResizing()){
                    if (OutputPanelLogic.queuedRender){
                        renderTask.reset();
                    } else if (lastSelection != currentSelection){
                        renderDelay.restart();
                    }
                } else if (currentSelection != OutputPanel.TAB_SELECTION.VIEWPORT && !MainWindow.isResizing()) {
                    renderDelay.stop();
                }

                if (lastResizeState != currResizeState && currResizeState == !MainWindow.isResizing()){
                    renderTask.reset();
                }


            renderTask.run();
        }

        fixedUpdateDelay.stop();
        renderDelay.stop();

        dispose();
        System.exit(0);
    }

    public synchronized static void render(float fixedDeltaTimeMs, float fixedDeltaTimeSec){
        BufferStrategy bs = OutputPanel.getMainCanvas().getBufferStrategy();
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        CanvasRenderer.updateViewMatrix(g);
        CanvasRenderer.clear();
        for (CoreUpdate update : updates) {
            update.render(g, fixedDeltaTimeMs, fixedDeltaTimeSec);
        }

        g.dispose();
        bs.show();
    }

    public synchronized static void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec){
        for (CoreUpdate update : updates) {
            update.fixedUpdate(fixedDeltaTimeMs, fixedDeltaTimeSec);
        }
    }

    public static void dispose(){
        MainWindow.dispose();
    }
}
