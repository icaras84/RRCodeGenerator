package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;
import com.icaras84.rrcodegenerator.core.utils.extraui.Pose2dJPanel;

import javax.swing.*;
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
    private JTextField trajName; //provides name of existing/new trajectory
    private String trajVarName;

    private JComboBox<START_POSE> startPoseType; //determines if we start from a custom coord or the end of another trajectory
    private START_POSE startPoseState;
    private Pose2dJPanel poseEditor; //pose editor for editing the start pose
    private JTextField trajectoryEnd; //if trajectory is staring from the end of an existing/generated traj, enter name
    private String trajEnd;

    private JCheckBox startTangentIsHeading; //find out if the tangent is differing from the robot heading at that point in time
    private JFormattedTextField startTangent; //get custom heading angle

    private Trajectory trajectory; //provides underlying trajectory to be queried or rendered

    public CodeGenTrajectoryPanel(RobotProperties properties){
        super();
        this.properties = properties;
        this.trajectoryOpsPanels = new LinkedList<>();
        this.trajectory = null;

        initUI();
    }

    private void initUI(){

        //create drop-down menu for choosing the trajectory start type
        startPoseType = new JComboBox<>(START_POSE.values());
        startPoseType.addItemListener(e ->{
            if (e.getItem() instanceof START_POSE){
                startPoseState = (START_POSE) e.getItem();

                switch (startPoseState){
                    case POSE:
                        trajectoryEnd.setVisible(false);
                        break;
                    case TRAJECTORY_END:
                        trajectoryEnd.setVisible(true);
                        break;
                }
            }
        });

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
        output.append(trajName)
                .append(" = drive.trajectorySequenceBuilder(")
                .append(getTrajectoryStart())
                .append(")\n");

        trajectoryOpsPanels.forEach((op) -> output.append(op.getTrajectoryOp().generate()));

        return output.append(".build();").toString();
    }

    public void regenerateTrajectory(){
        TrajectoryBuilder builder = properties.constructTrajectoryBuilder(poseEditor.getPose2d(), poseEditor.getPose2d().getHeading());
        trajectoryOpsPanels.forEach(t -> t.insertSegmentIntoBuilder(builder));
        this.trajectory = builder.build();
    }
}
