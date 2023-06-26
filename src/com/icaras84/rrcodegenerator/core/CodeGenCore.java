package com.icaras84.rrcodegenerator.core;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.icaras84.rrcodegenerator.core.renderer.CodeGenCanvasRenderer;
import com.icaras84.rrcodegenerator.core.renderer.ui.CodeGenCanvasPanel;
import com.icaras84.rrcodegenerator.core.renderer.ui.CodeGenMainWindow;

import javax.swing.*;
import java.awt.*;

public class CodeGenCore {

    private static boolean created = false;
    private static boolean pendingProgramExit = false;

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
                System.setProperty("apple.awt.application.name", CodeGenMainWindow.windowTitle);
                System.setProperty("apple.awt.application.icon", "/window_icon/RRCodeGenIcon128.png");
            }
            created = true;

            SwingUtilities.invokeLater(() -> {
                IntelliJTheme.setup(CodeGenCore.class.getResourceAsStream("/flatlaf_themes/arc_dark_orange/arc_theme_dark_orange.theme.json"));
                CodeGenMainWindow.create();
                CodeGenMainWindow.setVisibility(true);
                CodeGenCanvasPanel.createBufferStrategy(2);
                CodeGenCanvasRenderer.init();
            });

        }

        while (!pendingProgramExit){
            pendingProgramExit = CodeGenMainWindow.isClosing();
        }

        CodeGenMainWindow.dispose();
        System.exit(0);
    }


}
