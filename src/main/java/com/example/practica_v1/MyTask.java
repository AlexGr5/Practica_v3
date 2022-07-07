package com.example.practica_v1;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MyTask extends Task {

    volatile HelloController HCController;
    volatile private FilesAndDirs Frames;
    private String Path;

    private RecognitionParameters parameters;

    public void SetHelloController(HelloController HelloController1)
    {
        HCController = HelloController1;
    }

    public void SetFrames(FilesAndDirs Frames)
    {
        if (Frames != null)
            this.Frames = Frames;
    }

    public void SetPath(String path)
    {
        Path = path;
    }

    @Override
    public Void call() throws Exception {
        //int max = 10;
        //for (int i = 1; i <= max; i++) {
        //    updateProgress(i,max);
        //    Thread.sleep(200);
        //}
        Frames.SetMyTask(this);
        //Frames.MainProcess(Path, ".jpg");
        Frames.MainProcessVariable2(Path, ".jpg", parameters);

        return null;
    }

    @Override
    public void updateProgress(long workDone, long max) {
        HCController.PrintFrameInImgViews(Frames.GetFrameInListIndex((int) workDone));
        HCController.ProgressBarMain.setProgress(((double) workDone + 1) / (double) max);
    }

    public RecognitionParameters getParameters() {
        return parameters;
    }

    public void setParameters(RecognitionParameters parameters) {
        this.parameters = parameters;
    }
}
