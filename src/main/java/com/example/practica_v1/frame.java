package com.example.practica_v1;

import org.opencv.core.Core;

import org.opencv.imgproc.Imgproc;

import org.opencv.core.Mat;

import org.opencv.core.Scalar;



import org.opencv.core.Size;

public class frame {
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private ImgInFormatMat imgOriginal;
    private ImgInFormatMat imgTemp;
    private ImgInFormatMat imgMainColor;
    private ImgInFormatMat imgWhiteBlack;
    private ImgInFormatMat imgRecognized;

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
        imgOriginal = new ImgInFormatMat();
        imgTemp = new ImgInFormatMat();
        imgWhiteBlack = new ImgInFormatMat();
        imgMainColor = new ImgInFormatMat();
        imgRecognized = new ImgInFormatMat();
    }

    public frame(String pathForOriginImg)
    {
        imgOriginal = new ImgInFormatMat();
        imgTemp = new ImgInFormatMat();
        imgWhiteBlack = new ImgInFormatMat();
        imgMainColor = new ImgInFormatMat();
        imgRecognized = new ImgInFormatMat();

        ReadImgOriginal(pathForOriginImg);
    }


    public Mat GetOriginal()
    {
        return imgOriginal.GetImg();
    }
    public Mat GetTemp()
    {
        return imgTemp.GetImg();
    }
    public Mat GetMainColor()
    {
        return imgMainColor.GetImg();
    }
    public Mat GetWight()
    {
        return imgWhiteBlack.GetImg();
    }
    public Mat GetRecognized()
    {
        return imgRecognized.GetImg();
    }

    public boolean ReadImgOriginal(String path)
    {
        if(imgOriginal.ReadImg(path, "original"))
            return true;
        else return false;
    }

    public void WriteOriginal(String newFullPath) {

        imgOriginal.WriteImg(newFullPath, "original");
    }

    public void WriteMainColor(String newFullPath) {

        imgMainColor.WriteImg(newFullPath, "MainColor");
    }


    public void WriteTemp(String newFullPath) {

        imgTemp.WriteImg(newFullPath, "temp");

    }

    public void WriteWhiteBlack(String newFullPath) {

        imgWhiteBlack.WriteImg(newFullPath, "White and Black");

    }

    public void WriteRecognised(String newFullPath) {

        imgOriginal.WriteImg(newFullPath, "original");

    }

public void IncreasingSaturation() {

    if (imgOriginal.GetImg().empty())
    {
        ;
    }
    else {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(imgOriginal.GetImg(), imgHSV, Imgproc.COLOR_BGR2HSV);
        // Увеличение насыщенности
        Core.add(imgHSV, new Scalar(0, 40, 0), imgHSV);
        Mat imgBGR = new Mat();
        Imgproc.cvtColor(imgHSV, imgBGR, Imgproc.COLOR_HSV2BGR);
        imgTemp.SetImg(imgBGR);
    }
}


    public void SetMainColor(int color)
    {
        mainColor = color;
    }

    public void OriginalToMainColor()
    {

        Mat WorkImg = imgTemp.GetImg();

        if (WorkImg.empty())
        {
            ;
        }
        else {
            // Создаём новое пустое изображение, такого же размера
            imgMainColor.SetImg(new Mat(WorkImg.rows(), WorkImg.cols(), WorkImg.type()));

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


                    pixel[0] = blue;
                    pixel[1] = green;
                    pixel[2] = red;

                    // Установим этот цвет в пиксель нового изображения
                    imgMainColor.GetImg().put(x, y, pixel);
                }
            }
        }
    }


    public void OriginalToMainColorAlternative(ColorParams colorParams)
    {
        Mat WorkImg = imgTemp.GetImg();

        if (WorkImg.empty())
        {
            ;
        }
        else {
            // Создаём новое пустое изображение, такого же размера
            imgMainColor.SetImg(new Mat(WorkImg.rows(), WorkImg.cols(), WorkImg.type()));

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


                    double maxValueColor;

                    maxValueColor = Math.max(Math.max(colorParams.getRed(), colorParams.getGreen()), colorParams.getBlue());

                    double mainChoiceColor = 0;
                    double addColor1 = 0;
                    double addColor2 = 0;

                    if(maxValueColor == colorParams.getRed()) {
                        mainChoiceColor = red;
                        addColor1 = green;
                        addColor2 = blue;
                    }
                    if(maxValueColor == colorParams.getGreen()) {
                        mainChoiceColor = green;
                        addColor1 = red;
                        addColor2 = blue;
                    }
                    if(maxValueColor == colorParams.getBlue()) {
                        mainChoiceColor = blue;
                        addColor1 = green;
                        addColor2 = red;
                    }

                    // Если основного много - сделаем полносью красный
                    if ((mainChoiceColor > (addColor1 + colorParams.getDelta())) &&
                            (mainChoiceColor > (addColor2 + colorParams.getDelta()))){
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


                    pixel[0] = blue;
                    pixel[1] = green;
                    pixel[2] = red;


                    // Установим этот цвет в пиксель нового изображения
                    imgMainColor.GetImg().put(x, y, pixel);
                }
            }
        }
    }


    public void MainColorToGray()
    {
        if (imgMainColor.GetImg().empty()) {
            System.out.println("MainColor is empty");
        }
        else {
            Mat img2 = new Mat();
            Imgproc.cvtColor(imgMainColor.GetImg(), img2, Imgproc.COLOR_BGR2GRAY);
            Mat img3 = new Mat();
            double thresh = Imgproc.threshold(img2, img3, 100, 255,
                    Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            //System.out.println(thresh);
            imgWhiteBlack.SetImg(img3);
        }
    }


    public boolean RecognizeAndDrawCircles(RecognitionParameters parameters)
    {
        boolean Res = false;

        if (imgWhiteBlack.GetImg().empty() || imgOriginal.GetImg().empty())
            return false;

        imgRecognized.SetImg(imgOriginal.GetImg().clone());
        if (circles.FindAndDrawCircles(imgWhiteBlack.GetImg(), imgRecognized.GetImg(), parameters))
        {
            Res = true;
            isRecognised = true;
        }

        return Res;
    }

    public synchronized void WorkWithFrame(RecognitionParameters parameters, ColorParams NewColorParams)
    {
        IncreasingSaturation();
        BlurRichImg();
        if(NewColorParams.isISAutomaticColor())
            OriginalToMainColorAlternative(NewColorParams);
        else {
            OriginalToMainColor();
        }
        MainColorToGray();
        //BlurWightBlackImg();
        RecognizeAndDrawCircles(parameters);
    }

    public void BlurRichImg()
    {
        Imgproc.blur(imgTemp.GetImg(), imgTemp.GetImg(), new Size(3, 3));
    }

    public void BlurWightBlackImg()
    {
        Imgproc.blur(imgWhiteBlack.GetImg(), imgWhiteBlack.GetImg(), new Size(3, 3));
    }

}
