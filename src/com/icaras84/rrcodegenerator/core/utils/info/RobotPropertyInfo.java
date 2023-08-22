package com.icaras84.rrcodegenerator.core.utils.info;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.*;

public class RobotPropertyInfo {

    public enum ROBOT_TYPE{
        MECANUM,
        TANK,
        SWERVE
    }

    private ROBOT_TYPE robotType;
    private double trackWidth, wheelbase, lateralMultiplier;
    private double maxVelocity, maxAcceleration;
    private double maxAngVelocity, maxAngAcceleration;

    public RobotPropertyInfo(){
        this(
                ROBOT_TYPE.MECANUM,
                17,
                17,
                1.2,
                30,
                60,
                Math.PI / 3,
                Math.PI / 3
        );
    }

    public RobotPropertyInfo(ROBOT_TYPE robotType, double trackWidth, double wheelbase, double lateralMultiplier, double maxVelocity, double maxAcceleration, double maxAngVelocity, double maxAngAcceleration) {
        this.robotType = robotType;
        this.trackWidth = trackWidth;
        this.wheelbase = wheelbase;
        this.lateralMultiplier = lateralMultiplier;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxAngVelocity = maxAngVelocity;
        this.maxAngAcceleration = maxAngAcceleration;
    }

    public TrajectoryBuilder constructTrajectoryBuilder(Pose2d startPose, double startTangent){
        switch (robotType){
            case SWERVE:
                return new TrajectoryBuilder(
                        startPose,
                        startTangent,
                        new SwerveVelocityConstraint(
                                maxVelocity,
                                trackWidth,
                                wheelbase),
                        new ProfileAccelerationConstraint(maxAcceleration)
                );
            case TANK:
                return new TrajectoryBuilder(
                        startPose,
                        startTangent,
                        new TankVelocityConstraint(
                                maxVelocity,
                                trackWidth),
                        new ProfileAccelerationConstraint(maxAcceleration)
                );
            default:
                return new TrajectoryBuilder(
                        startPose,
                        startTangent,
                        new MecanumVelocityConstraint(
                                maxVelocity,
                                trackWidth),
                        new ProfileAccelerationConstraint(maxAcceleration)
                );
        }
    }

    public ROBOT_TYPE getRobotType() {
        return robotType;
    }

    public void setRobotType(ROBOT_TYPE robotType) {
        this.robotType = robotType;
    }

    public double getTrackWidth() {
        return trackWidth;
    }

    public void setTrackWidth(double trackWidth) {
        this.trackWidth = trackWidth;
    }

    public double getWheelbase() {
        return wheelbase;
    }

    public void setWheelbase(double wheelbase) {
        this.wheelbase = wheelbase;
    }

    public double getLateralMultiplier() {
        return lateralMultiplier;
    }

    public void setLateralMultiplier(double lateralMultiplier) {
        this.lateralMultiplier = lateralMultiplier;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public double getMaxAngVelocity() {
        return maxAngVelocity;
    }

    public void setMaxAngVelocity(double maxAngVelocity) {
        this.maxAngVelocity = maxAngVelocity;
    }

    public double getMaxAngAcceleration() {
        return maxAngAcceleration;
    }

    public void setMaxAngAcceleration(double maxAngAcceleration) {
        this.maxAngAcceleration = maxAngAcceleration;
    }

    public TrajectoryVelocityConstraint getMaxVelConstraint(){
        switch (robotType){
            case SWERVE:
                return new SwerveVelocityConstraint(maxVelocity, trackWidth, wheelbase);
            case TANK:
                return new TankVelocityConstraint(maxVelocity, trackWidth);
            default:
                return new MecanumVelocityConstraint(maxVelocity, trackWidth, wheelbase, lateralMultiplier);
        }
    }

    public String getMaxVelConstraintString(){
        switch (robotType){
            case SWERVE:
                return "new SwerveVelocityConstraint(%s, %s, %s)";
            case TANK:
                return "new TankVelocityConstraint(%s, %s)";
            default:
                return "new MecanumVelocityConstraint(%s, %s, %s, %s)";
        }
    }

    public TrajectoryVelocityConstraint getMaxVelConstraint(double vel){
        switch (robotType){
            case SWERVE:
                return new SwerveVelocityConstraint(vel, trackWidth, wheelbase);
            case TANK:
                return new TankVelocityConstraint(vel, trackWidth);
            default:
                return new MecanumVelocityConstraint(vel, trackWidth, wheelbase, lateralMultiplier);
        }
    }

    public String getMaxVelConstraintStringFilled(double vel){
        switch (robotType){
            case SWERVE:
                return String.format("new SwerveVelocityConstraint(%s, %s, %s)", vel, trackWidth, wheelbase);
            case TANK:
                return String.format("new TankVelocityConstraint(%s, %s)", vel, trackWidth);
            default:
                return String.format("new MecanumVelocityConstraint(%s, %s, %s, %s)", vel, trackWidth, wheelbase, lateralMultiplier);
        }
    }

    public TrajectoryAccelerationConstraint getMaxAccelConstraint(){
        return new ProfileAccelerationConstraint(maxAcceleration);
    }

    public TrajectoryAccelerationConstraint getMaxAccelConstraint(double accel){
        return new ProfileAccelerationConstraint(accel);
    }

    public String getMaxAccelConstraintString(){
        return "new ProfileAccelerationConstraint(%s)";
    }

    public String getMaxAccelConstraintStringFilled(double accel){
        return String.format("new ProfileAccelerationConstraint(%s)", accel);
    }
}
