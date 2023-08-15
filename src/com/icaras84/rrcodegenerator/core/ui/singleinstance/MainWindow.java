package com.icaras84.rrcodegenerator.core.ui.singleinstance;

import com.formdev.flatlaf.util.SystemInfo;
import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class MainWindow {

    public static int width, height;
    public static int lastWidth, lastHeight;
    public static String TITLE = "RR Trajectory Editor";
    private static boolean isClosing;
    private static boolean isResizing;

    //resizing list
    private static Vector<Runnable> resizeList;
    //dispose list
    private static Vector<Runnable> disposeList;

    //UI Components
    private static JFrame mainFrame;
    private static JPanel mainPanel;
    public static void init(int initWidth, int initHeight){
        initVariables(initWidth, initHeight);

        JFrame.setDefaultLookAndFeelDecorated(true);

        mainFrame = new JFrame(TITLE);
        mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.X_AXIS));
        GeneralUtils.changeTitleBar(mainFrame);

        /*
        if (SystemInfo.isMacOS) MainWindow.getMainFrame().getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true );
         */

        ImageIcon i16 = new ImageIcon(GeneralUtils.getImage("/window_icon/RRCodeGenIcon16.png"));
        ImageIcon i32 = new ImageIcon(GeneralUtils.getImage("/window_icon/RRCodeGenIcon32.png"));
        ImageIcon i64 = new ImageIcon(GeneralUtils.getImage("/window_icon/RRCodeGenIcon64.png"));
        ImageIcon i128 = new ImageIcon(GeneralUtils.getImage("/window_icon/RRCodeGenIcon128.png"));
        Vector<Image> windowIcons = new Vector<>();
        windowIcons.add(i16.getImage());
        windowIcons.add(i32.getImage());
        windowIcons.add(i64.getImage());
        windowIcons.add(i128.getImage());
        mainFrame.setIconImages(windowIcons);

        //deal with window size
        mainFrame.setMinimumSize(new Dimension(720, 405));
        mainFrame.setSize(initWidth, initHeight);
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
                super.componentResized(e);
            }
        });

        //deal with window closing
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isClosing = true;
                super.windowClosing(e);
            }
        });
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createSubUI();

        mainFrame.setVisible(true);
    }

    private static void createSubUI(){
        OutputPanel.init();
        TrajectoryEditorPanel.initMain();
        NavigationPanel.init();

        mainFrame.add(NavigationPanel.getPanel());
        mainFrame.add(TrajectoryEditorPanel.getReservedPanel());
        mainFrame.add(OutputPanel.getPanel());
    }

    private static void initVariables(int initWidth, int initHeight){
        width = initWidth;
        height = initHeight;
        isClosing = false;
        resizeList = new Vector<>();
        disposeList = new Vector<>();
    }

    public static void submitResizeOperation(Runnable r){
        resizeList.add(r);
    }
    public static void resize(){
        lastWidth = width;
        lastHeight = height;
        width = mainFrame.getWidth();
        height = mainFrame.getHeight();
        isResizing = !(lastWidth == width && lastHeight == height);

        for (Runnable r: resizeList) {
            r.run();
        }
    }

    public static synchronized boolean isClosing(){
        return isClosing;
    }

    public static synchronized boolean isResizing(){
        return isResizing;
    }

    public static void submitDisposeOperation(Runnable r){
        disposeList.add(r);
    }
    public static synchronized void dispose(){
        for (Runnable r: disposeList) {
            r.run();
        }

        mainFrame.dispose();
    }

    public static JFrame getMainFrame(){
        return mainFrame;
    }

    public static synchronized void repaint(){
        mainFrame.repaint();
    }
}
