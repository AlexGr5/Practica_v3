package com.example.practica_v1;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FilesAndDirs {

    protected String DirPath;

    protected String TypeOfNeedFiles;
    protected ArrayList<String> ListOfAllFiles;
    protected ArrayList<String> ListOfNeededFiles;

    volatile protected List<frame> ListOfFrames = new ArrayList<frame>();

    protected RecognitionParameters paramsRecogn;

    protected ColorParams colorParams;

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

    public void SetParamsRecognized(RecognitionParameters parameters)
    {
        paramsRecogn = new RecognitionParameters(parameters.getDp(), parameters.getDenominatorOfMinDist(),
                parameters.getParam1(), parameters.getParam2(),parameters.getMinRadius(),parameters.getMaxRadius());
    }

    public void SetColorParams(ColorParams NewColorParams)
    {
        colorParams = new ColorParams(NewColorParams.isISAutomaticColor(), NewColorParams.getRed(),
                NewColorParams.getGreen(), NewColorParams.getBlue(), NewColorParams.getDelta());
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

        return ArrType;
    }

    public boolean CreateWorkingDirs(String MainPath)
    {
        boolean Res = false;

        String SuccessfulName = "/Successful";
        String UnsuccessfulName = "/Unsuccessful";
        String TempName = "/Temp";
        String SceneName = "/Scene";

        Directory dirSuccessful = new Directory(MainPath + SuccessfulName);
        Directory dirUnsuccessful = new Directory(MainPath + UnsuccessfulName);
        Directory dirTemp = new Directory(MainPath + TempName);
        
        if(dirSuccessful.CreateDir())
            if(dirUnsuccessful.CreateDir())
                if(dirTemp.CreateDir())
                    Res = true;

        return Res;
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

    public boolean ImageListProcessing()
    {
        boolean Res = false;

        if (DirPath.length() > 0)
        {
            if (ListOfNeededFiles.size() > 0)
            {
                Res = true;

                LoadProcessingRecognizeSendToDisplaySave();
            }
        }

        return Res;
    }

    private void LoadProcessingRecognizeSendToDisplaySave() {
        for (int i = 0; i < ListOfNeededFiles.size(); i++)
        {
            ListOfFrames.add(new frame(DirPath + "/" + ListOfNeededFiles.get(i)));

            // = new frame(DirPath, ListOfNeededFiles.get(i));

            ListOfFrames.get(i).WorkWithFrame(paramsRecogn, colorParams);

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


    public void MainProcessVariable2(String Path, String TypeOfFiles, RecognitionParameters parameters, ColorParams NewColorParams)
    {
        SetParamsRecognized(parameters);
        SetColorParams(NewColorParams);
        SetDirPath(Path);
        SelectTypeFilesOfListFiles(FilesInDirToList(Path), ".jpg");
        CreateWorkingDirs(Path);

        ImageListProcessing();

        System.out.println("\n\nRecognizing end");

    }



    public void ChangeLabel(Label lb)
    {
        lb.setText("New text");
    }

}
