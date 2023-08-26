package com.icaras84.rrcodegenerator.core.utils.robot;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;

import java.awt.*;

public class RobotComponent {

    private Matrix3x3 transform;
    private Vector2d[] points;
    private boolean fillComponent;
    private Color partColor;

    public RobotComponent(){
        points = new Vector2d[0];
        partColor = Color.BLACK;
    }

    public RobotComponent(Color partColor, Vector2d... points){
        this.points = points;
        this.partColor = partColor;
        this.transform = new Matrix3x3();
    }

    public void render(Matrix3x3 parentTransform){
        CanvasRenderer.setColor(partColor);
        if (fillComponent){
            CanvasRenderer.fillPolygon(points, parentTransform.times(transform));
        } else {
            CanvasRenderer.strokePolyline(points, parentTransform.times(transform), true);
        }
    }

    public Matrix3x3 getTransform() {
        return transform;
    }

    public void setTransform(Matrix3x3 transform) {
        this.transform = transform;
    }

    public Vector2d[] getPoints() {
        return points;
    }

    public void setPoints(Vector2d[] points) {
        this.points = points;
    }

    public boolean isFillComponent() {
        return fillComponent;
    }

    public void setFillComponent(boolean fillComponent) {
        this.fillComponent = fillComponent;
    }

    public Color getPartColor() {
        return partColor;
    }

    public void setPartColor(Color partColor) {
        this.partColor = partColor;
    }
}
