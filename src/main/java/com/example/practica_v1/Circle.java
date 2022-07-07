package com.example.practica_v1;

public class Circle {

    private double X;

    private double Y;

    private double Radius;

    public Circle()
    {

    }

    public Circle(Circle circle)
    {
        X = circle.getX();
        Y = circle.getY();
        Radius = circle.getRadius();
    }

    public Circle(double x, double y, double radius)
    {
        X = x;
        Y = y;
        Radius = radius;
    }

    public void SetCircle(double x, double y, double radius)
    {
        X = x;
        Y = y;
        Radius = radius;
    }


    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getRadius() {
        return Radius;
    }

    public void setRadius(double radius) {
        Radius = radius;
    }
}
