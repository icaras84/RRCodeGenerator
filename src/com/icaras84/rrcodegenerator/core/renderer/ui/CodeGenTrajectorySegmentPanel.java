package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.icaras84.rrcodegenerator.core.trajectorycreation.TrajectoryCollection;
import com.icaras84.rrcodegenerator.core.trajectorycreation.TrajectoryOperation;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;
import com.icaras84.rrcodegenerator.core.utils.extraui.Pose2dJPanel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;

public class CodeGenTrajectorySegmentPanel extends JPanel {

    public final int WIDTH = 300, HEIGHT = 300;

    private CodeGenTrajectoryPanel parentPanel;

    private TrajectoryOperation trajectoryOp; //store an underlying representation of a trajectory segment

    private JComboBox<TrajectoryOperation.TRAJECTORY_TYPE> trajectoryTypeSelector; //UI for selecting type of segment
    private TrajectoryOperation.TRAJECTORY_TYPE trajType;

    private Pose2dJPanel endPoseEditor; //UI for editing end pose of trajectory segment

    private JFormattedTextField splineTangentTextBox;  //UI for customizing spline tangent
    private JCheckBox linkPose; //check box for if the spline tangent is same as heading
    private boolean isUsingTangent, isLinkedToPose;
    private double tangent; //actual numerical value for the spline tangent

    private JCheckBox usingMotionConstraintsBox; //check box to see if we need to generate constraints
    private boolean useConstraint;
    private JFormattedTextField velMax, accelMax; //UI for editing these constraints
    private double nVelMax, nAccelMax;

    public CodeGenTrajectorySegmentPanel(CodeGenTrajectoryPanel parentPanel){
        super();
        this.setLayout(new GridBagLayout());
        this.parentPanel = parentPanel;
        this.setSize(WIDTH, HEIGHT);
        this.setMinimumSize(getSize());
        this.setMaximumSize(getSize());
        init();
    }

    private void init(){
        createAllUI();
        arrangeAllUI();
    }

    private void createAllUI(){
        //create all the UI components for this end pose editor
        createTrajectoryTypeSelector();
        createPoseEditor();
        createSplineHeadingEditor();
        createConstraintEditor();
    }

    private void arrangeAllUI(){
        //arrange all the UI components
    }

    private void createTrajectoryTypeSelector() {
        trajType = TrajectoryOperation.TRAJECTORY_TYPE.lineTo;
        this.trajectoryOp = new TrajectoryOperation(trajType);

        this.trajectoryTypeSelector = new JComboBox<>(
                TrajectoryOperation.TRAJECTORY_TYPE.values()
        );

        trajectoryTypeSelector.setFocusable(false);


        this.trajectoryTypeSelector.addItemListener(e -> {
            if (e.getItem() instanceof TrajectoryOperation.TRAJECTORY_TYPE){
                trajType = (TrajectoryOperation.TRAJECTORY_TYPE) e.getItem();
                trajectoryOp.setTrajectoryType(trajType);

                switch (trajType){
                    case splineTo:
                    case splineToConstantHeading:
                    case splineToLinearHeading:
                    case splineToSplineHeading:
                        splineTangentTextBox.setEnabled(true);
                        isUsingTangent = true;
                        break;
                    case lineTo:
                    case lineToConstantHeading:
                    case lineToLinearHeading:
                    case lineToSplineHeading:
                        splineTangentTextBox.setEnabled(false);
                        isUsingTangent = false;
                        break;
                }
            }
        });
    }

    private void createPoseEditor(){
        endPoseEditor = new Pose2dJPanel();
    }

    private void createSplineHeadingEditor(){
        splineTangentTextBox = GeneralUtils.createRealNumberTextField();
        splineTangentTextBox.addPropertyChangeListener("value", e -> tangent = Double.parseDouble(splineTangentTextBox.getText()));

        linkPose = new JCheckBox("Use pose heading");
        linkPose.addItemListener(e -> isLinkedToPose = e.getStateChange() == ItemEvent.SELECTED);
    }

