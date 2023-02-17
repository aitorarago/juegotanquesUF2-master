package com.example.juegotanquesuf2;
import com.example.juegotanquesuf2.Controller.PlayGameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static Stage principalStage;
    static PlayGameController game=new PlayGameController();
    @Override
    public void start(Stage stage) throws Exception {
        principalStage=stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("introduction.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void cambiarEscena() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 600);
            principalStage.setScene(scene);
            game.jugar(principalStage,1,3);
        }

}
