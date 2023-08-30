package com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.icaras84.rrcodegenerator.core.ui.singleinstance.output.OutputPanel;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;

import java.awt.*;

public class CanvasRenderer {

    public static double DEFAULT_PATH_RESOLUTION = 2.0; // distance units ; presumed inches

    public static double FIELD_WIDTH =  141; //internal field width in inches
    public static double FIELD_LENGTH = 141; //internal field length in inches
    public static double TILE_WIDTH = FIELD_WIDTH / 6d; //internal field is 6x6 tile side lengths
    public static double TILE_LENGTH = FIELD_LENGTH / 6d;
    public static double FIELD_HALF = FIELD_WIDTH / 2d;
    public static double ROBOT_RADIUS = 9;
    public static boolean rotatedField = false;

    public static Vector2d CANVAS_FIELD_TL, CANVAS_FIELD_BR;

    private static Matrix3x3 viewTransform;
    private static double scalingFactor;

    private static Graphics2D g;
    public static void init(){
        OutputPanel.createBuffers();
        viewTransform = new Matrix3x3();
    }

    public static void updateViewMatrix(Graphics2D g){
        CanvasRenderer.g = g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        double canvasCenterX = OutputPanel.canvasWidth / 2d;
        double canvasCenterY = OutputPanel.canvasHeight / 2d;
        double smallerSide = Math.min(canvasCenterX, canvasCenterY);
        scalingFactor = smallerSide / FIELD_HALF;

        viewTransform.m[0][2] = canvasCenterX;
        viewTransform.m[1][2] = canvasCenterY;

        if (rotatedField){
            viewTransform.m[0][0] = 0;
            viewTransform.m[0][1] = -scalingFactor;
            viewTransform.m[1][0] = -scalingFactor;
            viewTransform.m[1][1] = 0;

            CANVAS_FIELD_TL = viewTransform.times(FIELD_HALF, FIELD_HALF);
            CANVAS_FIELD_BR = viewTransform.times(-FIELD_HALF, -FIELD_HALF);
        } else {
            viewTransform.m[0][0] = scalingFactor;
            viewTransform.m[0][1] = 0;
            viewTransform.m[1][0] = 0;
            viewTransform.m[1][1] = -scalingFactor;

            CANVAS_FIELD_TL = viewTransform.times(-FIELD_HALF, FIELD_HALF);
            CANVAS_FIELD_BR = viewTransform.times(FIELD_HALF, -FIELD_HALF);
        }
    }

    public static void clear(){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, OutputPanel.canvasWidth, OutputPanel.canvasHeight);

