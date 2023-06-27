package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;
import com.icaras84.rrcodegenerator.core.utils.extraui.Pose2dJPanel;

import javax.swing.*;
import java.util.LinkedList;

public class CodeGenTrajectoryPanel extends JPanel {

    public enum START_POSE{
        POSE,
        TRAJECTORY_END
    }

    private LinkedList<CodeGenTrajectorySegmentPanel> trajectoryOps;
    private RobotProperties properties;

    private JComboBox<START_POSE> startPoseType;
    private START_POSE startPoseState;
    private Pose2dJPanel poseEditor;
    private JTextField trajectoryEnd;
    private String trajEnd;

    private JCheckBox trajLocalVar;
    private boolean usingLocal;

    private JTextField trajName;
    private String trajVarName;

    public CodeGenTrajectoryPanel(RobotProperties properties){
        super();
        this.properties = properties;
        this.trajectoryOps = new LinkedList<>();

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
        CodeGenTrajectorySegmentPanel segmentPanel = new CodeGenTrajectorySegmentPanel(this, properties);
        trajectoryOps.add(segmentPanel);
        add(segmentPanel);
        repaint();
        revalidate();
    }

    public void requestDelete(CodeGenTrajectorySegmentPanel segmentPanel){
        trajectoryOps.remove(segmentPanel);
        remove(segmentPanel);
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

        trajectoryOps.forEach((op) -> output.append(op.getTrajectoryOp().generate()));

        output.append(".build();");
        return "";
    }
}
