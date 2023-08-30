package com.icaras84.rrcodegenerator.core.utils.robot;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer.CanvasRenderer;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;

import java.awt.*;

public class RobotComponent {

    public static class BasicRectangle extends RobotComponent{
        private Vector2d topRight, topLeft, bottomRight, bottomLeft;
        public BasicRectangle(int sideLength1, int sideLength2){
            this(Color.BLACK, new Pose2d(0, 0, 0), sideLength1, sideLength2);
        }

        public BasicRectangle(Color partColor, Pose2d transform, int sideLength1, int sideLength2){
            setPartColor(partColor);
            setTransform(Matrix3x3.transform(transform));

            double halfS1 = sideLength1 / 2d;
            double halfS2 = sideLength2 / 2d;
            topRight = new Vector2d(halfS1, -halfS2);
            bottomRight = new Vector2d(-halfS1, -halfS2);
            bottomLeft = new Vector2d(-halfS1, halfS2);
            topLeft = new Vector2d(halfS1, halfS2);

            setPoints(new Vector2d[]{
                    topLeft,
                    topRight,
                    bottomRight,
                    bottomLeft
            });
        }

        public Vector2d getTopRight() {
            return topRight;
        }

        public Vector2d getTopLeft() {
            return topLeft;
        }

        public Vector2d getBottomRight() {
            return bottomRight;
        }

        public Vector2d getBottomLeft() {
            return bottomLeft;
        }
    }

    public static class BasicCircle extends RobotComponent{

        private double radius;
        private Vector2d position;
        public BasicCircle(){
            super();
            position = new Vector2d(0, 0);
        }

        public BasicCircle(Color partColor, double radius){
            super(partColor);
            this.radius = radius;
            position = new Vector2d(0, 0);
        }

        @Override
        public void render(Matrix3x3 parentTransform) {
            CanvasRenderer.setColor(getPartColor());
            if (isFillComponent()){
                CanvasRenderer.fillCircle(parentTransform.times(getTransform()).times(position), radius);
            } else {
                CanvasRenderer.drawCircle(parentTransform.times(getTransform()).times(position), radius);
            }
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }
    }

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
