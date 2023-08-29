package com.icaras84.rrcodegenerator.core.ui.singleinstance.tools.ui;

import javax.swing.*;
import java.awt.*;

public class ToolPanel {
    private static JPanel mainPanel;

    private ToolPanel(){}

    public static void init(){
        mainPanel = new JPanel();

        TimelinePlayer.init();

        arrange();
    }

    public static void arrange(){
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(TimelinePlayer.getPanel());
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }
}
