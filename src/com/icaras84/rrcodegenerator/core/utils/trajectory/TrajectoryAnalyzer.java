package com.icaras84.rrcodegenerator.core.utils.trajectory;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.icaras84.rrcodegenerator.core.utils.info.EndPoseInfo;
import com.icaras84.rrcodegenerator.core.utils.info.TrajectoryInfo;

import java.util.Vector;

public class TrajectoryAnalyzer {
    private static class TrajSegment{
        private EndPoseInfo previousPose, middlePose, nextPose;

        public TrajSegment(EndPoseInfo previousPose, EndPoseInfo middlePose, EndPoseInfo nextPose) {
            this.previousPose = previousPose;
            this.middlePose = middlePose;
            this.nextPose = nextPose;
        }

        public TrajSegment(TrajSegment toCopy){
            this.previousPose = new EndPoseInfo(toCopy.previousPose);
            this.middlePose = new EndPoseInfo(toCopy.middlePose);
            this.nextPose = new EndPoseInfo(toCopy.nextPose);
        }

        public boolean isTrajectoryBeginning(){
            return previousPose == null;
        }

        public boolean isTrajectoryEnding() {
            return nextPose == null;
        }

        public EndPoseInfo.TRAJECTORY_SEGMENT_TYPE getFirstSegmentType(){
            return middlePose.getPathType();
        }

        public EndPoseInfo.TRAJECTORY_SEGMENT_TYPE getSecondSegmentType(){
            return nextPose.getPathType();
        }

        /*
        Key:
        LL -> line to line (continuity issue)
        LS -> line to spline
        SS -> spline to spline
        SL -> spline to line
         */
        public void fix(){
            replaceAndUpdateConstantHeadings();

            //skip over fixing poses that are at the head or tail of the path
            if (!(isTrajectoryBeginning() || isTrajectoryEnding())){

                //check for LL case
                if (!(pathTypeIsSpline(getFirstSegmentType()) || pathTypeIsSpline(getSecondSegmentType()))){
                    //try to preserve linear aspect as much as possible through tangent manipulation (albeit quite simple)
                    Vector2d segment1Diff = middlePose.getEndPose().vec().minus(previousPose.getEndPose().vec());
                    Vector2d segment2Diff = nextPose.getEndPose().vec().minus(middlePose.getEndPose().vec());
                    previousPose.setSplineTangent(segment1Diff.angle());
                    middlePose.setSplineTangent(segment2Diff.angle());

                    /*
                    check if heading needs to interpolate to the next one
                    if not, mimic lineTo/lineToConstantHeading behavior
                    Note: splineTo has the heading follow the curve of the path
                    */
                    if (pathTypeHeadingChanges(getFirstSegmentType())){
                        middlePose.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.splineToSplineHeading);
                    } else {
                        middlePose.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.splineToConstantHeading);
                    }
                }
            }
        }

        private void replaceAndUpdateConstantHeadings(){
            if (!isTrajectoryBeginning() && !pathTypeHeadingChanges(getFirstSegmentType())){
                Vector2d position = middlePose.getEndPose().vec();
                middlePose.setEndPose(new Pose2d(position, previousPose.getHeading()));
                if (getFirstSegmentType() == EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.lineTo)
                    middlePose.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.lineToConstantHeading);
            }

            if (!isTrajectoryEnding() && !pathTypeHeadingChanges(getSecondSegmentType())){
                Vector2d position = nextPose.getEndPose().vec();
                nextPose.setEndPose(new Pose2d(position, middlePose.getHeading()));
                if (getSecondSegmentType() == EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.lineTo)
                    nextPose.setPathType(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE.lineToConstantHeading);
            }
        }

        public EndPoseInfo getPoseInfo(){
            return middlePose;
        }
    }

    public static TrajectoryInfo fix(TrajectoryInfo in){
        //there is nothing to fix in a trajectory that has one or fewer segments
        if (in.getEndPoseBufferLength() >= 1) return new TrajectoryInfo(in);

        Vector<TrajSegment> segments = new Vector<>();
        Vector<EndPoseInfo> poses = new Vector<>();
        poses.add(new EndPoseInfo(in));
        poses.addAll(in.getEndPoses());

        EndPoseInfo prev, curr, next;

        int poseCount = poses.size();
        for (int i = 0; i < poseCount; i++) {
            prev = i - 1 >= 0 ? poses.get(i - 1) : null;
            curr = in.getEndPose(i);
            next = i + 1 < poseCount ? poses.get(i + 1) : null;
            segments.add(new TrajSegment(prev, curr, next));
        }
        segments.forEach(TrajSegment::fix);

        TrajectoryInfo output = new TrajectoryInfo();
        EndPoseInfo trajectoryStart = segments.get(0).getPoseInfo();
        output.setStartPose(trajectoryStart.getEndPose());
        output.setStartTangent(trajectoryStart.getSplineTangent());

        for (int i = 1; i < segments.size(); i++) {
            output.add(segments.get(i).getPoseInfo());
        }

        return output;
    }

    public static boolean pathTypeIsSpline(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE trajectoryType){
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

    public static boolean pathTypeHeadingChanges(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE trajectoryType){
        switch (trajectoryType){
            case lineToLinearHeading:
            case lineToSplineHeading:
            case splineTo:
            case splineToLinearHeading:
            case splineToSplineHeading:
                return true;
            default:
                return false;
        }
    }
}
