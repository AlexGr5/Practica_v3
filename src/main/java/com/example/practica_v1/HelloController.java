package com.example.practica_v1;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;


import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import java.io.File;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Scalar;
import org.opencv.core.CvType;
import org.opencv.core.Mat;



public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private AnchorPane anchorID;

    @FXML
    private URL location;

    @FXML
    private Label labelInpPath;

    @FXML
    private Label LabelRecognized;

    @FXML
    private Label labelColor;

    @FXML
    public ProgressBar ProgressBarMain;

    @FXML
    private Button ButtonMain;

    @FXML
    private TextField TextFieldDir;

    @FXML
    private TextField dpTextField;

    @FXML
    private TextField minDistTextField;

    @FXML
    private TextField param1TextField;

    @FXML
    private TextField param2TextField;

    @FXML
    private TextField minRadiusTextField;

    @FXML
    private TextField maxRadiusTextField;

    @FXML
    private TextField DeltaTextField;

    @FXML
    private Button btChoice;

    @FXML
    private Button ButSettingsDefalt;

    @FXML
    private ImageView imgMainColor;

    @FXML
    private ImageView imgOrig;

    @FXML
    private ImageView imgRecogn;

    @FXML
    private ImageView imgWB;

    @FXML
    private ColorPicker colorPicker1;

    @FXML
    private RadioButton RButColor;

    private boolean IsProcessing = false;

    private boolean ISAutomaticColor = true;

    private double red = 255;
    private double green = 0;
    private double blue = 0;

    private int Delta = 50;

    @FXML
    void initialize() {
        ButtonMain.setOnAction(this::OnClickMainButton);
        btChoice.setOnAction(this::OnClickChoiceButton);
        ButSettingsDefalt.setOnAction(this::OnClickDefaltSettingsButton);
        /*ButtonMain.setOnAction(event -> {
            frame frame1 = new frame();

            frame1.ReadImgOriginal("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src","t.jpg");
            frame1.WriteOriginal("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src/t_new.jpg");

            System.out.println("You've clicked!");
        });
         */


        RButColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!RButColor.isSelected()) {
                    colorPicker1.setDisable(false);
                    DeltaTextField.setDisable(false);
                }
                else{
                    colorPicker1.setDisable(true);
                    DeltaTextField.setDisable(true);
                }
            }
        });

        colorPicker1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //circleColor.setFill(colorPicker.getValue());
                System.out.println("Color1  = " + colorPicker1.getValue());
            }
        });


        DeltaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                DeltaTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        dpTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dpTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        minDistTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minDistTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        param1TextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                param1TextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        param2TextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                param2TextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        minRadiusTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minRadiusTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        maxRadiusTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxRadiusTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    private void OnClickDefaltSettingsButton(ActionEvent event) {

        RButColor.setSelected(true);
        colorPicker1.setDisable(true);
        DeltaTextField.setDisable(true);
        DeltaTextField.setText(String.valueOf(50));
        dpTextField.setText(String.valueOf(1));
        minDistTextField.setText(String.valueOf(8));
        param1TextField.setText(String.valueOf(100));
        param2TextField.setText(String.valueOf(15));
        minRadiusTextField.setText(String.valueOf(300));
        maxRadiusTextField.setText(String.valueOf(400));
    }

    public void OnClickChoiceButton(ActionEvent event) {

        if (!IsProcessing) {

            final DirectoryChooser directoryChooser = new DirectoryChooser();

            Stage stage = (Stage) btChoice.getScene().getWindow();

            FilesAndDirs NewFrames = new FilesAndDirs();

            File dir = directoryChooser.showDialog(stage);

            if (dir != null) {
                TextFieldDir.setText(dir.getAbsolutePath());

                //NewFrames.MainProcess(dir.getAbsolutePath(), ".jpg");

                //===
                //NewFrames.SetDirPath(dir.getAbsolutePath());
                //ArrayList<String> ListAllFiles = NewFrames.FilesInDirToList(dir.getAbsolutePath());
                //NewFrames.SelectTypeFilesOfListFiles(ListAllFiles, ".jpg");
                //NewFrames.CreateWorkingDirs(dir.getAbsolutePath());
                //===
            } else {
                TextFieldDir.setText(null);
            }
        }
        else
        {
            MessegeBoxError("Runtime error!", "The recognition process has already been started!", "Wait for the end of recognition and choose the path");
        }

    }




    @FXML
    public void OnClickMainButton(ActionEvent event) {

        if(!IsProcessing) {

            FilesAndDirs NewFrames = new FilesAndDirs();

            Path path = Paths.get(TextFieldDir.getText());

            RecognitionParameters temp = new RecognitionParameters(Integer.parseInt(dpTextField.getText()),
                    Integer.parseInt(minDistTextField.getText()), Integer.parseInt(param1TextField.getText()),
                    Integer.parseInt(param2TextField.getText()), Integer.parseInt(minRadiusTextField.getText()),
                    Integer.parseInt(maxRadiusTextField.getText()));

            System.out.println("Button click");

            ColorParams colorParams;

            if(RButColor.isSelected())
            {
                System.out.println("RB is selected");

                ISAutomaticColor = false;
                red = 255;
                green = 0;
                blue = 0;
                Delta = 50;
                colorParams = new ColorParams(ISAutomaticColor, red, green, blue, Delta);


            }
            else {
                System.out.println("RB is not selected");

                ISAutomaticColor = true;
                red = colorPicker1.getValue().getRed();
                green = colorPicker1.getValue().getGreen();
                blue = colorPicker1.getValue().getBlue();
                Delta = Integer.parseInt(DeltaTextField.getText());
                colorParams = new ColorParams(ISAutomaticColor, red, green, blue, Delta);
            }

            if (TextFieldDir.getText().length() == 0) {
                MessegeBoxError("Error path!", "Path not selected!", "Enter or select the correct path");
            } else {

                boolean parallel = true;    // Для отладки


                if (Files.exists(path)) {

                    IsProcessing = true;
                    ProgressBarMain.setProgress(0.0);

                    if (parallel) {
                        final MyTask task = new MyTask();
                    /*
                    //==========================================================================
                    //==========================================================================
                    final Task task = new Task<Void>() {
                        @Override
                        public Void call() throws Exception {
                            //int max = 10;
                            //for (int i = 1; i <= max; i++) {
                            //    updateProgress(i,max);
                            //    Thread.sleep(200);
                            //}
                            NewFrames.MainProcess(TextFieldDir.getText(), ".jpg");
                            return null;
                        }

                        @Override
                        public void updateProgress(long workDone, long max) {
                            PrintFrameInImgViews(NewFrames.GetFrameInListIndex((int) workDone));
                            ProgressBarMain.setProgress((double) workDone / (double) max);
                        }
                    };
                    new Thread(task).start();
                    //1//ExecutorService es = Executors.newSingleThreadExecutor ();
                    //1//es.submit (task);
                    //1//es.shutdown ();
                    //2//Service service=new Service<Void>() {
                    //2//    @Override
                    //2//     protected Task createTask() {
                    //2//        return task;
                    //2//    }
                    //2//};
                    //2//service.start();
                    //}
                    //==========================================================================
                    //==========================================================================
                     */
                        //ProgressBarMain.setProgress(0.0);


                        task.setParameters(temp);
                        task.SetColorParams(colorParams);
                        task.SetFrames(NewFrames);
                        task.SetHelloController(this);
                        task.SetPath(TextFieldDir.getText());
                        NewFrames.SetMyTask(task);
                        new Thread(task).start();
                    } else {
                        //NewFrames.MainProcess(TextFieldDir.getText(), ".jpg");
                        NewFrames.MainProcessVariable2(TextFieldDir.getText(), ".jpg", temp, new ColorParams(false, 255, 0, 0, 50));
                    }

                    IsProcessing = false;

                } else {
                    MessegeBoxError("Error path!", "Invalid path specified!", "Enter or select the correct path");
                }
            }

            //PrintImgInImgView(NewFrames);

        /*
        frame frame1 = new frame();
        frame1.ReadImgOriginal("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src","t.jpg");
        frame1.WriteOriginal("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src/t_new.jpg");
        frame1.IncreasingSaturation();
        frame1.WriteTemp("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src/t_TEMP.jpg");
        frame1.OriginalToMainColor();
        frame1.WriteMainColor("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src/t_MC.jpg");
        frame1.MainColorToGray();
        frame1.WriteWhiteBlack("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src/t_WB.jpg");
        //frame1.TestCircles();
        frame1.RecognizeAndDrawCircles();
        frame1.WriteRecognised("D:/altstu/2kurs/LetPract/testProj/Practica_v1/src/t_Recogn.jpg");
         */


        /*
        frame1.ReadImgOriginal("D:/altstu/2kurs/LetPract/","2.jpg");
        frame1.WriteOriginal("D:/altstu/2kurs/LetPract/2_new.jpg");
        frame1.IncreasingSaturation();
        frame1.WriteTemp("D:/altstu/2kurs/LetPract//2_TEMP.jpg");
        frame1.OriginalToMainColor();
        frame1.WriteMainColor("D:/altstu/2kurs/LetPract/2_MC.jpg");
        frame1.MainColorToGray();
        frame1.WriteWhiteBlack("D:/altstu/2kurs/LetPract/2_WB.jpg");
        frame1.TestCircles();
        frame1.WriteRecognised("D:/altstu/2kurs/LetPract/2_Recogn.jpg");
        */
        }
        else
        {
            MessegeBoxError("Runtime error!","The recognition process has already been started!", "Wait for the end of recognition and click again");
        }

    }

    public void MessegeBoxError(String Title, String HeaderText, String ContentText)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(Title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }


    public void PrintFrameInImgViews(frame frame1) {
        if (frame1 != null) {

            if (frame1.GetIsRecognized()) {
                //LabelRecognized.setText("Is Recognized!");

                PrintImgInImgView(imgOrig, frame1.GetOriginal());
                PrintImgInImgView(imgMainColor, frame1.GetTemp());
                PrintImgInImgView(imgWB, frame1.GetMainColor());
                PrintImgInImgView(imgRecogn, frame1.GetRecognized());

            } else {
                //LabelRecognized.setText("Is NOT Recognized!");

                PrintImgInImgView(imgOrig, frame1.GetOriginal());
                PrintImgInImgView(imgMainColor, frame1.GetTemp());
                PrintImgInImgView(imgWB, frame1.GetMainColor());

                imgRecogn.setImage(null);
            }

        }
    }

    /*
    public void PrintFrameInImgViews(frame frame1) {
        if (frame1 != null) {

            if (frame1.GetIsRecognized()) {
                //LabelRecognized.setText("Is Recognized!");

                MatOfByte bufOrig = new MatOfByte();
                boolean stOrig = Imgcodecs.imencode(".jpg", frame1.GetOriginal(), bufOrig, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imOrig = new Image(new ByteArrayInputStream(bufOrig.toArray()));
                imgOrig.setImage(imOrig);

                MatOfByte bufTemp = new MatOfByte();
                boolean stTemp = Imgcodecs.imencode(".jpg", frame1.GetTemp(), bufTemp, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imTemp = new Image(new ByteArrayInputStream(bufTemp.toArray()));
                imgMainColor.setImage(imTemp);

                MatOfByte bufMainColor = new MatOfByte();
                boolean stMainColor = Imgcodecs.imencode(".jpg", frame1.GetMainColor(), bufMainColor, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imMainColor = new Image(new ByteArrayInputStream(bufMainColor.toArray()));
                imgWB.setImage(imMainColor);

                MatOfByte bufRecognized = new MatOfByte();
                boolean stRecognized = Imgcodecs.imencode(".jpg", frame1.GetRecognized(), bufRecognized, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imRecognized = new Image(new ByteArrayInputStream(bufRecognized.toArray()));
                imgRecogn.setImage(imRecognized);
            } else {
                //LabelRecognized.setText("Is NOT Recognized!");

                MatOfByte bufOrig = new MatOfByte();
                boolean stOrig = Imgcodecs.imencode(".jpg", frame1.GetOriginal(), bufOrig, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imOrig = new Image(new ByteArrayInputStream(bufOrig.toArray()));
                imgOrig.setImage(imOrig);

                MatOfByte bufTemp = new MatOfByte();
                boolean stTemp = Imgcodecs.imencode(".jpg", frame1.GetTemp(), bufTemp, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imTemp = new Image(new ByteArrayInputStream(bufTemp.toArray()));
                imgMainColor.setImage(imTemp);

                MatOfByte bufMainColor = new MatOfByte();
                boolean stMainColor = Imgcodecs.imencode(".jpg", frame1.GetMainColor(), bufMainColor, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
                Image imMainColor = new Image(new ByteArrayInputStream(bufMainColor.toArray()));
                imgWB.setImage(imMainColor);

                imgRecogn.setImage(null);
            }

        }
    }
     */



    public void PrintImgInImgView(ImageView ImgView, Mat img)
    {
        MatOfByte bufOrig = new MatOfByte();
        boolean stOrig = Imgcodecs.imencode(".jpg", img, bufOrig, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100));
        Image imOrig = new Image(new ByteArrayInputStream(bufOrig.toArray()));
        ImgView.setImage(imOrig);
    }



}
