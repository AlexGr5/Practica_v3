package com.example.practica_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Scalar;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class FilesAndDirs {

    protected String DirPath;

    protected String TypeOfNeedFiles;
    protected ArrayList<String> ListOfAllFiles;
    protected ArrayList<String> ListOfNeededFiles;

    volatile protected List<frame> ListOfFrames = new ArrayList<frame>();

    protected frame CurrentFrame;

    volatile protected MyTask myTask;

    public void SetMyTask(MyTask myTask1)
    {
        myTask = myTask1;
    }

    public int GetSizeFramesList()
    {
        if(ListOfFrames.size() > 0)
            return ListOfFrames.size();

        else return 0;
    }

    public frame GetFrameInListIndex(int index)
    {
        if(ListOfFrames.size() > 0 && index > 0 && index < ListOfFrames.size())
            return ListOfFrames.get(index);

        else return null;
    }

    public void SetDirPath(String path)
    {
        DirPath = new String(path);
    }
    public String GetDirPath()
    {
        return new String(DirPath);
    }

    public ArrayList<String> FilesInDirToList(String path) {

        ArrayList<String> ArrListOfFiles = new ArrayList<String>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                ArrListOfFiles.add(file.getName());
            }
        }

        ListOfAllFiles = (ArrayList<String>)ArrListOfFiles.clone();

        return ArrListOfFiles;
    }

    public ArrayList<String> SelectTypeFilesOfListFiles (ArrayList<String> ArrList, String typeOfFile)
    {
        ArrayList<String> ArrType = new ArrayList<String>();

        TypeOfNeedFiles = new String(typeOfFile);

        for (int i = 0; i < ArrList.size(); i++)
        {
            if (ArrList.get(i).length() >= typeOfFile.length()) {
                if (ArrList.get(i).regionMatches(ArrList.get(i).length() - typeOfFile.length(), typeOfFile, 0, typeOfFile.length())) {
                    ArrType.add(ArrList.get(i));
                }
            }
        }

        ListOfNeededFiles = (ArrayList<String>)ArrType.clone();

        /*
        for (int i = 0; i < ArrType.size(); i++) {

            System.out.println(ArrType.get(i));
        }
        */

        return ArrType;
    }

    public boolean CreateWorkingDirs(String MainPath)
    {
        boolean Res = false;

        String SuccessfulName = "/Successful";
        String UnsuccessfulName = "/Unsuccessful";
        String TempName = "/Temp";
        String SceneName = "/Scene";

        Path pathSuccessful = Paths.get(MainPath + SuccessfulName);
        Path pathUnsuccessful = Paths.get(MainPath + UnsuccessfulName);
        Path pathTemp = Paths.get(MainPath + TempName);
        Path pathScene = Paths.get(MainPath + SceneName);

        if (!Files.exists(pathSuccessful)) {
            try {
                Files.createDirectory(pathSuccessful);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Res = true;
            System.out.println("New Directory created !   "+MainPath + SuccessfulName);

            if (!Files.exists(pathUnsuccessful)) {
                try {
                    Files.createDirectory(pathUnsuccessful);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("New Directory created !   "+MainPath + UnsuccessfulName);
                Res = true;

                if (!Files.exists(pathTemp)) {
                    try {
                        Files.createDirectory(pathTemp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Res = true;
                    System.out.println("New Directory created !   "+MainPath + TempName);

                    if (!Files.exists(pathScene)) {
                        try {
                            Files.createDirectory(pathScene);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Res = true;
                        System.out.println("New Directory created !   "+MainPath + SceneName);


                    } else {
                        Res = false;
                        System.out.println("Directory already exists");
                    }

                } else {
                    Res = false;
                    System.out.println("Directory already exists");
                }

            } else {
                System.out.println("Directory already exists");
                Res = false;
            }

        } else {
            Res = false;
            System.out.println("Directory already exists");
        }

        return Res;
    }

    public boolean LoadListFrames()
    {
        boolean Res = false;

        if (DirPath.length() > 0)
        {
            if (ListOfNeededFiles.size() > 0)
            {
                Res = true;

                for (int i = 0; i < ListOfNeededFiles.size(); i++) {
                    ListOfFrames.add(new frame(DirPath, ListOfNeededFiles.get(i)));
                }
            }
        }

        return Res;
    }

    public void ProcessingFrames()
    {
        for (int i = 0; i < ListOfNeededFiles.size(); i++) {
            ListOfFrames.get(i).WorkWithFrame();

            if (ListOfFrames.get(i).GetIsRecognized())
            {
                System.out.println("Img " + DirPath + "/" + ListOfNeededFiles.get(i) + " is recognized!");
            }
            else {

                System.out.println("Img " + DirPath + "/" + ListOfNeededFiles.get(i) + " is not recognized!");
            }
            myTask.updateProgress((long)i,(long)ListOfNeededFiles.size());
        }
    }

    public void SaveFrames()
    {
        for (int i = 0; i < ListOfNeededFiles.size(); i++) {
            StringBuffer SBName = new StringBuffer(ListOfNeededFiles.get(i));

            StringBuffer NameTempImg = new StringBuffer(SBName);
            NameTempImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_temp");

            StringBuffer NameMainColorImg = new StringBuffer(SBName);
            NameMainColorImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_MC");

            StringBuffer NameWightBlackImg = new StringBuffer(SBName);
            NameWightBlackImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_WB");

            StringBuffer NameRecognImg = new StringBuffer(SBName);
            NameRecognImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_Recogn");

            ListOfFrames.get(i).WriteTemp(DirPath + "/Temp/" + NameTempImg);
            ListOfFrames.get(i).WriteMainColor(DirPath + "/Temp/" + NameMainColorImg);
            ListOfFrames.get(i).WriteWhiteBlack(DirPath + "/Temp/" + NameWightBlackImg);

            if (ListOfFrames.get(i).GetIsRecognized()) {
                ListOfFrames.get(i).WriteRecognised(DirPath + "/Successful/" + NameRecognImg);
            }
            else {
                ListOfFrames.get(i).WriteOriginal(DirPath + "/Unsuccessful/" + ListOfNeededFiles.get(i));
            }
        }
    }

    public void MainProcess(String Path, String TypeOfFiles)
    {
        SetDirPath(Path);
        SelectTypeFilesOfListFiles(FilesInDirToList(Path), ".jpg");
        CreateWorkingDirs(Path);

        LoadListFrames();
        ProcessingFrames();
        SaveFrames();

        System.out.println("\n\nRecognizing end");
    }

    public void SaveFrame(int index)
    {
        StringBuffer SBName = new StringBuffer(ListOfNeededFiles.get(index));

        StringBuffer NameTempImg = new StringBuffer(SBName);
        NameTempImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_temp");

        StringBuffer NameMainColorImg = new StringBuffer(SBName);
        NameMainColorImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_MC");

        StringBuffer NameWightBlackImg = new StringBuffer(SBName);
        NameWightBlackImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_WB");

        StringBuffer NameRecognImg = new StringBuffer(SBName);
        NameRecognImg.insert(SBName.length() - TypeOfNeedFiles.length(), "_Recogn");

        ListOfFrames.get(index).WriteTemp(DirPath + "/Temp/" + NameTempImg);
        ListOfFrames.get(index).WriteMainColor(DirPath + "/Temp/" + NameMainColorImg);
        ListOfFrames.get(index).WriteWhiteBlack(DirPath + "/Temp/" + NameWightBlackImg);

        if (ListOfFrames.get(index).GetIsRecognized()) {
            ListOfFrames.get(index).WriteRecognised(DirPath + "/Successful/" + NameRecognImg);
        }
        else {
            ListOfFrames.get(index).WriteOriginal(DirPath + "/Unsuccessful/" + ListOfNeededFiles.get(index));
        }
    }

    public boolean ImageProcessingOneByOne()
    {
        boolean Res = false;

        if (DirPath.length() > 0)
        {
            if (ListOfNeededFiles.size() > 0)
            {
                Res = true;

                for (int i = 0; i < ListOfNeededFiles.size(); i++)
                {
                    ListOfFrames.add(new frame(DirPath, ListOfNeededFiles.get(i)));

                    // = new frame(DirPath, ListOfNeededFiles.get(i));

                    ListOfFrames.get(i).WorkWithFrame();

                    if (ListOfFrames.get(i).GetIsRecognized())
                    {
                        System.out.println("Img " + DirPath + "/" + ListOfNeededFiles.get(i) + " is recognized!");
                    }
                    else {

                        System.out.println("Img " + DirPath + "/" + ListOfNeededFiles.get(i) + " is not recognized!");
                    }

                    myTask.updateProgress((long)i,(long)ListOfNeededFiles.size());

                    SaveFrame(i);
                }
            }
        }

        return Res;
    }


    public void MainProcessVariable2(String Path, String TypeOfFiles)
    {
        SetDirPath(Path);
        SelectTypeFilesOfListFiles(FilesInDirToList(Path), ".jpg");
        CreateWorkingDirs(Path);

        ImageProcessingOneByOne();

        System.out.println("\n\nRecognizing end");
    }



    public void ChangeLabel(Label lb)
    {
        lb.setText("New text");
    }

}
