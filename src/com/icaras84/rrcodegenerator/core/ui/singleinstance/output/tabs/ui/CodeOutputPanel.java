package com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.ui;

import com.icaras84.rrcodegenerator.core.CodeGenCore;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.OutputPanelLogic;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;

import javax.swing.*;

public class CodeOutputPanel {
    private static JPanel mainPanel;

    private static JLabel codeGenOutput;
    private static String codeGenStringOutput;

    private static JPanel enabledTrajectoriesPanel;

    private static JPanel settingsPanel;

    private static JToolBar inputBar;
    private static JButton queueRender;
    private static JButton generateCode;
    private static JButton copyGeneratedCode;

    private CodeOutputPanel(){}

    public static void init(){
        mainPanel = new JPanel();
        codeGenOutput = new JLabel("Hello");

        initToolbar();
        arrange();
    }

    public static void arrange(){
        mainPanel.add(codeGenOutput);
    }

    private static void initToolbar(){
        /*
        generateCode = new JButton(new ImageIcon(GeneralUtils.getImage("/button_icons/intellij_icons/minimap_dark.png")));
        generateCode.addActionListener(OutputPanelLogic::generateCode);
        generateCode.setToolTipText("Generate trajectory code."); //icon might not be clear
        generateCode.setSize(16, 16);

         */

        copyGeneratedCode = new JButton(new ImageIcon(GeneralUtils.getImage("/button_icons/intellij_icons/copy_dark.png")));
        copyGeneratedCode.addActionListener(OutputPanelLogic::copyGeneratedCodeButton);
        copyGeneratedCode.setToolTipText("Copy generated trajectory code.");
        copyGeneratedCode.setSize(16, 16);

        queueRender = new JButton(new ImageIcon(GeneralUtils.getImage("/button_icons/intellij_icons/editSource_dark.png")));
        queueRender.addActionListener(OutputPanelLogic::queueRender);
        queueRender.setToolTipText("Render enabled trajectories.");
        queueRender.setSize(16, 16);

        inputBar = new JToolBar();
        inputBar.setSize(56, 16);
        inputBar.setPreferredSize(inputBar.getSize());
        inputBar.setMinimumSize(inputBar.getSize());
        inputBar.setMaximumSize(inputBar.getSize());
        inputBar.setBorder(null);
        inputBar.setFloatable(false);
        inputBar.setRollover(false);

        //inputBar.add(generateCode);
        inputBar.add(copyGeneratedCode);
        inputBar.add(queueRender);
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }
}
