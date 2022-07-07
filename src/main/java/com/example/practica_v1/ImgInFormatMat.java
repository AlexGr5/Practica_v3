package com.example.practica_v1;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImgInFormatMat {

    private Mat Img;

    private String Path;

    //private String Name;

    private String info;


    public ImgInFormatMat()
    {

    }

    public ImgInFormatMat(Mat img, String path, String name, String information)
    {
        Img = img.clone();
        Path = path;
        //Name = name;
        info = information;
    }

    public void SetImgInFormatMat(Mat img, String path, String name, String information)
    {
        Img = img.clone();
        Path = path;
        //Name = name;
        info = information;
    }

    public Mat GetImg()
    {
        return Img;
    }
    public void SetImg(Mat newImg)
    {
        Img = newImg.clone();
    }

    public String GetInfo()
    {
        return info;
    }
    public void SetInfo(String information)
    {
        info = information;
    }

    public boolean ReadImg(String path,String information)
    {
        this.info = information;
        this.Img = Imgcodecs.imread(path);
        this.Path = new String(path);
        if (this.Img.empty()) {
            System.out.println("Failed to load " + information +" image");
            return false;
        }
        else {
            System.out.println("File:' " + Path + " opened");
            return true;
        }
    }

    public void WriteImg(String newFullPath, String information) {

        if(!Img.empty()) {
            boolean st = Imgcodecs.imwrite(newFullPath, Img);
            if (!st) {
                System.out.println("Failed to save " + info + " image");
            } else {
                System.out.println("File:' " + newFullPath + " saved");
            }
        }
        else {
            System.out.println("Failed to save " + info + " image. Mat exist!");
        }
    }

}
