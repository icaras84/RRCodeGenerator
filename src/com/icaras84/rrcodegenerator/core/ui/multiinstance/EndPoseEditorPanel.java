package com.icaras84.rrcodegenerator.core.ui.multiinstance;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.icaras84.rrcodegenerator.core.trajectorycreation.TrajectoryOperation;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;
import com.icaras84.rrcodegenerator.core.utils.extraui.Pose2dJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class EndPoseEditorPanel extends JPanel {

    public final static int PANEL_WIDTH = 275, PANEL_HEIGHT = 240;

    private TrajectoryEditorPanel mainTrajectoryEditor;

    private JToolBar operations;
    private JComboBox<TrajectoryOperation.TRAJECTORY_TYPE> pathTypeBox;
    private TrajectoryOperation.TRAJECTORY_TYPE pathType;

    private JButton deleteButton;

    private Pose2dJPanel poseEditor;

    private JCheckBox splineTangentAsHeading;
    private JLabel tangentLabel;
    private JFormattedTextField tangentEditor;
    private boolean isTangentEnabled;
    private double splineTangentValue;

    private JCheckBox useConstraints;
    private JLabel velocityLabel;
    private JLabel accelerationLabel;
    private JFormattedTextField velocityEditor;
    private JFormattedTextField accelerationEditor;
    private double velConstraintValue;
    private double accelConstraintValue;

    public EndPoseEditorPanel(TrajectoryEditorPanel mainTrajectoryEditor){
        super();
        this.mainTrajectoryEditor = mainTrajectoryEditor;

        init();
        arrange();
    }

    public void init(){
        this.setLayout(new GridBagLayout());
        this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        this.setMaximumSize(this.getSize());
        this.setMinimumSize(this.getSize());
        this.setPreferredSize(this.getSize());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        operations = new JToolBar();


        pathTypeBox = new JComboBox<>(TrajectoryOperation.TRAJECTORY_TYPE.values());
        pathTypeBox.addItemListener(this::updateTangentBoxStatus);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::requestDelete);

        operations.add(pathTypeBox);
        operations.add(deleteButton);

        poseEditor = new Pose2dJPanel("Segment End Pose");

        splineTangentAsHeading = new JCheckBox("Tangent is heading");
        splineTangentAsHeading.addItemListener(this::updateSplineTangentHandling);
        splineTangentAsHeading.setEnabled(false);

        tangentLabel = new JLabel(" Spline Tangent:");
        tangentEditor = GeneralUtils.createRealNumberTextField();
        tangentEditor.addPropertyChangeListener("value", this::updateSplineTangentValue);
        tangentEditor.setEnabled(false);

        useConstraints = new JCheckBox("Use Velocity and Acceleration Constraints");
        useConstraints.addItemListener(this::updateConstraintBoxStatus);

        velocityLabel = new JLabel(" Velocity:");
        velocityEditor = GeneralUtils.createNonNegativeNumberTextField();
        velocityEditor.addPropertyChangeListener("value", this::updateVelocityConstraint);
        velocityEditor.setEnabled(false);

        accelerationLabel = new JLabel(" Acceleration:");
        accelerationEditor = GeneralUtils.createNonNegativeNumberTextField();
        accelerationEditor.addPropertyChangeListener("value", this::updateAccelerationConstraint);
        accelerationEditor.setEnabled(false);
    }

    public void arrange(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;

        this.add(operations, gbc);

        poseEditor.setBorder(null);
        gbc.gridheight = 4;
        gbc.gridy = 1;
        this.add(poseEditor, gbc);

        gbc.gridheight = 1;
        gbc.gridy = 5;
        this.add(splineTangentAsHeading, gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        this.add(tangentLabel, gbc);

        gbc.gridx = 1;
        this.add(tangentEditor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        this.add(useConstraints, gbc);

        gbc.gridy = 8;
        gbc.gridwidth = 1;
        this.add(velocityLabel, gbc);

        gbc.gridx = 1;
        this.add(velocityEditor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        this.add(accelerationLabel, gbc);

        gbc.gridx = 1;
        this.add(accelerationEditor, gbc);
    }

    private void updateTangentBoxStatus(ItemEvent e){
        pathType = (TrajectoryOperation.TRAJECTORY_TYPE) pathTypeBox.getSelectedItem();
        switch (Objects.requireNonNull(pathType)){
            case splineTo:
            case splineToLinearHeading:
            case splineToSplineHeading:
            case splineToConstantHeading:
                splineTangentAsHeading.setEnabled(true);
                isTangentEnabled = true;
                break;
            default:
                splineTangentAsHeading.setEnabled(false);
                isTangentEnabled = false;
        }

        tangentEditor.setEnabled(isTangentEnabled);
    }

    private void updateSplineTangentHandling(ItemEvent e){
        if (splineTangentAsHeading.isSelected()){
            tangentEditor.setEnabled(false);
            splineTangentValue = poseEditor.getPose2d().getHeading();
        } else {
            tangentEditor.setEnabled(isTangentEnabled);
            splineTangentValue = ((Number) tangentEditor.getValue()).doubleValue();
        }
    }

    private void updateSplineTangentValue(PropertyChangeEvent e){
        if (tangentEditor.isEnabled())
            splineTangentValue = ((Number) tangentEditor.getValue()).doubleValue();
    }

    private void updateConstraintBoxStatus(ItemEvent e){
        velocityEditor.setEnabled(useConstraints.isSelected());
        accelerationEditor.setEnabled(useConstraints.isSelected());
    }

    private void updateVelocityConstraint(PropertyChangeEvent e){
        velConstraintValue = ((Number) velocityEditor.getValue()).doubleValue();
    }

    private void updateAccelerationConstraint(PropertyChangeEvent e){
        accelConstraintValue = ((Number) accelerationEditor.getValue()).doubleValue();
    }
    public void requestDelete(){
        requestDelete(null);
    }

    private void requestDelete(ActionEvent e){
        mainTrajectoryEditor.subPanelRequestDelete(this);
        mainTrajectoryEditor.remove(this);
    }

    public TrajectoryOperation.TRAJECTORY_TYPE getPathType(){
        return pathType;
    }

    public Pose2d getEndPose(){
        return poseEditor.getPose2d();
    }

    public double getSplineTangentValue(){
        return splineTangentValue;
    }

    public boolean isUsingConstraints(){
        return useConstraints.isSelected();
    }

    public double getVelocityConstraintValue(){
        return velConstraintValue;
    }

    public double getAccelerationConstraintValue(){
        return accelConstraintValue;
    }
}
