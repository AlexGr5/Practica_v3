package com.example.practica_v1;

public class ColorParams {

    private boolean ISAutomaticColor = true;

    private double red = 255;
    private double green = 0;
    private double blue = 0;

    private int Delta = 50;

    public ColorParams() {;}

    public ColorParams(boolean IsSelected, double red, double green, double blue, int Delta)
    {
        setISAutomaticColor(IsSelected);
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.setDelta(Delta);
    }

    public void SetColorParams(boolean IsSelected, double red, double green, double blue, int Delta)
    {
        setISAutomaticColor(IsSelected);
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.setDelta(Delta);
    }

    public boolean isISAutomaticColor() {
        return ISAutomaticColor;
    }

    public void setISAutomaticColor(boolean ISAutomaticColor) {
        this.ISAutomaticColor = ISAutomaticColor;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public int getDelta() {
        return Delta;
    }

    public void setDelta(int delta) {
        Delta = delta;
    }
}
