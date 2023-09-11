package com.icaras84.rrcodegenerator.core.ui.singleinstance.tools.ui;

import com.icaras84.rrcodegenerator.core.ui.singleinstance.nav.logic.NavigationPanelLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolPanel {
    private static JPanel mainPanel;
    private static JButton build;

    private ToolPanel(){}

    public static void init(){
        mainPanel = new JPanel();

        TimelinePlayer.init();
        build = new JButton("Build");
        build.addActionListener(e -> NavigationPanelLogic.updateTrajectoryCurrentBuild());

        arrange();
    }

    public static void arrange(){
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(build);
        mainPanel.add(TimelinePlayer.getPanel());
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }
}
