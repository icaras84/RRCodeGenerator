package com.icaras84.rrcodegenerator.core.utils.info;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.icaras84.rrcodegenerator.core.trajectorycreation.TrajectoryOperation;

import java.util.Stack;
import java.util.Vector;
import java.util.function.Predicate;

public class TrajectoryAnalyzer {
    private static class Analysis{
        private TrajectoryInfo trajectoryInfo;
        private Stack<Integer> problemIndices;
        private boolean solved;

        public Analysis(TrajectoryInfo info, Stack<Integer> problems) {
            this.trajectoryInfo = info;
            this.problemIndices = problems;
        }

        public TrajectoryInfo getTrajectoryInfo() {
            return trajectoryInfo;
        }

        public void setTrajectoryInfo(TrajectoryInfo trajectoryInfo) {
            this.trajectoryInfo = trajectoryInfo;
        }

        public Stack<Integer> getProblemIndices() {
            return problemIndices;
        }

        public void setProblemIndices(Stack<Integer> problemIndices) {
            this.problemIndices = problemIndices;
        }

        public boolean isSolved() {
            return solved;
        }

        public void setSolved(boolean solved) {
            this.solved = solved;
        }

        public boolean hasTrajectory(TrajectoryInfo info){
            return trajectoryInfo == info || trajectoryInfo.equals(info);
        }
    }

    private TrajectoryAnalyzer(){}

    private static Vector<Analysis> analyzedTrajectories = new Vector<>();

    public synchronized static void analyze(TrajectoryInfo trajectory){
        Stack<Integer> problems = new Stack<>();
        int endPoseCount = trajectory.getEndPoseBufferLength();

        EndPoseInfo current, next;
        for (int i = 0; i < endPoseCount - 1; i++) {
            current = trajectory.getEndPose(i);
            next = trajectory.getEndPose(i + 1);

            if (isContinuityIssue(current, next)) problems.push(i);
        }

        analyzedTrajectories.add(new Analysis(trajectory, problems));
    }

    public synchronized static Analysis search(TrajectoryInfo info){
        return (Analysis) analyzedTrajectories.stream().filter((analysis -> analysis.hasTrajectory(info))).toArray()[0];
    }

    public static boolean isContinuityIssue(EndPoseInfo currentPose, EndPoseInfo nextPose){
        Pose2d currentPoseTangent = new Pose2d(
                currentPose.getX(),
                currentPose.getY(),
                pathTypeIsSpline(currentPose.getPathType()) ?
                        currentPose.getSplineTangent() : currentPose.getHeading());

        Vector2d nextPoseDir = new Vector2d(
                nextPose.getX() - currentPose.getX(),
                nextPose.getY() - currentPose.getY())
                .div(currentPoseTangent.vec().distTo(nextPose.getEndPose().vec()));

        float tangentialDirection = (float) nextPoseDir.dot(currentPoseTangent.headingVec());

        return tangentialDirection != 1f;
    }

    private static boolean pathTypeIsSpline(TrajectoryOperation.TRAJECTORY_SEGMENT_TYPE trajectoryType){
        switch (trajectoryType){
            case splineTo:
            case splineToConstantHeading:
            case splineToLinearHeading:
            case splineToSplineHeading:
                return true;
            default:
                return false;
        }
    }
}
