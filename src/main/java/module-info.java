module com.example.practica_v1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires opencv;
    requires java.desktop;

    opens com.example.practica_v1 to javafx.fxml;
    exports com.example.practica_v1;
}