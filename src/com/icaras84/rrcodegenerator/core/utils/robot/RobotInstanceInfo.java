package com.icaras84.rrcodegenerator.core.utils.robot;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;

import java.util.Vector;

public class RobotInstanceInfo {
    private Matrix3x3 transform;
    private Vector<RobotComponent> components;

    public RobotInstanceInfo(){
        transform = new Matrix3x3();
        components = new Vector<>();
    }

    public void setTransform(Pose2d robotPose){
        transform = Matrix3x3.transform(robotPose);
    }

    public void addComponent(RobotComponent part){
        components.add(part);
    }

    public void removeComponent(RobotComponent part){
        components.remove(part);
    }

    public void render(){
        for (RobotComponent part: components) {
            part.render(transform);
        }
    }
}
