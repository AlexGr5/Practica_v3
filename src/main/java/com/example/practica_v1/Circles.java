package com.example.practica_v1;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Circles {

    //private List<double[]> ListCircles = new ArrayList<double[]>();

    private List<Circle> ListCircles = new ArrayList<Circle>();

    public boolean FindCircles(Mat ImgGray, RecognitionParameters parameters)
    {
        boolean Res = false;

        Mat workImg = ImgGray;

        if (workImg.empty())
            return false;

        Mat circles = new Mat();
        //Imgproc.HoughCircles(imgWhiteBlack, circles, Imgproc.HOUGH_GRADIENT,
        //        2, imgWhiteBlack.cols() /*imgWhiteBlack.rows() / 4*/ );


        //====
        //Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT,1, workImg.rows() / 8, 100, 15, 0, 0);

        //Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT,1, workImg.rows() / 8, 100, 15, 300, 400);

        Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT,parameters.getDp(), workImg.rows() / parameters.getDenominatorOfMinDist(),
                parameters.getParam1(), parameters.getParam2(), parameters.getMinRadius(), parameters.getMaxRadius());

        //===


        //Scalar white = new Scalar(255, 255, 255);
        //Scalar purple = new Scalar(255, 0, 0);
        //double b = 0;
        //int w = 255;

        //Mat result = new Mat(workImg.size(), CvType.CV_8UC3, white);


        //===
        //System.out.println("Arrays circles");
        //System.out.println(circles.dump());
        //===

        for (int i = 0, r = circles.rows(); i < r; i++) {
            for (int j = 0, c = circles.cols(); j < c; j++) {
                double[] circle = circles.get(i, j);

                //===
                //System.out.println(Arrays.toString(circle));
                //===

                //Imgproc.circle(result, new Point(circle[0], circle[1]),
                //        (int) circle[2], purple);

                //ListCircles.add(new double[] {circle[0] - circle[2], circle[1] - circle[2], circle[0] + circle[2], circle[1] + circle[2]});
                ListCircles.add(new Circle (circle[0], circle[1], circle[2]));

                //Imgproc.rectangle(result, new Point(circle[0] - circle[2], circle[1] - circle[2]),
                //        new Point(circle[0] + circle[2], circle[1] + circle[2]), purple, 5);
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

                //double[] rect = ListCircles.get(i);
                double[] rect = {temp.getX() - temp.getRadius(), temp.getY() - temp.getRadius(), temp.getX() + temp.getRadius(), temp.getY() + temp.getRadius()};


                //System.out.println(Arrays.toString(rect));

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
