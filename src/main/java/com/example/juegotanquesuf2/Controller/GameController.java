package com.example.juegotanquesuf2.Controller;

import com.example.juegotanquesuf2.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;

    public class GameController {
        @FXML
        private Button btnplay;
        @FXML
        private Button btninstruct;
        @FXML
        private Button btnexit;

        HelloApplication helloApplication = new HelloApplication();

        public void mostrarInstrucciones(){
            btninstruct.setOnAction(actionEvent -> {
                Alert inst = new Alert(Alert.AlertType.INFORMATION);
                inst.setWidth(400);
                inst.setHeight(400);
                inst.setTitle("INSTRUCCIONS D'US: ");
                inst.setHeaderText("GUERRA DE TANQUES");
                inst.setContentText("""
El objetivo del juego es defender a toda costa tu tanque
de los enemigos, intenta que no se acerquen a tu cañon 
para seguir sumando puntos. Dispones de 3 vidas.""");
                inst.show();
            });
        }

        public void salir(){
            btnexit.setOnAction(e -> HelloApplication.principalStage.close());
        }

        public void startPlay() {
            btnplay.setOnAction(e -> {
                try {
                    HelloApplication.cambiarEscena();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        };

    }

