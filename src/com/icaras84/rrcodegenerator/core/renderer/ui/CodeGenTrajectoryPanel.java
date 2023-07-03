package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;
import com.icaras84.rrcodegenerator.core.utils.GeneralUtils;
import com.icaras84.rrcodegenerator.core.utils.extraui.Pose2dJPanel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.LinkedList;

public class CodeGenTrajectoryPanel extends JPanel {

    public enum START_POSE{
        POSE,
        TRAJECTORY_END
    }

    private LinkedList<CodeGenTrajectorySegmentPanel> trajectoryOpsPanels;
    private RobotProperties properties;

    private JCheckBox trajLocalVar; //determines if we put in or remove the "Trajectory" typing of a variable
    private boolean usingLocal;
    private JFormattedTextField trajName; //provides name of existing/new trajectory
    private String trajVarName;

    private JComboBox<START_POSE> startPoseType; //determines if we start from a custom coord or the end of another trajectory
    private START_POSE startPoseState;
    private Pose2dJPanel poseEditor; //pose editor for editing the start pose
    private JFormattedTextField trajectoryEnd; //if trajectory is staring from the end of an existing/generated traj, enter name
    private String trajEnd;

    private JCheckBox startTangentIsHeading; //find out if the tangent is differing from the robot heading at that point in time
    private JFormattedTextField startTangent; //get custom heading angle
    private boolean tangentIsPoseHeading;
    private double startTangentAngle;

    private Trajectory trajectory; //provides underlying trajectory to be queried or rendered

    public CodeGenTrajectoryPanel(RobotProperties properties){
        super();
        this.properties = properties;
        this.trajectoryOpsPanels = new LinkedList<>();
        this.trajectory = null;

        initUI();
    }

    private void initUI(){

        trajLocalVar = new JCheckBox();
        trajLocalVar.addItemListener(e -> usingLocal = e.getStateChange() == ItemEvent.SELECTED);

        trajectoryEnd = new JFormattedTextField();
        trajectoryEnd.addPropertyChangeListener("value", event -> {
            trajEnd = trajectoryEnd.getValue().toString();
        });

        //create drop-down menu for choosing the trajectory start type
        startPoseType = new JComboBox<>(START_POSE.values());
        startPoseType.addItemListener(e ->{
            if (e.getItem() instanceof START_POSE){
                startPoseState = (START_POSE) e.getItem();

                switch (startPoseState){
                    case POSE:
                        trajectoryEnd.setEnabled(false);
                        break;
                    case TRAJECTORY_END:
                        trajectoryEnd.setEnabled(true);
                        break;
                }
            }
        });

        poseEditor = new Pose2dJPanel();

        trajName = new JFormattedTextField();
        trajName.addPropertyChangeListener("value", event ->{
            trajVarName = trajName.getValue().toString();
            poseEditor.setEditorTitle(trajVarName);
        });

        startTangent = GeneralUtils.createRealNumberTextField();
        startTangent.addPropertyChangeListener("value", evt -> startTangentAngle = (double) startTangent.getValue());

        startTangentIsHeading = new JCheckBox();
        startTangentIsHeading.addItemListener(e -> {
            tangentIsPoseHeading = e.getStateChange() == ItemEvent.SELECTED;
            startTangent.setValue(poseEditor.getPose2d().getHeading());
        });

        arrangeUI();
    }

    private void arrangeUI(){

    }

    private void createSegment(){
        CodeGenTrajectorySegmentPanel segmentPanel = new CodeGenTrajectorySegmentPanel(this);
        trajectoryOpsPanels.add(segmentPanel);
        add(segmentPanel);
        regenerateTrajectory();
        repaint();
        revalidate();
    }

    public void requestDelete(CodeGenTrajectorySegmentPanel segmentPanel){
        trajectoryOpsPanels.remove(segmentPanel);
        remove(segmentPanel);
        regenerateTrajectory();
        repaint();
        revalidate();
    }

    private String getTrajectoryStart(){
        switch (startPoseState){
            case POSE:
                Pose2d pose = poseEditor.getPose2d();
                return String.format("new Pose2d(%s, %s, %s)", pose.getX(), pose.getY(), pose.getHeading());
            case TRAJECTORY_END:
                return trajEnd + ".end()";
        }
        return "";
    }

    public String generate(){
        StringBuilder output = new StringBuilder();
        if (usingLocal){
            output.append("Trajectory ");
        }

        if (tangentIsPoseHeading){
            startTangentAngle = poseEditor.getPose2d().getHeading();
        }

        output.append(trajName)
                .append(" = drive.trajectoryBuilder(")
                .append(getTrajectoryStart())
                .append(", ")
                .append(startTangentAngle)
                .append(")\n");

        trajectoryOpsPanels.forEach((op) -> output.append(op.getTrajectoryOp().generate()));

        return output.append(".build();").toString();
    }

    public void regenerateTrajectory(){
        TrajectoryBuilder builder = properties.constructTrajectoryBuilder(poseEditor.getPose2d(), startTangentAngle);
        trajectoryOpsPanels.forEach(t -> t.insertSegmentIntoBuilder(builder));
        this.trajectory = builder.build();
    }
}
