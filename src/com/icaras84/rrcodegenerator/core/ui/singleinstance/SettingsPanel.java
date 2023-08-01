package com.icaras84.rrcodegenerator.core.ui.singleinstance;

import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;

import javax.swing.*;

public class SettingsPanel {
    public static RobotProperties robotProperties;

    private static JPanel mainPanel;

    private SettingsPanel(){}

    public static void init(){
        mainPanel = new JPanel();

        MainWindow.submitResizeOperation(SettingsPanel::resize);
    }

    public static void arrange(){

    }

    public static void resize(){
        mainPanel.setSize(OutputPanel.visiblePanelWidth, OutputPanel.visiblePanelHeight);
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }
}
