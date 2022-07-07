package com.example.practica_v1;

import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.Mat;

import org.opencv.core.Scalar;


import org.opencv.core.CvType;
import org.opencv.core.Point;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.opencv.core.Size;

public class frame {
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private Mat imgOriginal;
    private String pathOriginal;
    private String nameOriginal;

    private Mat imgTemp;

    private Mat imgMainColor;
    private String pathMainColor;
    private Mat imgWhiteBlack;
    private String pathWhiteBlack;
    private Mat imgRecognized;
    private String pathRecognized;


    private Circles circles = new Circles();

    private int mainColor = 50;    // разница между красным каналом и остальными.
    //private int mainColor = 75;    // разница между красным каналом и остальными.
    // уменьшая его можно больше красных и близких к ним точек выделять
    // увеличивая его - всё более красные точки выделяются

    volatile boolean isRecognised;

    public boolean GetIsRecognized()
    {
        return isRecognised;
    }

    public frame()
    {
        ;
    }

    public frame(String pathForOriginImg, String NameOriginImg)
    {
        ReadImgOriginal(pathForOriginImg, NameOriginImg);
    }


    public Mat GetOriginal()
    {
        return imgOriginal;
    }
    public Mat GetTemp()
    {
        return imgTemp;
    }
    public Mat GetMainColor()
    {
        return imgMainColor;
    }
    public Mat GetWight()
    {
        return imgWhiteBlack;
    }
    public Mat GetRecognized()
    {
        return imgRecognized;
    }

    public boolean ReadImgOriginal(String path, String name)
    {
        this.imgOriginal = Imgcodecs.imread(path + "/" + name);
        this.pathOriginal = new String(path);
        this.nameOriginal = new String(name);
        if (this.imgOriginal.empty()) {
            System.out.println("Failed to load original image");
            return false;
        }
        else {
            System.out.println("File:' " + pathOriginal + "/" + nameOriginal + " opened");
            return true;
        }
    }

public void WriteOriginal(String newFullPath) {

    boolean st = Imgcodecs.imwrite(newFullPath, imgOriginal);
    if (!st) {
        System.out.println("Failed to save original image");
    }
    else {
        System.out.println("File:' " + newFullPath + " saved");
    }
}

    public void WriteMainColor(String newFullPath) {

        boolean st = Imgcodecs.imwrite(newFullPath, imgMainColor);
        if (!st) {
            System.out.println("Failed to save image in primary color");
        }
        else {
            System.out.println("File:' " + newFullPath + " saved");
        }
    }


    public void WriteTemp(String newFullPath) {

        boolean st = Imgcodecs.imwrite(newFullPath, imgTemp);
        if (!st) {
            System.out.println("Failed to save 'rich' image");
        }
        else {
            System.out.println("File:' " + newFullPath + " saved");
        }
    }

    public void WriteWhiteBlack(String newFullPath) {

        boolean st = Imgcodecs.imwrite(newFullPath, imgWhiteBlack);
        if (!st) {
            System.out.println("Failed to save black and white image");
        }
        else {
            System.out.println("File:' " + newFullPath + " saved");
        }
    }

    public void WriteRecognised(String newFullPath) {

        if (!imgRecognized.empty()) {
            boolean st = Imgcodecs.imwrite(newFullPath, imgRecognized);
            if (!st) {
                System.out.println("Failed to save recognized image");
            } else {
                System.out.println("File:' " + newFullPath + " saved");
            }
        }
    }

public void IncreasingSaturation() {

    if (imgOriginal.empty())
    {
        ;
    }
    else {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(imgOriginal, imgHSV, Imgproc.COLOR_BGR2HSV);
        // Увеличение насыщенности
        Core.add(imgHSV, new Scalar(0, 40, 0), imgHSV);
        Mat imgBGR = new Mat();
        Imgproc.cvtColor(imgHSV, imgBGR, Imgproc.COLOR_HSV2BGR);
        imgTemp = imgBGR;
    }
}


    public void SetMainColor(int color)
    {
        mainColor = color;
    }

