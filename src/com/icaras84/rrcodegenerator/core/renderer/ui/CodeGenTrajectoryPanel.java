package com.icaras84.rrcodegenerator.core.renderer.ui;

import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;

import javax.swing.*;
import java.util.LinkedList;

public class CodeGenTrajectoryPanel extends JPanel {

    private LinkedList<CodeGenTrajectorySegmentPanel> trajectoryOps;
    private RobotProperties properties;

    public CodeGenTrajectoryPanel(RobotProperties properties){
        super();
        this.properties = properties;
        this.trajectoryOps = new LinkedList<>();
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