    private void createConstraintEditor(){
        //Set up formatting
        NumberFormat realNumFormat = NumberFormat.getNumberInstance();
        realNumFormat.setMaximumFractionDigits(4);
        NumberFormatter realNumberFormatterNonNegative = new NumberFormatter(realNumFormat);
        realNumberFormatterNonNegative.setValueClass(Double.class);
        realNumberFormatterNonNegative.setAllowsInvalid(true);
        realNumberFormatterNonNegative.setCommitsOnValidEdit(true);
        realNumberFormatterNonNegative.setMinimum(0.0d);

        velMax = new JFormattedTextField(realNumberFormatterNonNegative);
        velMax.setEnabled(useConstraint);
        velMax.addPropertyChangeListener("value", e -> nVelMax = Double.parseDouble(velMax.getText()));

        accelMax = new JFormattedTextField(realNumberFormatterNonNegative);
        accelMax.setEnabled(useConstraint);
        accelMax.addPropertyChangeListener("value", e -> nAccelMax = Double.parseDouble(accelMax.getText()));

        //edit constraints
        usingMotionConstraintsBox = new JCheckBox("Use Velocity & Acceleration Constraint");
        usingMotionConstraintsBox.setFocusable(false);
        usingMotionConstraintsBox.addItemListener(e -> {
            useConstraint = e.getStateChange() == ItemEvent.SELECTED;

            velMax.setEnabled(useConstraint);
            accelMax.setEnabled(useConstraint);
        });
    }

    public String generate(){

        Pose2d endPose = endPoseEditor.getPose2d();
        double endX = endPose.getX();
        double endY = endPose.getY();
        double endPoseHeading = endPose.getHeading();

        trajectoryOp.mapEndPos(String.format("new Vector2d(%f, %f)", endX, endY));
        trajectoryOp.mapEndPose(String.format("new Pose2d(%f, %f, %f)", endX, endY, endPoseHeading));

        if (isUsingTangent){
            if (isLinkedToPose){
                trajectoryOp.mapParameter(TrajectoryOperation.COMMON_PARAMS.END_HEADING, String.valueOf(endPoseHeading));
            } else {
                trajectoryOp.mapParameter(TrajectoryOperation.COMMON_PARAMS.END_HEADING, String.valueOf(tangent));
            }
        }

        trajectoryOp.setUseVelAndAccelConstraints(useConstraint);
        if (useConstraint){
            trajectoryOp.mapMaxVelAndAccel(
                    TrajectoryCollection.robotProperties.getMaxVelConstraintStringFilled(nVelMax),
                    String.format(TrajectoryCollection.robotProperties.getMaxAccelConstraintString(), nAccelMax)
            );
        }

        return trajectoryOp.generate();
    }

    public void delete(){
        parentPanel.requestDelete(this);
    }

    public TrajectoryOperation getTrajectoryOp(){
        return this.trajectoryOp;
    }

    public void insertSegmentIntoBuilder(TrajectoryBuilder builder){
        Pose2d endPose = endPoseEditor.getPose2d();
        double endX = endPose.getX();
        double endY = endPose.getY();
        double endPoseHeading = endPose.getHeading();

        //vel and accel constraints don't matter much to the shape of the trajectory
        switch (trajType){
            case lineTo:
                builder.lineTo(new Vector2d(endX, endY));
                break;
            case lineToConstantHeading:
                builder.lineToConstantHeading(new Vector2d(endX, endY));
                break;
            case lineToLinearHeading:
                builder.lineToLinearHeading(new Pose2d(endX, endY, endPoseHeading));
                break;
            case lineToSplineHeading:
                builder.lineToSplineHeading(new Pose2d(endX, endY, endPoseHeading));
                break;
            case splineTo:
                builder.splineTo(new Vector2d(endX, endY), tangent);
                break;
            case splineToConstantHeading:
                builder.splineToConstantHeading(new Vector2d(endX, endY), tangent);
                break;
            case splineToLinearHeading:
                builder.splineToLinearHeading(new Pose2d(endX, endY, endPoseHeading), tangent);
                break;
            case splineToSplineHeading:
                builder.splineToSplineHeading(new Pose2d(endX, endY, endPoseHeading), tangent);
                break;
        }
    }
}
