package com.icaras84.rrcodegenerator.core;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.icaras84.rrcodegenerator.core.logic.OutputPanelLogic;
import com.icaras84.rrcodegenerator.core.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.MainWindow;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.OutputPanel;
import com.icaras84.rrcodegenerator.core.utils.RunOnce;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Vector;

public class CodeGenCore {

    private static boolean created = false;
    private static boolean swingUiCreated = false;
    private static boolean pendingProgramExit = false;

    private static Timer vsyncTimer;
    private static final int VSYNC_FPS = 60;
    private static final int FIXED_UPDATE_FPS = 30;
    private static final float FIXED_UPDATE_THRESH_MS = 1000f / FIXED_UPDATE_FPS;
    private static final float FIXED_UPDATE_THRESH_SEC = FIXED_UPDATE_THRESH_MS / 1000f;

    private static Vector<CoreUpdate> updates;

    public static void run(){
        updates = new Vector<>();
        updates.add(new CoreUpdate() {

            RobotProperties properties = new RobotProperties();
            Trajectory traj = properties.constructTrajectoryBuilder(new Pose2d(0, 0, Math.PI / 2), Math.PI / 2)
                    .splineTo(new Vector2d(30, 30), Math.PI * 0.6)
                    .splineToSplineHeading(new Pose2d(-30, 45, Math.PI), Math.PI)
                    .build();

            Stroke normalStroke = new BasicStroke(1);
            Stroke pathStroke = new BasicStroke(3);

            @Override
            public void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec) {

            }

            @Override
            public void render(Graphics2D g, float fixedDeltaTimeMs, float fixedDeltaTimeSec) {
                g.setColor(Color.BLACK);
                g.setStroke(pathStroke);
                CanvasRenderer.drawTrajectory(g, traj);
                g.setColor(Color.GREEN);
                CanvasRenderer.drawPose(g, traj.start());
                CanvasRenderer.drawPose(g, traj.end());
            }
        });

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
                    } else if (lastSelection != currentSelection){
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

        fixedUpdateDelay.stop();
        renderDelay.stop();

        dispose();
        System.exit(0);
    }

    public synchronized static void render(float fixedDeltaTimeMs, float fixedDeltaTimeSec){
        BufferStrategy bs = OutputPanel.getMainCanvas().getBufferStrategy();
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        CanvasRenderer.updateViewMatrix(g);
        CanvasRenderer.clear(g);
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
