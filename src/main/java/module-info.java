module com.example.juegotanquesuf2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.juegotanquesuf2 to javafx.fxml;
    exports com.example.juegotanquesuf2;
    exports com.example.juegotanquesuf2.Controller;
    opens com.example.juegotanquesuf2.Controller to javafx.fxml, javafx.media;
}