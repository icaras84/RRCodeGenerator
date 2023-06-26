package com.icaras84.rrcodegenerator.core.utils.maths;

import com.icaras84.rrcodegenerator.core.utils.Creatable;

public class Vector2d implements Creatable {

    public double x, y;

    public Vector2d(){
        this(0,0);
    }

    public Vector2d(double nx, double ny){
        x = nx;
        y = ny;
    }

    public Vector2d plus(Vector2d other){
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d minus(Vector2d other){
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d times(double scalar){
        return new Vector2d(x * scalar, y * scalar);
    }

    public double times(Vector2d other){
        return x * other.x + y * other.y;
    }

    public Vector2d div(double scalar){
        return new Vector2d(x / scalar, y / scalar);
    }

    public double len(){
        return Math.sqrt(x * x + y * y);
    }

    public Vector2d normalized(){
        double len = len();
        return new Vector2d(x / len, y / len);
    }

    public void normalize(){
        double len = len();
        x /= len;
        y /= len;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }


    @Override
    public String getObjCreation(Object... args) {
        return "new Vector2d(" + x + ", " + y + ")";
    }
}
