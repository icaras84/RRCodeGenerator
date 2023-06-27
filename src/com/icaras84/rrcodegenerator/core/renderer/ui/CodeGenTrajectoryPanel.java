package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Vector;

public class CodeGenTrajectoryPanel extends JPanel {

    public enum START_POSE{
        POSE,
        TRAJECTORY_END
    }

    private LinkedList<CodeGenTrajectorySegmentPanel> trajectoryOps;
    private RobotProperties properties;

    private JComboBox<START_POSE> startPoseType;

    public CodeGenTrajectoryPanel(RobotProperties properties){
        super();
        this.properties = properties;
        this.trajectoryOps = new LinkedList<>();

        initUI();
    }

    private void initUI(){

        //create drop-down menu for choosing the trajectory start type
        Vector<START_POSE> options = new Vector<>();
        options.add(START_POSE.POSE);
        options.add(START_POSE.TRAJECTORY_END);
        startPoseType = new JComboBox<>(options);
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
}
