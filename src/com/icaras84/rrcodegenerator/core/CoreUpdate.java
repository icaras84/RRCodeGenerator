package com.icaras84.rrcodegenerator.core;

import java.awt.*;

public interface CoreUpdate {
    void fixedUpdate(float fixedDeltaTimeMs, float fixedDeltaTimeSec);
    void render(Graphics2D g, float fixedDeltaTimeMs, float fixedDeltaTimeSec);
}
