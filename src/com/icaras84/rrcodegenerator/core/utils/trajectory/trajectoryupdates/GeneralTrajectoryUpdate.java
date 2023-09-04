package com.icaras84.rrcodegenerator.core.utils.trajectory.trajectoryupdates;

import com.icaras84.rrcodegenerator.core.CoreUpdate;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.nav.logic.NavigationPanelLogic;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.utils.robot.RobotPropertyInfo;

import java.awt.*;

public class GeneralTrajectoryUpdate implements CoreUpdate {
    RobotPropertyInfo defaultProps;

    @Override
    public void init() {
        defaultProps = new RobotPropertyInfo();
    }

    @Override
    public void lateInit() {

    }

    @Override
    public void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec) {

    }

    @Override
    public void render(Graphics2D g, float fixedDeltaTimeMs, float fixedDeltaTimeSec) {
        CanvasRenderer.drawTrajectory(NavigationPanelLogic.getCurrentTrajectoryItems().validBuild.toTrajectory(defaultProps));
    }
}
