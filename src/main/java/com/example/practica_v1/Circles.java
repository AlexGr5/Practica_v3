package com.example.practica_v1;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Circles {

    private List<Circle> ListCircles = new ArrayList<Circle>();

    public boolean FindCircles(Mat ImgGray, RecognitionParameters parameters)
    {
        boolean Res = false;

        Mat workImg = ImgGray;

        if (workImg.empty())
            return false;

        Mat circles = new Mat();

        Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT,parameters.getDp(), workImg.rows() / parameters.getDenominatorOfMinDist(),
                parameters.getParam1(), parameters.getParam2(), parameters.getMinRadius(), parameters.getMaxRadius());


        for (int i = 0, r = circles.rows(); i < r; i++) {
            for (int j = 0, c = circles.cols(); j < c; j++) {
                double[] circle = circles.get(i, j);

                ListCircles.add(new Circle (circle[0], circle[1], circle[2]));

            }
        }

        if (ListCircles.size() > 0)
            Res = true;

        return Res;
    }

    public boolean DrawCircles(Mat photo)
    {
        boolean Res = false;

        Mat workImg = photo;

        if (workImg.empty())
            return false;

        Scalar purple = new Scalar(255, 0, 0);

        if (ListCircles.size() > 0) {
            for (int i = 0; i < ListCircles.size(); i++) {

                Circle temp = new Circle(ListCircles.get(i));

                double[] rect = {temp.getX() - temp.getRadius(), temp.getY() - temp.getRadius(), temp.getX() + temp.getRadius(), temp.getY() + temp.getRadius()};

                Imgproc.rectangle(workImg, new Point(rect[0], rect[1]),
                        new Point(rect[2], rect[3]), purple, 20);
            }
            Res = true;
        }

        return Res;
    }

    public boolean FindAndDrawCircles(Mat WightBlackImg, Mat DrawImg, RecognitionParameters parameters)
    {
        boolean Res = false;

        if (WightBlackImg.empty() || DrawImg.empty())
            return false;

        if (FindCircles(WightBlackImg, parameters))
        {
            if (DrawCircles(DrawImg))
            {
                Res = true;
            }
        }

        return Res;
    }

}
