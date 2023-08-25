package com.icaras84.rrcodegenerator.core.ui.singleinstance.output.tabs.logic;

import com.icaras84.rrcodegenerator.core.utils.info.RobotPropertyInfo;
import com.icaras84.rrcodegenerator.core.utils.info.SettingsInfo;

public class SettingsLogic {

    public static SettingsInfo settingsInfo = new SettingsInfo();

    public static void changeRobotType(RobotPropertyInfo.ROBOT_TYPE nType){
        settingsInfo.setRobotRobotType(nType);
    }
}
