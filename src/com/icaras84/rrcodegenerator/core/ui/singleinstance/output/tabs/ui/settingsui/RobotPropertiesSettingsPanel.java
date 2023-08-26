package com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.ui.settingsui;

import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.logic.SettingsLogic;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;
import com.icaras84.rrcodegenerator.core.utils.robot.RobotPropertyInfo;

import javax.swing.*;
import java.awt.*;

public class RobotPropertiesSettingsPanel {

    private static JPanel mainPanel;

    private static JLabel panelTitle;

    private static JLabel robotTypeLabel;
    private static JComboBox<RobotPropertyInfo.ROBOT_TYPE> robotTypeChooser;

    private static JLabel
            trackWidthLabel,
            wheelbaseLabel,
            lateralMultiplierLabel,
            maxVelocityLabel,
            maxAccelerationLabel,
            maxAngVelocityLabel,
            maxAngAccelerationLabel;

    private static JFormattedTextField
            trackWidthEditor,
            wheelbaseEditor,
            lateralMultiplierEditor,
            maxVelocityEditor,
            maxAccelerationEditor,
            maxAngVelocityEditor,
            maxAngAccelerationEditor;

    public static void init(){
        mainPanel = new JPanel();
        panelTitle = new JLabel("— Robot Properties —");

        robotTypeLabel = new JLabel("Robot Type: ");
        trackWidthLabel = new JLabel("Track Width: ");
        wheelbaseLabel = new JLabel("Wheelbase: ");
        lateralMultiplierLabel = new JLabel("Lateral Multiplier: ");
        maxVelocityLabel = new JLabel("Max Velocity: ");
        maxAccelerationLabel = new JLabel("Max Acceleration: ");
        maxAngVelocityLabel = new JLabel("Max Angular Velocity: ");
        maxAngAccelerationLabel = new JLabel("Max Angular Acceleration: ");

        robotTypeChooser = new JComboBox<>(RobotPropertyInfo.ROBOT_TYPE.values());
        robotTypeChooser.addItemListener((e) -> SettingsLogic.changeRobotType((RobotPropertyInfo.ROBOT_TYPE) robotTypeChooser.getSelectedItem()));

        trackWidthEditor = GeneralUtils.createNonNegativeNumberTextField();
        wheelbaseEditor = GeneralUtils.createNonNegativeNumberTextField();
        lateralMultiplierEditor = GeneralUtils.createNonNegativeNumberTextField();
        maxVelocityEditor = GeneralUtils.createNonNegativeNumberTextField();
        maxAccelerationEditor = GeneralUtils.createNonNegativeNumberTextField();
        maxAngVelocityEditor = GeneralUtils.createNonNegativeNumberTextField();
        maxAngAccelerationEditor = GeneralUtils.createNonNegativeNumberTextField();

        arrange();
    }

    public static void arrange(){
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(panelTitle, gbc);

        pairEditorAndLabel(1, robotTypeLabel, robotTypeChooser);
        pairEditorAndLabel(2, trackWidthLabel, trackWidthEditor);
        pairEditorAndLabel(3, wheelbaseLabel, wheelbaseEditor);
        pairEditorAndLabel(4, lateralMultiplierLabel, lateralMultiplierEditor);
        pairEditorAndLabel(5, maxAngVelocityLabel, maxAngVelocityEditor);
        pairEditorAndLabel(6, maxAngAccelerationLabel, maxAngAccelerationEditor);
        pairEditorAndLabel(7, maxVelocityLabel, maxVelocityEditor);
        pairEditorAndLabel(8, maxAccelerationLabel, maxAccelerationEditor);
    }

    private static void pairEditorAndLabel(int row, JLabel label, JComponent component){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        gbc.weighty = 1/9d;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        mainPanel.add(label, gbc);
        gbc.gridx = 1;
        mainPanel.add(component, gbc);
    }

    public static JPanel getMainPanel(){
        return mainPanel;
    }
}
