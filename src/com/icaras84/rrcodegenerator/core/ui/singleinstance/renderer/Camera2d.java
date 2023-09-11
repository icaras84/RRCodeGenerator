package com.icaras84.rrcodegenerator.core.ui.singleinstance.renderer;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.icaras84.rrcodegenerator.core.utils.maths.Matrix3x3;

public class Camera2d {

    private static final double MIN_ZOOM = 0.0001;

    private Matrix3x3 cameraMatrix;
    private double zoom;
    private double tx, ty;


    public Camera2d(){
        cameraMatrix = new Matrix3x3();
        zoom = 1;
        tx = 0;
        ty = 0;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = Math.max(zoom, MIN_ZOOM);
    }

    public void incrementZoom(double inc){
        this.zoom += inc;
        this.zoom = Math.max(this.zoom, MIN_ZOOM);
    }

    public void decrementZoom(double dec){
        this.zoom -= dec;
        this.zoom = Math.max(this.zoom, MIN_ZOOM);
    }

    public Vector2d getTranslation() {
        return new Vector2d(tx, ty);
    }

    public double getTranslationX(){
        return tx;
    }

    public double getTranslationY(){
        return ty;
    }

    public void setTranslation(Vector2d translation) {
        this.tx = translation.getX();
        this.ty = translation.getY();
    }

    public void setTranslation(double tx, double ty){
        this.tx = tx;
        this.ty = ty;
    }

    public void setTranslationX(double tx){
        this.tx = tx;
    }

    public void setTranslationY(double ty){
        this.ty = ty;
    }

    public void incrementX(double dx){
        tx += dx;
    }

    public void decrementX(double dx){
        tx -= dx;
    }

    public void incrementY(double dy){
        ty += dy;
    }

    public void decrementY(double dy){
        ty -= dy;
    }

    public void updateMatrix(){
        cameraMatrix.m[0][0] = zoom;
        cameraMatrix.m[0][2] = -tx;
        cameraMatrix.m[1][1] = zoom;
        cameraMatrix.m[1][2] = -ty;
    }

    public Matrix3x3 getMatrix() {
        updateMatrix();
        return cameraMatrix;
    }
}
