package com.icaras84.rrcodegenerator.core.utils.maths;

import com.icaras84.rrcodegenerator.core.utils.Creatable;

public class Vector3d implements Creatable {

    public double x, y, z;

    public Vector3d(){
        this(0,0, 0);
    }

    public Vector3d(Vector2d v, double z){
        this(v.x, v.y, z);
    }

    public Vector3d(double nx, double ny, double nz){
        x = nx;
        y = ny;
        z = nz;
    }

    public Vector3d plus(Vector3d other){
        return new Vector3d(x + other.x, y + other.y, z + other.z);
    }

    public Vector3d minus(Vector3d other){
        return new Vector3d(x - other.x, y - other.y, z - other.z);
    }

    public Vector3d times(double scalar){
        return new Vector3d(x * scalar, y * scalar, z * scalar);
    }

    public double times(Vector3d other){
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3d div(double scalar){
        return new Vector3d(x / scalar, y / scalar, z / scalar);
    }

    public double len(){
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3d normalized(){
        double len = len();
        return new Vector3d(x / len, y / len, z /len);
    }

    public void normalize(){
        double len = len();
        x /= len;
        y /= len;
        z /= len;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }


    @Override
    public String getObjCreation(Object... args) {
        return "new Vector3d(" + x + ", " + y + ", " + z + ")";
    }
}