    public void OriginalToMainColor()
    {

        Mat WorkImg = imgTemp;

        if (WorkImg.empty())
        {
            ;
        }
        else {
            // Создаём новое пустое изображение, такого же размера
            imgMainColor = new Mat(WorkImg.rows(), WorkImg.cols(), WorkImg.type());

            int channels = WorkImg.channels();// Получить количество каналов изображения
            double[] pixel = new double[3];

            // Обработаем каждый пиксель исходного изображения
            for (int x = 0; x < WorkImg.rows(); x++) {          // цикл по ширине
                for (int y = 0; y < WorkImg.cols(); y++) {       // цикл по высоте

                    // Получаем цвет текущего пикселя
                    pixel = WorkImg.get(x, y).clone();

                    // Получаем красную, зелёную и синюю составляющую цвета
                    double blue = pixel[0];
                    double green = pixel[1];
                    double red = pixel[2];


                    // Если красного много - сделаем полносью красный
                    if ((red > (blue + mainColor)) && (red > (green + mainColor))) {
                        red = 255;
                        green = 0;
                        blue = 0;
                    }
                    // иначе пусть будет белый цвет (255,255,255) (черный - RGB=(0,0,0))
                    else {
                        red = 255;
                        green = 255;
                        blue = 255;
                    }

                    //double newRed = red;
                    //double newBlue = blue;
                    //double newGreen = green;


                    pixel[0] = blue;
                    pixel[1] = green;
                    pixel[2] = red;
                    //imgTemp.put(x, y, pixel);

                    // Создадим новый цвет
                    //Scalar newColor = new Scalar(newRed, newGreen, newBlue, 0);

                    // Установим этот цвет в пиксель нового изображения
                    //im2.put(x, y, arr);
                    imgMainColor.put(x, y, pixel);
                }
            }
            // Сохраним результат в файл
            //File output = new File("step1.jpg");
            //ImageIO.write(im2, "jpg", output);
        }
    }

    public void MainColorToGray()
    {
        if (imgMainColor.empty()) {
            System.out.println("MainColor is empty");
        }
        else {
            Mat img2 = new Mat();
            Imgproc.cvtColor(imgMainColor, img2, Imgproc.COLOR_BGR2GRAY);
            Mat img3 = new Mat();
            double thresh = Imgproc.threshold(img2, img3, 100, 255,
                    Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            //System.out.println(thresh);
            imgWhiteBlack = img3;
        }
    }


    /*
    public void TestCircles()
    {

        Mat workImg = imgWhiteBlack;

        Mat circles = new Mat();
        //Imgproc.HoughCircles(imgWhiteBlack, circles, Imgproc.HOUGH_GRADIENT,
        //        2, imgWhiteBlack.cols() imgWhiteBlack.rows() / 4 );
        Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT,1, workImg.rows() / 8, 100, 15, 0, 0);
        Scalar white = new Scalar(255, 255, 255);
        Scalar purple = new Scalar(255, 0, 0);
        double b = 0;
        int w = 255;
        Mat result = new Mat(workImg.size(), CvType.CV_8UC3, white);

        //Mat result = imgOriginal;

        //===
        System.out.println("Arrays circles");
        System.out.println(circles.dump());
        //===

        for (int i = 0, r = circles.rows(); i < r; i++) {
            for (int j = 0, c = circles.cols(); j < c; j++) {
                double[] circle = circles.get(i, j);

                //===
                System.out.println(Arrays.toString(circle));
                //===

                //Imgproc.circle(result, new Point(circle[0], circle[1]),
                //        (int) circle[2], purple);
                Imgproc.rectangle(result, new Point(circle[0] - circle[2], circle[1] - circle[2]),
                        new Point(circle[0] + circle[2], circle[1] + circle[2]), purple, 5);
            }
        }
        imgRecognized = result;
    }
     */

    public boolean RecognizeAndDrawCircles()
    {
        boolean Res = false;

        if (imgWhiteBlack.empty() || imgOriginal.empty())
            return false;

        imgRecognized = imgOriginal.clone();
        if (circles.FindAndDrawCircles(imgWhiteBlack, imgRecognized))
        {
            Res = true;
            isRecognised = true;
        }

        return Res;
    }

    public synchronized void WorkWithFrame()
    {
        IncreasingSaturation();
        BlurRichImg();
        OriginalToMainColor();
        MainColorToGray();
        //BlurWightBlackImg();
        RecognizeAndDrawCircles();
    }

    public void BlurRichImg()
    {
        Imgproc.blur(imgTemp, imgTemp, new Size(3, 3));
    }

    public void BlurWightBlackImg()
    {
        Imgproc.blur(imgWhiteBlack, imgWhiteBlack, new Size(3, 3));
    }

    public void OriginalToWhiteBlack()
    {
        ;
    }

}
