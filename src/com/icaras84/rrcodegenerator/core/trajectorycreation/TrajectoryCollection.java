package com.icaras84.rrcodegenerator.core.trajectorycreation;

import com.icaras84.rrcodegenerator.core.renderer.ui.CodeGenTrajectoryPanel;
import com.icaras84.rrcodegenerator.core.roadrunnerqscore.RobotProperties;

import java.util.HashMap;

public class TrajectoryCollection {

    public static RobotProperties robotProperties;
    private static HashMap<String, CodeGenTrajectoryPanel> registeredTrajectories;

    static {
        robotProperties = new RobotProperties();
        registeredTrajectories = new HashMap<>();
    }

    public static boolean isNameRegistered(String trajName){
        return registeredTrajectories.containsKey(trajName);
    }

    public static boolean isNameAvailable(String trajName){
        return !isNameRegistered(trajName);
    }

    public static void registerTrajectory(String trajName, CodeGenTrajectoryPanel trajectoryPanel){
        registeredTrajectories.put(trajName, trajectoryPanel);
    }

    public static CodeGenTrajectoryPanel genTrajectory(String trajName){
        return isNameRegistered(trajName) ? registeredTrajectories.get(trajName) : null;
    }
}
