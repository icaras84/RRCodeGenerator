package com.icaras84.rrcodegenerator.core.utils.maths;

public class Matrix3x3 {

    public double[][] m;

    public Matrix3x3(){
        m = new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
    }

    public Matrix3x3(double[][] mat){
        m = mat;
    }

    public Matrix3x3(Matrix3x3 matrix3x3){
        m = new double[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                m[y][x] = matrix3x3.m[y][x];
            }
        }
    }

    public Matrix3x3 plus(Matrix3x3 matrix){
        double[][] output = new double[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                output[y][x] = m[y][x] + matrix.m[y][x];
            }
        }
        return new Matrix3x3(output);
    }

    public Matrix3x3 minus(Matrix3x3 matrix){
        double[][] output = new double[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                output[y][x] = m[y][x] + matrix.m[y][x];
            }
        }
        return new Matrix3x3(output);
    }

    //who said we'd have to respect math rules
    public Vector2d times(Vector2d inputVector){
        return new Vector2d(
                m[0][0] * inputVector.x + m[0][1] * inputVector.y + m[0][2],
                m[1][0] * inputVector.x + m[1][1] * inputVector.y + m[1][2]
        );
    }

    public com.acmerobotics.roadrunner.geometry.Vector2d times(double x, double y){
        return new com.acmerobotics.roadrunner.geometry.Vector2d(
                m[0][0] * x + m[0][1] * y + m[0][2],
                m[1][0] * x + m[1][1] * y + m[1][2]
        );
    }
    public com.acmerobotics.roadrunner.geometry.Vector2d times(com.acmerobotics.roadrunner.geometry.Vector2d inputVector){
        return new com.acmerobotics.roadrunner.geometry.Vector2d(
                m[0][0] * inputVector.getX() + m[0][1] * inputVector.getY() + m[0][2],
                m[1][0] * inputVector.getX() + m[1][1] * inputVector.getY() + m[1][2]
        );
    }

    public Vector3d times(Vector3d inputVector){
        return new Vector3d(
                m[0][0] * inputVector.x + m[0][1] * inputVector.y + m[0][2] * inputVector.z,
                m[1][0] * inputVector.x + m[1][1] * inputVector.y + m[1][2] * inputVector.z,
                m[2][0] * inputVector.x + m[2][1] * inputVector.y + m[2][2] * inputVector.z
        );
    }

    public Matrix3x3 times(Matrix3x3 matrix){
        double[][] nMat = new double[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                nMat[col][row] =    m[col][0] * matrix.m[0][row]
                                  + m[col][1] * matrix.m[1][row]
                                  + m[col][2] * matrix.m[2][row];
            }
        }
        return new Matrix3x3(nMat);
    }

    public Matrix3x3 times(double scalar){
        double[][] nMat = new double[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                nMat[col][row] = m[col][row] * scalar;
            }
        }
        return new Matrix3x3(nMat);
    }

    public Matrix3x3 div(double scalar){
        double[][] output = new double[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                output[y][x] = m[y][x] / scalar;
            }
        }
        return new Matrix3x3(output);
    }

    public double det(){
        return  m[0][0] * subDet(subMatrix(0, 0)) -
                m[0][1] * subDet(subMatrix(0, 1)) +
                m[0][2] * subDet(subMatrix(0, 2));
    }

    private double subDet(double[] m){
        return m[0] * m[3] - m[1] * m[2];
    }

    //row must be 0-2
    //col must be 0-2
    private double[] subMatrix(int row, int col){
        double[] output = new double[4];
        int o = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if ((y != row || x != col) && o < 4){
                    output[o] = m[y][x];
                    o++;
                }
            }
        }
        return output;
    }

    public void transpose(){
        double[][] output = new double[3][3];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                output[x][y] = m[y][x];
            }
        }

        m = output;
    }

    public Matrix3x3 transposed(){
        double[][] output = new double[3][3];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                output[x][y] = m[y][x];
            }
        }

        return new Matrix3x3(output);
    }

    public void invert(){
        double det = det();
        if (det == 0) return;

        int multiplier = 1;
        double[][] output = new double[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                output[c][r] = subDet(subMatrix(r, c)) * multiplier / det;
                multiplier *= -1;
            }
            multiplier *= -1;
        }
        m = output;
    }

    public Matrix3x3 inverted(){
        double det = det();
        if (det == 0) return null;

        int multiplier = 1;
        double[][] output = new double[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                output[c][r] = subDet(subMatrix(r, c)) * multiplier / det;
                multiplier *= -1; //transposed checkerboard pattern is itself for odd side length
            }
            multiplier *= -1;
        }
        return new Matrix3x3(output);
    }

    @Override
    public String toString() {
        return m[0][0] + ", " + m[0][1] + ", " + m[0][2] + "\n"
                + m[1][0] + ", " + m[1][1] + ", " + m[1][2] + "\n"
                + m[2][0] + ", " + m[2][1] + ", " + m[2][2];
    }

    public static Matrix3x3 rotateCW(double radians){
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new Matrix3x3(
                new double[][]{
                        {cos, sin, 0},
                        {-sin, cos, 0},
                        {0, 0, 1}
                }
        );
    }

    public static Matrix3x3 rotateCCW(double radians){
        return rotateCW(-radians);
    }
}
