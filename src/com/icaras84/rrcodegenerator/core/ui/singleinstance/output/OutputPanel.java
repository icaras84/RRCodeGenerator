package com.icaras84.rrcodegenerator.core.ui.singleinstance.output;

import com.icaras84.rrcodegenerator.core.ui.multiinstance.TrajectoryEditorPanel;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.ui.CodeOutputPanel;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.ui.settingsui.SettingsPanel;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.window.MainWindow;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OutputPanel{

    public enum TAB_SELECTION{
        VIEWPORT("Viewport", 0),
        CODE_OUT("Code", 1),
        SETTINGS("Settings", 2);

        private int idx;
        private String tabName;

        TAB_SELECTION(String tabName, int idx){
            this.tabName = tabName;
            this.idx = idx;
        }
    }

    public static int panelWidth, panelHeight;
    public static int visiblePanelWidth, visiblePanelHeight;
    public static int canvasWidth, canvasHeight;

    private static JTabbedPane mainPanel;

    private static JPanel canvasPanel;
    private static Canvas mainCanvas;

    public static int mouseX, mouseY;

    private OutputPanel(){}

    public static void init(){
        mainPanel = new JTabbedPane();
        mainPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resize();
                MainWindow.resize(); //centralize resizing
            }
        });

        initCanvasPanel();
        mainPanel.addTab(TAB_SELECTION.VIEWPORT.tabName, canvasPanel);

        CodeOutputPanel.init();
        mainPanel.addTab(TAB_SELECTION.CODE_OUT.tabName, CodeOutputPanel.getMainPanel());

        SettingsPanel.init();
        mainPanel.addTab(TAB_SELECTION.SETTINGS.tabName, SettingsPanel.getMainPanel());

        resize();
        MainWindow.submitResizeOperation(OutputPanel::resize);
    }

    public static double wheelVel = 0;

    private static void initCanvasPanel(){
        canvasPanel = new JPanel();
        canvasPanel.setLayout(new BorderLayout());

        mainCanvas = new Canvas(
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration()
        );

        canvasPanel.add(mainCanvas, BorderLayout.CENTER);
        canvasPanel.addMouseWheelListener(e -> wheelVel = e.getUnitsToScroll());
        mainCanvas.setBackground(Color.BLACK);
        canvasPanel.setBackground(Color.BLACK);

        mainCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }



    public static void resize(){
        panelWidth = MainWindow.width - (TrajectoryEditorPanel.PANEL_WIDTH * 2);
        panelHeight = MainWindow.height;
        visiblePanelWidth = mainPanel.getVisibleRect().width;
        //visiblePanelHeight = panelHeight - GeneralUtils.JTabHeight;
        visiblePanelHeight = mainPanel.getVisibleRect().height;

        canvasWidth = visiblePanelWidth;
        canvasHeight = visiblePanelHeight;

        mainPanel.setSize(panelWidth, panelHeight);
        mainPanel.setPreferredSize(mainPanel.getSize());
    }

    public static void createBuffers(){
        mainCanvas.createBufferStrategy(2);
    }

    public static JTabbedPane getPanel(){
        return mainPanel;
    }

    public synchronized static TAB_SELECTION getTabSelection(){
        int tabIdx = mainPanel.getSelectedIndex();
        TAB_SELECTION output = null;
        if (tabIdx == TAB_SELECTION.VIEWPORT.idx){
            output = TAB_SELECTION.VIEWPORT;
        } else if (tabIdx == TAB_SELECTION.CODE_OUT.idx){
            output = TAB_SELECTION.CODE_OUT;
        } else if (tabIdx == TAB_SELECTION.SETTINGS.idx) {
            output = TAB_SELECTION.SETTINGS;
        }
        return output;
    }

    public synchronized static Canvas getMainCanvas(){
        return mainCanvas;
    }
}
