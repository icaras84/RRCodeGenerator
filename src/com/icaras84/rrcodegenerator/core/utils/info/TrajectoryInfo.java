package com.icaras84.rrcodegenerator.core.utils.info;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import java.util.Vector;

public class TrajectoryInfo {

    private String trajectoryName;
    private double startX, startY, startHeading;
    private Vector<EndPoseInfo> endPoses;

    public TrajectoryInfo(){
        trajectoryName = "Empty";
        endPoses = new Vector<>();
    }

    public void add(EndPoseInfo endPoseInfo){
        endPoses.add(endPoseInfo);
    }

    public void delete(EndPoseInfo endPoseInfo){
        endPoses.remove(endPoseInfo);
    }

    public void revalidatePoses(){
        for (EndPoseInfo poseInfo: endPoses) {
            poseInfo.setMainTrajectory(this);
        }
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

    public String createCodeString(){
        StringBuilder output = new StringBuilder();

        return output.toString();
    }

    public String toString(){
        return trajectoryName;
    }
}
