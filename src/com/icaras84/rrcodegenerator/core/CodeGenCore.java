package com.icaras84.rrcodegenerator.core;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.icaras84.rrcodegenerator.core.logic.OutputPanelLogic;
import com.icaras84.rrcodegenerator.core.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.MainWindow;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.OutputPanel;
import com.icaras84.rrcodegenerator.core.utils.RunOnce;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class CodeGenCore {

    private static boolean created = false;
    private static boolean swingUiCreated = false;
    private static boolean pendingProgramExit = false;

    private static Timer vsyncTimer;
    private static final int VSYNC_FPS = 30;
    private static final int PERIODIC_RENDER_TIME_MS = 1500;

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
            CodeGenCore.render();
            System.out.println("Rendered!");
            OutputPanelLogic.queuedRender = false;
        });
        Timer renderDelay = new Timer(PERIODIC_RENDER_TIME_MS, e -> renderTask.reset()); //tab switching has some intrinsic delay in rendering
        renderDelay.setRepeats(true);

        OutputPanel.TAB_SELECTION lastSelection, currentSelection = null;
        boolean lastResizeState, currResizeState = true;

        while (!pendingProgramExit){
            pendingProgramExit = MainWindow.isClosing();

            //canRender controls if OutputPanel has properly been created
            //Tab Selection is to prevent the GPU drawing over other tabs' content, consequently optimizing it
            if (swingUiCreated){
                lastSelection = currentSelection;
                currentSelection = OutputPanel.getTabSelection();

                lastResizeState = currResizeState;
                currResizeState = MainWindow.isResizing();

                if (currentSelection == OutputPanel.TAB_SELECTION.VIEWPORT && !MainWindow.isResizing()){
                    if (OutputPanelLogic.queuedRender){
                        renderTask.reset();
                    } else if (lastSelection != currentSelection && currentSelection == OutputPanel.TAB_SELECTION.VIEWPORT){
                        renderDelay.restart();
                    }
                } else if (currentSelection != OutputPanel.TAB_SELECTION.VIEWPORT && !MainWindow.isResizing()) {
                    renderDelay.stop();
                }

                if (lastResizeState != currResizeState && currResizeState == !MainWindow.isResizing()){
                    renderTask.reset();
                }
            }

            renderTask.run();
        }

        dispose();
        System.exit(0);
    }

    public static void render(){
        BufferStrategy bs = OutputPanel.getMainCanvas().getBufferStrategy();
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        CanvasRenderer.updateViewMatrix(g);
        CanvasRenderer.clear(g);
        g.setColor(Color.ORANGE);
        CanvasRenderer.drawPose(g, new Pose2d(0, 0, 0));
        g.dispose();
        bs.show();
    }

    public static void dispose(){
        MainWindow.dispose();
    }
}
