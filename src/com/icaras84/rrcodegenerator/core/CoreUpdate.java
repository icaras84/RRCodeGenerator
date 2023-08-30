package com.icaras84.rrcodegenerator.core;

import java.awt.*;

public interface CoreUpdate {

    /**
     * Method executes just before the main loop is created
     */
    void init();

    /**
     * Method executes after the swing ui is created
     */
    void lateInit();

    /**
     * Method is periodically executed
     * @param fixedDeltaTimeMs
     * @param fixedDeltaTimeSec
     */
    void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec);

    /**
     * Method is executed periodically
     * @param g
     * @param fixedDeltaTimeMs
     * @param fixedDeltaTimeSec
     */
    void render(Graphics2D g, float fixedDeltaTimeMs, float fixedDeltaTimeSec);
}