        g.setColor(Color.WHITE);
        g.fillRect(
                (int) CANVAS_FIELD_TL.getX(), (int) CANVAS_FIELD_TL.getY(),
                (int) (CANVAS_FIELD_BR.getX() - CANVAS_FIELD_TL.getX()),
                (int) (CANVAS_FIELD_BR.getY() - CANVAS_FIELD_TL.getY())
        );
    }

    public static void markCenter(){
        double canvasCenterX = OutputPanel.canvasWidth / 2d;
        double canvasCenterY = OutputPanel.canvasHeight / 2d;
        g.setColor(Color.WHITE);
        g.drawLine((int) (canvasCenterX - 30), (int) canvasCenterY, (int) (canvasCenterX + 30), (int) canvasCenterY);
        g.drawLine((int) canvasCenterX, (int) (canvasCenterY - 30), (int) canvasCenterX, (int) (canvasCenterY + 30));
    }

    public static void drawSampledPath(Path path, double resolution){
        int samples = (int) Math.ceil(path.length() / resolution);
        double[] xPoints = new double[samples];
        double[] yPoints = new double[samples];
        double dx = path.length() / (samples - 1);
        for (int i = 0; i < samples; i++) {
            double displacement = i * dx;
            Pose2d pose = path.get(displacement);
            xPoints[i] = pose.getX();
            yPoints[i] = pose.getY();
        }
        strokePolyline(xPoints, yPoints);
    }

    public static void drawSampledPath(Path path){
        drawSampledPath(path, DEFAULT_PATH_RESOLUTION);
    }

    public static void strokePolyline(double[] x, double[] y){
        int minimumPoints = Math.min(x.length, y.length);
        int[] canvasX = new int[minimumPoints];
        int[] canvasY = new int[minimumPoints];

        for (int i = 0; i < minimumPoints; i++) {
            Vector2d output = viewTransform.times(x[i], y[i]);
            canvasX[i] = (int) output.getX();
            canvasY[i] = (int) output.getY();
        }

        g.drawPolyline(canvasX, canvasY, minimumPoints);
    }

    public static void strokePolyline(Vector2d[] points){
        strokePolyline(points, Matrix3x3.IDENTITY, false);
    }

    public static void strokePolyline(Vector2d[] points, Matrix3x3 additionalTransform, boolean fullPoly){
        int[] canvasX = new int[points.length + (fullPoly ? 1 : 0)];
        int[] canvasY = new int[points.length + (fullPoly ? 1 : 0)];

        for (int i = 0; i < points.length; i++) {
            Vector2d output = viewTransform.times(additionalTransform.times(points[i]));
            canvasX[i] = (int) output.getX();
            canvasY[i] = (int) output.getY();
        }

        if (fullPoly){
            canvasX[canvasX.length - 1] = canvasX[0];
            canvasY[canvasY.length - 1] = canvasY[0];
        }

        g.drawPolyline(canvasX, canvasY, canvasX.length);
    }

    public static void fillPolygon(Vector2d[] points, Matrix3x3 additionalTransform){
        int[] canvasX = new int[points.length];
        int[] canvasY = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            Vector2d output = viewTransform.times(additionalTransform.times(points[i]));
            canvasX[i] = (int) output.getX();
            canvasY[i] = (int) output.getY();
        }
        g.fillPolygon(new Polygon(canvasX, canvasY, points.length));
    }

    public static void drawLine(double x0, double y0, double x1, double y1){
        Vector2d end1 = viewTransform.times(x0, y0);
        Vector2d end2 = viewTransform.times(x1, y1);
        g.drawLine((int) end1.getX(), (int) end1.getY(), (int) end2.getX(), (int) end2.getY());
    }

    public static void drawPose(Pose2d pose2d){
        //draw radius
        g.setColor(Color.ORANGE);
        g.setStroke(new BasicStroke(2.5f));
        drawCircle(pose2d.vec(), ROBOT_RADIUS);

        Vector2d scaledHeading = pose2d.headingVec().times(ROBOT_RADIUS);
        Vector2d scaledTangent = new Vector2d(scaledHeading.getY(), -scaledHeading.getX());
        //draw axes
        g.setColor(Color.RED);
        drawLine(
                pose2d.getX(),
                pose2d.getY(),
                pose2d.getX() + scaledHeading.getX(),
                pose2d.getY() + scaledHeading.getY()
        );

        g.setColor(Color.BLUE);
        drawLine(
                pose2d.getX(),
                pose2d.getY(),
                pose2d.getX() + scaledTangent.getX(),
                pose2d.getY() + scaledTangent.getY()
        );
    }

    public static void drawCircle(Vector2d pos, double radius){
        Vector2d o = viewTransform.times(pos);
        radius *= scalingFactor;
        double centeredX = o.getX() - radius;
        double centeredY = o.getY() - radius;
        double diameter = radius * 2;

        g.drawOval(
                (int) centeredX,
                (int) centeredY,
                (int) diameter,
                (int) diameter
        );
    }

    public static void fillCircle(Vector2d pos, double radius){
        Vector2d o = viewTransform.times(pos);
        radius *= scalingFactor;
        double centeredX = o.getX() - radius;
        double centeredY = o.getY() - radius;
        double diameter = radius * 2;

        g.fillOval(
                (int) centeredX,
                (int) centeredY,
                (int) diameter,
                (int) diameter
        );
    }

    public static void drawTrajectory(Trajectory op){
        drawSampledPath(op.getPath());
    }

    public static void setColor(Color strokeColor){
        g.setColor(strokeColor);
    }

    public static void setPenStroke(Stroke penStroke){
        g.setStroke(penStroke);
    }
}
