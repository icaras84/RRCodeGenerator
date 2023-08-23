package com.icaras84.rrcodegenerator.core.ui.singleinstance.output.settings.ui;

import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.OutputPanel;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.window.MainWindow;

import javax.swing.*;

public class SettingsPanel {
    private static JPanel mainPanel;

    private SettingsPanel(){}

    public static void init(){
        mainPanel = new JPanel();

        RobotPropertiesSettingsPanel.init();

        arrange();
        MainWindow.submitResizeOperation(SettingsPanel::resize);
    }

    public static void arrange(){
        mainPanel.add(RobotPropertiesSettingsPanel.getMainPanel());
    }

    public static void resize(){
        mainPanel.setSize(OutputPanel.visiblePanelWidth, OutputPanel.visiblePanelHeight);
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }
}
