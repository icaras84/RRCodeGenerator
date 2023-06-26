package com.icaras84.rrcodegenerator.core.trajectorycreation;

import java.util.HashMap;

public class TrajectoryOperation {

    public enum TRAJECTORY_TYPE{
        lineTo,
        lineToConstantHeading,
        lineToLinearHeading,
        lineToSplineHeading,

        splineTo,
        splineToConstantHeading,
        splineToLinearHeading,
        splineToSplineHeading
    }

    public enum COMMON_PARAMS{
        END_POS("END_POS"),
        END_HEADING("END_HEADING"),
        END_POSE("END_POSE"),
        MAX_VEL("MAX_VEL"),
        MAX_ACCEL("MAX_ACCEL");

        private String value;

        COMMON_PARAMS(String v){
            value = v;
        }
    }

    private TRAJECTORY_TYPE trajectoryType;
    private boolean useVelAndAccelConstraints;

    private HashMap<String, String> parameterMapping;

    public TrajectoryOperation(TRAJECTORY_TYPE trajectoryType){
        this(trajectoryType, false);
    }

    public TrajectoryOperation(TRAJECTORY_TYPE trajectoryType, boolean useVelAndAccelConstraints){
        this.trajectoryType = trajectoryType;
        this.useVelAndAccelConstraints = useVelAndAccelConstraints;
        parameterMapping = new HashMap<>();
    }

    public void mapParameter(String paramName, String arg){
        parameterMapping.put(paramName, arg);
    }

    public void mapParameter(COMMON_PARAMS paramName, String arg){
        mapParameter(paramName.value, arg);
    }

    public void mapMaxVelAndAccel(String maxVel, String maxAccel){
        mapParameter(COMMON_PARAMS.MAX_VEL, maxVel);
        mapParameter(COMMON_PARAMS.MAX_ACCEL, maxAccel);
    }

    public void mapEndPose(String pose){
        mapParameter(COMMON_PARAMS.END_POSE, pose);
    }

    public void mapEndPos(String pos){
        mapParameter(COMMON_PARAMS.END_POS, pos);
    }

    public String generate(){
        return generate(1);
    }

    public String generate(int tabs){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < tabs; i++) {
            output.append('\t');
        }

        switch (trajectoryType){
            case lineTo:
                output.append("lineTo(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POS.value));
                break;
            case lineToConstantHeading:
                output.append("lineToConstantHeading(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POS.value));
                break;
            case lineToLinearHeading:
                output.append("lineToLinearHeading(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POSE.value));
                break;
            case lineToSplineHeading:
                output.append("lineToSplineHeading(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POSE.value));
                break;

            case splineTo:
                output.append("splineTo(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POS.value)).append(", ");
                output.append(parameterMapping.get(COMMON_PARAMS.END_HEADING.value));
                break;
            case splineToConstantHeading:
                output.append("splineToConstantHeading(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POS.value)).append(", ");
                output.append(parameterMapping.get(COMMON_PARAMS.END_HEADING.value));
                break;
            case splineToLinearHeading:
                output.append("splineToLinearHeading(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POSE.value)).append(", ");
                output.append(parameterMapping.get(COMMON_PARAMS.END_HEADING.value));
                break;
            case splineToSplineHeading:
                output.append("splineToSplineHeading(");
                output.append(parameterMapping.get(COMMON_PARAMS.END_POSE.value)).append(", ");
                output.append(parameterMapping.get(COMMON_PARAMS.END_HEADING.value));
                break;
        }

        if (useVelAndAccelConstraints){
            output.append(", ").append(parameterMapping.get(COMMON_PARAMS.MAX_VEL.value)).append(", ");
            output.append(parameterMapping.get(COMMON_PARAMS.MAX_ACCEL.value));
        }

        return output.append(")\n").toString();
    }

    public TRAJECTORY_TYPE getTrajectoryType() {
        return trajectoryType;
    }

    public void setTrajectoryType(TRAJECTORY_TYPE trajectoryType) {
        this.trajectoryType = trajectoryType;
    }

    public boolean isUsingVelAndAccelConstraints() {
        return useVelAndAccelConstraints;
    }

    public void setUseVelAndAccelConstraints(boolean useVelAndAccelConstraints) {
        this.useVelAndAccelConstraints = useVelAndAccelConstraints;
    }

    public HashMap<String, String> getParameterMapping() {
        return parameterMapping;
    }

    public void setParameterMapping(HashMap<String, String> parameterMapping) {
        this.parameterMapping = parameterMapping;
    }
}
