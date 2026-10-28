module com.example.caso_estudio_2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.caso_estudio_2 to javafx.fxml;
    exports com.example.caso_estudio_2;
}