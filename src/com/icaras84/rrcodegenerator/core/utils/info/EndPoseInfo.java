package com.icaras84.rrcodegenerator.core.utils.info;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.logic.SettingsLogic;

public class EndPoseInfo {
    public enum TRAJECTORY_SEGMENT_TYPE {
        lineTo,
        lineToConstantHeading,
        lineToLinearHeading,
        lineToSplineHeading,

        splineTo,
        splineToConstantHeading,
        splineToLinearHeading,
        splineToSplineHeading
    }

    private EndPoseInfo.TRAJECTORY_SEGMENT_TYPE pathType;
    private double x, y, heading, splineTangent;
    private boolean isUsingMovementConstraints;
    private double velConstraint;
    private double accelConstraint;

    public EndPoseInfo(TrajectoryInfo mainTrajectory) {
        pathType = TRAJECTORY_SEGMENT_TYPE.splineTo;
        add(mainTrajectory);
    }

    public EndPoseInfo(EndPoseInfo poseInfo){
        this.pathType = poseInfo.pathType;
        setEndPose(poseInfo.getEndPose());
        this.isUsingMovementConstraints = poseInfo.isUsingMovementConstraints;
        this.velConstraint = poseInfo.velConstraint;
        this.accelConstraint = poseInfo.accelConstraint;
    }

    public void add(TrajectoryInfo parentTrajectory){
        parentTrajectory.add(this);
    }

    public void delete(TrajectoryInfo parentTrajectory) {
        parentTrajectory.delete(this);
    }

    public EndPoseInfo.TRAJECTORY_SEGMENT_TYPE getPathType() {
        return pathType;
    }

    public void setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE pathType) {
        this.pathType = pathType;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getSplineTangent() {
        return splineTangent;
    }

    public void setSplineTangent(double splineTangent) {
        this.splineTangent = splineTangent;
    }

    public Pose2d getEndPose() {
        return new Pose2d(x, y, heading);
    }

    public void setEndPose(Pose2d pose) {
        x = pose.getX();
        y = pose.getY();
        heading = pose.getHeading();
    }

    public boolean isUsingMovementConstraints() {
        return isUsingMovementConstraints;
    }

    public void setUsingMovementConstraints(boolean usingMovementConstraints) {
        isUsingMovementConstraints = usingMovementConstraints;
    }

    public double getVelConstraint() {
        return velConstraint;
    }

    public void setVelConstraint(double velConstraint) {
        this.velConstraint = velConstraint;
    }

    public double getAccelConstraint() {
        return accelConstraint;
    }

    public void setAccelConstraint(double accelConstraint) {
        this.accelConstraint = accelConstraint;
    }

    public String getAsMethodString() {
        StringBuilder output = new StringBuilder(".");
        switch (pathType) {
            case lineTo:
                output.append(String.format("lineTo(new Vector2d(%.3f, %.3f)", x, y));
                break;
            case lineToConstantHeading:
                output.append(String.format("lineToConstantHeading(new Vector2d(%.3f, %.3f)", x, y));
                break;
            case lineToLinearHeading:
                output.append(String.format("lineToLinearHeading(new Pose2d(%.3f, %.3f, %.3f)", x, y, heading));
                break;
            case lineToSplineHeading:
                output.append(String.format("lineToSplineHeading(new Pose2d(%.3f, %.3f, %.3f)", x, y, heading));
                break;
            case splineTo:
                output.append(String.format("splineTo(new Vector2d(%.3f, %.3f), %.3f", x, y, splineTangent));
                break;
            case splineToConstantHeading:
                output.append(String.format("splineToConstantHeading(new Vector2d(%.3f, %.3f), %.3f", x, y, splineTangent));
                break;
            case splineToLinearHeading:
                output.append(String.format("splineToLinearHeading(new Pose2d(%.3f, %.3f, %.3f), %.3f", x, y, heading, splineTangent));
                break;
            case splineToSplineHeading:
                output.append(String.format("splineToSplineHeading(new Pose2d(%.3f, %.3f, %.3f), %.3f", x, y, heading, splineTangent));
                break;
        }

        if (isUsingMovementConstraints) {
            output.append(", ");
            output.append(SettingsLogic.settingsInfo.getRobotProperties().getMaxVelConstraintStringFilled(velConstraint));
            output.append(", ");
            output.append(SettingsLogic.settingsInfo.getRobotProperties().getMaxAccelConstraintStringFilled(accelConstraint));
        }

        output.append(")");
        return output.toString();
    }
}
