package com.icaras84.rrcodegenerator.core.utils.info;

public class RendererInfo {
    private double defaultPathResolution = 2.0; // distance units ; presumed inches
    private double fieldWidth =  141; //internal field width in inches
    private double fieldLength = 141; //internal field length in inches
    private double tileWidth = fieldWidth / 6d; //internal field is 6x6 tile side lengths
    private double tileLength = fieldLength / 6d;
    private double fieldHalf = fieldWidth / 2d;
    private boolean rotatedField = false;
    public RendererInfo(){}

    public double getDefaultPathResolution() {
        return defaultPathResolution;
    }

    public void setDefaultPathResolution(double defaultPathResolution) {
        this.defaultPathResolution = defaultPathResolution;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(double fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public double getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(double fieldLength) {
        this.fieldLength = fieldLength;
    }

    public double getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(double tileWidth) {
        this.tileWidth = tileWidth;
    }

    public double getTileLength() {
        return tileLength;
    }

    public void setTileLength(double tileLength) {
        this.tileLength = tileLength;
    }

    public double getFieldHalf() {
        return fieldHalf;
    }

    public void setFieldHalf(double fieldHalf) {
        this.fieldHalf = fieldHalf;
    }

    public boolean isRotatedField() {
        return rotatedField;
    }

    public void setRotatedField(boolean rotatedField) {
        this.rotatedField = rotatedField;
    }
}
