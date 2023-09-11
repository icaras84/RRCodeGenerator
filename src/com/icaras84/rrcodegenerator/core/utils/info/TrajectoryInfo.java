package com.icaras84.rrcodegenerator.core.utils.info;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.icaras84.rrcodegenerator.core.utils.robot.RobotPropertyInfo;

import java.util.Arrays;
import java.util.Vector;
import java.util.function.Supplier;

public class TrajectoryInfo {



    private String trajectoryName;
    private double startX, startY, startHeading, startTangent;
    private Vector<EndPoseInfo> endPoses;
    private transient boolean isValid; //doesn't need to be serialized

    public TrajectoryInfo(){
        trajectoryName = "_defaultName";
        endPoses = new Vector<>();
    }

    public TrajectoryInfo(TrajectoryInfo toCopy){
        load(toCopy);
    }

    public void load(TrajectoryInfo toCopy){
        setStartPose(toCopy.getStartPose());
        startTangent = toCopy.startTangent;
        trajectoryName = toCopy.trajectoryName;
        endPoses = new Vector<>(toCopy.endPoses);
    }

    public void add(EndPoseInfo endPoseInfo){
        endPoses.add(endPoseInfo);
    }

    public void add(Supplier<EndPoseInfo> endPoseInfoSupplier){
        endPoses.add(endPoseInfoSupplier.get());
    }

    public void delete(EndPoseInfo endPoseInfo){
        endPoses.remove(endPoseInfo);
    }

    public void delete(int idx){
        endPoses.remove(idx);
    }

    public void delete(int... indices){
        Arrays.sort(indices);
        for (int i = 0; i < indices.length; i++) {
            endPoses.remove(indices[indices.length - i - 1]);
        }
    }

    public void revalidate(){
        Vector<EndPoseInfo> poseCache = new Vector<>(endPoses);
        endPoses.clear();
        for (EndPoseInfo pose: poseCache) endPoses.add(new EndPoseInfo(pose));
        poseCache.clear();
    }

    public String getTrajectoryName() {
        return trajectoryName;
    }

    public void setTrajectoryName(String trajectoryName) {
        this.trajectoryName = trajectoryName;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getStartHeading() {
        return startHeading;
    }

    public void setStartHeading(double startHeading) {
        this.startHeading = startHeading;
    }

    public double getStartTangent() {
        return startTangent;
    }

    public void setStartTangent(double startTangent) {
        this.startTangent = startTangent;
    }

    public Pose2d getStartPose(){
        return new Pose2d(startX, startY, startHeading);
    }

    public void setStartPose(Pose2d startPose){
        startX = startPose.getX();
        startY = startPose.getY();
        startHeading = startPose.getHeading();
    }

    public Vector<EndPoseInfo> getEndPoses() {
        return endPoses;
    }

    public void setEndPoses(Vector<EndPoseInfo> endPoses) {
        this.endPoses = endPoses;
    }

    public int getEndPoseBufferLength(){
        return endPoses.size();
    }

    public EndPoseInfo getEndPose(int n){
        return endPoses.get(n);
    }

    public void setEndPose(int n, EndPoseInfo poseInfo){
        endPoses.set(n, poseInfo);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String createCodeString(RobotPropertyInfo robotPropertyInfo){
        StringBuilder output = new StringBuilder();

        return output.toString();
    }

    public String toString(){
        return trajectoryName;
    }

    public Trajectory toTrajectory(RobotPropertyInfo robotPropertyInfo){
        TrajectoryBuilder builder = robotPropertyInfo.constructTrajectoryBuilder(getStartPose(), startTangent);
        for (EndPoseInfo segment : endPoses) {segment.toSegment(builder, robotPropertyInfo);}
        if (!endPoses.isEmpty() && isValid) {
            return builder.build();
        } else {
            return null;
        }
    }
}
