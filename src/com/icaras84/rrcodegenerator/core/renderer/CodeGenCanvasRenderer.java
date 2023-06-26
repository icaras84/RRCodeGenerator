package com.icaras84.rrcodegenerator.core.renderer;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.icaras84.rrcodegenerator.core.renderer.ui.CodeGenCanvasPanel;
import com.icaras84.rrcodegenerator.core.trajectorycreation.TrajectoryOperation;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;

import java.awt.*;

public class CodeGenCanvasRenderer {

    public static double DEFAULT_PATH_RESOLUTION = 2.0; // distance units ; presumed inches

    public static double FIELD_WIDTH =  141; //internal field width in inches
    public static double FIELD_LENGTH = 141; //internal field length in inches
    public static double TILE_WIDTH = FIELD_WIDTH / 6d;
    public static double TILE_LENGTH = FIELD_LENGTH / 6d;
    public static double ROBOT_RADIUS = 9;

    private static Matrix3x3 viewTransform;
    private static double canvasScaleX, canvasScaleY;

    public static void init(){
        viewTransform = new Matrix3x3();
        updateViewMatrix();
    }

    public static void setViewTransform(Matrix3x3 nViewTransform){
        viewTransform = nViewTransform;
    }

    public static Matrix3x3 getViewTransform(){
        return viewTransform;
    }

    public static void updateViewMatrix(){
        double canvasWidth = CodeGenCanvasPanel.getCanvasWidth();
        double canvasHeight = CodeGenCanvasPanel.getCanvasHeight();

        viewTransform.m[0][2] = canvasWidth / 2d;
        viewTransform.m[1][2] = canvasHeight / 2d;

        canvasScaleX = canvasWidth / FIELD_WIDTH;
        canvasScaleY = canvasHeight / FIELD_LENGTH;
        //map x and y to NDC / 2, then to canvas coordinates, then rotate by 90 deg CCW
        viewTransform.m[0][1] = -canvasScaleY;
        viewTransform.m[1][0] = canvasScaleX;
    }

    public static double improvisedMatMul(Matrix3x3 matrix3x3, int row, double x, double y){
        double[] matRow = matrix3x3.m[row];
        return x * matRow[0] + y * matRow[1] + matRow[2];
    }



    public static void clear(Graphics2D g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, CodeGenCanvasPanel.getCanvasWidth(), CodeGenCanvasPanel.getCanvasHeight());
    }

    public static void drawSampledPath(Graphics2D g, Path path, double resolution){
        int samples = (int) Math.ceil(path.length() / resolution);
        double[] xPoints = new double[samples];
        double[] yPoints = new double[samples];
        double dx = path.length() / (samples - 1);
        for (int i = 0; i < samples; i++) {
            double displacement = i * dx;
            Pose2d pose = path.get(displacement);
            xPoints[i] = improvisedMatMul(viewTransform, 0, pose.getX(), pose.getY());
            yPoints[i] = improvisedMatMul(viewTransform, 1, pose.getX(), pose.getY());
        }
        strokePolyline(g, xPoints, yPoints);
    }

    public static void drawSampledPath(Graphics2D g, Path path){
        drawSampledPath(g, path, DEFAULT_PATH_RESOLUTION);
    }

    public static void strokePolyline(Graphics2D g, double[] x, double[] y){
        int minimumPoints = Math.min(x.length, y.length);
        int[] canvasX = new int[minimumPoints];
        int[] canvasY = new int[minimumPoints];

        for (int i = 0; i < minimumPoints; i++) {
            canvasX[i] = (int) x[i];
            canvasY[i] = (int) y[i];
        }

        g.drawPolyline(canvasX, canvasY, minimumPoints);
    }

    public static void drawLine(Graphics2D g, double x0, double y0, double x1, double y1){
        int transformedX0 = (int) (improvisedMatMul(viewTransform, 0, x0, y0));
        int transformedX1 = (int) (improvisedMatMul(viewTransform, 0, x1, y1));

        int transformedY0 = (int) (improvisedMatMul(viewTransform, 1, x0, y0));
        int transformedY1 = (int) (improvisedMatMul(viewTransform, 1, x1, y1));

        g.drawLine(transformedX0, transformedY0, transformedX1, transformedY1);
    }

    public static void drawPose(Graphics2D g, Pose2d pose2d){
        //draw axes
        drawCircle(g, pose2d.vec(), ROBOT_RADIUS);
        Vector2d scaledHeading = pose2d.headingVec().times(ROBOT_RADIUS);
        Vector2d scaledTangent = new Vector2d(scaledHeading.getY(), -scaledHeading.getX());
        drawLine(g,
                pose2d.getX(),
                pose2d.getY(),
                pose2d.getX() + scaledHeading.getX(),
                pose2d.getY() + scaledHeading.getY()
        );

        drawLine(g,
                pose2d.getX(),
                pose2d.getY(),
                pose2d.getX() + scaledTangent.getX(),
                pose2d.getY() + scaledTangent.getY()
        );
    }

    public static void drawCircle(Graphics2D g, Vector2d pos, double radius){
        double centeredX = pos.getX() - radius;
        double centeredY = pos.getY() - radius;
        double diameter = radius * 2 * canvasScaleX;

        g.drawOval(
                (int) improvisedMatMul(viewTransform, 0, centeredX, centeredY),
                (int) improvisedMatMul(viewTransform, 1, centeredX, centeredY),
                (int) diameter,
                (int) diameter
        );
    }

    public static void drawTrajectory(Graphics2D g, Trajectory op){
        drawSampledPath(g, op.getPath());
    }
}
