package com.icaras84.rrcodegenerator.core.roadrunnerqscore;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.constraints.*;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.trajectorysequence.TrajectorySequenceBuilder;

public class RobotProperties {

    public enum ROBOT_TYPE{
        CUSTOM,
        MECANUM,
        TANK,
        SWERVE
    }

    private ROBOT_TYPE robotType;
    private double trackWidth, wheelbase;
    private double maxVelocity, maxAcceleration;
    private double maxAngVelocity, maxAngAcceleration;

    public RobotProperties(){
        this(
                ROBOT_TYPE.MECANUM,
                17,
                17,
                30,
                60,
                Math.PI / 3,
                Math.PI / 3
        );
    }

    public RobotProperties(ROBOT_TYPE robotType, double trackWidth, double wheelbase, double maxVelocity, double maxAcceleration, double maxAngVelocity, double maxAngAcceleration) {
        this.robotType = robotType;
        this.trackWidth = trackWidth;
        this.wheelbase = wheelbase;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxAngVelocity = maxAngVelocity;
        this.maxAngAcceleration = maxAngAcceleration;
    }

    public TrajectorySequenceBuilder constructTrajectoryBuilder(Pose2d startPose, double startTangent){
        switch (robotType){
            case SWERVE:
                return new TrajectorySequenceBuilder(
                        startPose,
                        startTangent,
                        new SwerveVelocityConstraint(
                                maxVelocity,
                                trackWidth,
                                wheelbase),
                        new ProfileAccelerationConstraint(maxAcceleration),
                        maxAngVelocity,
                        maxAngAcceleration);
            case TANK:
                return new TrajectorySequenceBuilder(
                        startPose,
                        startTangent,
                        new TankVelocityConstraint(
                                maxVelocity,
                                trackWidth),
                        new ProfileAccelerationConstraint(maxAcceleration),
                        maxAngVelocity,
                        maxAngAcceleration);
            case MECANUM:
            case CUSTOM:
            default:
                return new TrajectorySequenceBuilder(
                        startPose,
                        startTangent,
                        new MecanumVelocityConstraint(
                                maxVelocity,
                                trackWidth),
                        new ProfileAccelerationConstraint(maxAcceleration),
                        maxAngVelocity,
                        maxAngAcceleration);
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
            case MECANUM:
            case CUSTOM:
            default:
                return new MecanumVelocityConstraint(maxVelocity, trackWidth);
        }
    }

    public String getMaxVelConstraintString(){
        switch (robotType){
            case SWERVE:
                return "new SwerveVelocityConstraint(%s, %s, %s)";
            case TANK:
                return "new TankVelocityConstraint(%s, %s)";
            case MECANUM:
            case CUSTOM:
            default:
                return "new MecanumVelocityConstraint(%s, %s)";
        }
    }

    public String getMaxVelConstraintStringFilled(double vel){
        switch (robotType){
            case SWERVE:
                return String.format("new SwerveVelocityConstraint(%s, %s, %s)", vel, trackWidth, wheelbase);
            case TANK:
                return String.format("new TankVelocityConstraint(%s, %s)", vel, trackWidth);
            case MECANUM:
            case CUSTOM:
            default:
                return String.format("new MecanumVelocityConstraint(%s, %s)", vel, trackWidth);
        }
    }

    public TrajectoryAccelerationConstraint getMaxAccelConstraint(){
        return new ProfileAccelerationConstraint(maxAcceleration);
    }

    public String getMaxAccelConstraintString(){
        return "new ProfileAccelerationConstraint(%s)";
    }
}
