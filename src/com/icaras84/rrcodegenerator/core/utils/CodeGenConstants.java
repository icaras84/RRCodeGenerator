package com.icaras84.rrcodegenerator.core.utils;

public class CodeGenConstants {

    public static String driveReference = "drive";
    public static String trajBuilderMethodRef = "trajectorySequenceBuilder(~)";

    public static String velocityConstraintConstructor = "MecanumVelocityConstraint(~, DriveConstants.TRACK_WIDTH)";
    public static String accelConstraintConstructor = "ProfileAccelerationConstraint(~)";
}
