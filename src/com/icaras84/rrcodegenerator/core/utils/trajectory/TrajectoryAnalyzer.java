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

        public boolean isTrajectoryEnding(){
            return nextPose == null;
        }
    }

    private static boolean pathTypeIsSpline(EndPoseInfo.TRAJECTORY_SEGMENT_TYPE trajectoryType){
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
