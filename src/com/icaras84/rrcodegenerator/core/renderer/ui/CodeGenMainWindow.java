package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.icaras84.rrcodegenerator.core.renderer.CodeGenCanvasRenderer;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class CodeGenMainWindow {

    //WINDOW CONSTANTS
    public static final int initWindowWidth = 1080, initWindowHeight = 720;
    public static final Dimension windowSize = new Dimension(initWindowWidth, initWindowHeight);
    public static final String windowTitle = "RRTrajectory Generator";

    private static boolean pendingClose;

    private static JFrame window;
    private static JPanel mainPanel;
    private static JSplitPane mergedPanel;

    public static void create(){
        window = new JFrame(windowTitle);
        window.setLayout(new BorderLayout());

        window.setMinimumSize(new Dimension((int) (initWindowWidth / 1.5d), (int) (initWindowHeight / 1.5d)));
        window.setSize(initWindowWidth, initWindowHeight);

        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //we are handling closing
        window.setLocationRelativeTo(null);

        GeneralUtils.changeTitleBar(window);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pendingClose = true;
                super.windowClosing(e);
            }
        });

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowSize.width = window.getWidth();
                windowSize.height = window.getHeight();
                resizeUIComponents();
                super.componentResized(e);
            }
        });

        ImageIcon i16 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(CodeGenMainWindow.class.getResource("/window_icon/RRCodeGenIcon16.png")));
        ImageIcon i32 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(CodeGenMainWindow.class.getResource("/window_icon/RRCodeGenIcon32.png")));
        ImageIcon i64 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(CodeGenMainWindow.class.getResource("/window_icon/RRCodeGenIcon64.png")));
        ImageIcon i128 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(CodeGenMainWindow.class.getResource("/window_icon/RRCodeGenIcon128.png")));

        Vector<Image> windowIcons = new Vector<>();
        windowIcons.add(i16.getImage());
        windowIcons.add(i32.getImage());
        windowIcons.add(i64.getImage());
        windowIcons.add(i128.getImage());

        window.setIconImages(windowIcons);

        createUIComponents();

        window.add(mainPanel);
    }

    private static void createUIComponents(){
        mainPanel = new JPanel(new BorderLayout());

        //CodeGenToolBar.createUI();
        CodeGenCanvasPanel.createUI();
        CodeGenSideBar.createUI();

        mergedPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, CodeGenSideBar.getRootPanel(), CodeGenCanvasPanel.getRootPanel());
        mergedPanel.setResizeWeight(3/8d);

        //mainPanel.add(CodeGenToolBar.getRootPanel(), BorderLayout.NORTH);
        mainPanel.add(mergedPanel, BorderLayout.CENTER);
    }

    protected static void resizeUIComponents(){
        //CodeGenToolBar.resize(windowSize.width, 30);
        CodeGenCanvasPanel.resize(windowSize.width - mergedPanel.getDividerLocation(), windowSize.height - CodeGenToolBar.TOOLBAR_HEIGHT);
        CodeGenSideBar.resize(mergedPanel.getDividerLocation(), windowSize.height - CodeGenToolBar.TOOLBAR_HEIGHT);
        CodeGenCanvasRenderer.updateViewMatrix();
    }

    public static String getTitle(){
        return window.getTitle();
    }

    public synchronized static boolean isClosing(){
        return pendingClose;
    }

    public static void setVisibility(boolean visible){
        CodeGenCanvasPanel.setVisibility(visible);
        window.setVisible(visible);
    }

    public static JFrame getWindow(){
        return window;
    }

    public static void dispose(){
        window.dispose();
    }
}
