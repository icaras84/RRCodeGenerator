package com.icaras84.rrcodegenerator.core.utils.info;

import com.icaras84.rrcodegenerator.core.utils.robot.RobotPropertyInfo;

public class SettingsInfo {
    private RobotPropertyInfo robotProperties;

    public SettingsInfo(){
        robotProperties = new RobotPropertyInfo();
    }

    public RobotPropertyInfo getRobotProperties() {
        return robotProperties;
    }

    public void setRobotProperties(RobotPropertyInfo robotProperties) {
        this.robotProperties = robotProperties;
    }

    public RobotPropertyInfo.ROBOT_TYPE getRobotType() {
        return robotProperties.getRobotType();
    }

    public void setRobotRobotType(RobotPropertyInfo.ROBOT_TYPE robotType) {
        robotProperties.setRobotType(robotType);
    }

    public double getRobotTrackWidth() {
        return robotProperties.getTrackWidth();
    }

    public void setRobotTrackWidth(double trackWidth) {
        robotProperties.setTrackWidth(trackWidth);
    }

    public double getRobotWheelbase() {
        return robotProperties.getWheelbase();
    }

    public void setRobotWheelbase(double wheelbase) {
        robotProperties.setWheelbase(wheelbase);
    }

    public double getRobotLateralMultiplier() {
        return robotProperties.getLateralMultiplier();
    }

    public void setRobotLateralMultiplier(double lateralMultiplier) {
        robotProperties.setLateralMultiplier(lateralMultiplier);
    }

    public double getRobotMaxVelocity() {
        return robotProperties.getMaxVelocity();
    }

    public void setRobotMaxVelocity(double maxVelocity) {
        robotProperties.setMaxVelocity(maxVelocity);
    }

    public double getRobotMaxAcceleration() {
        return robotProperties.getMaxAcceleration();
    }

    public void setRobotMaxAcceleration(double maxAcceleration) {
        robotProperties.setMaxAcceleration(maxAcceleration);
    }

    public double getRobotMaxAngVelocity() {
        return robotProperties.getMaxAngVelocity();
    }

    public void setRobotMaxAngVelocity(double maxAngVelocity) {
        robotProperties.setMaxAngVelocity(maxAngVelocity);
    }

    public double getRobotMaxAngAcceleration() {
        return robotProperties.getMaxAngAcceleration();
    }

    public void setRobotMaxAngAcceleration(double maxAngAcceleration) {
        robotProperties.setMaxAngAcceleration(maxAngAcceleration);
    }
}
