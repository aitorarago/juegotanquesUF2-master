package com.example.juegotanquesuf2.Controller;

import com.example.juegotanquesuf2.HelloApplication;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Helicopter {
    private ImageView imageView;
    private TranslateTransition transition;
    private int vidas=5;

    public Helicopter(double startX, double startY) {
        imageView = new ImageView(new Image(HelloApplication.class.getResource("helicopt.gif").toExternalForm()));
        imageView.setX(startX);
        imageView.setY(startY);

        transition = new TranslateTransition(Duration.seconds(2), imageView);
        transition.setByX(700);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();
        Path path = new Path();
        path.getElements().add(new MoveTo(200, 200));
        path.getElements().add(new ArcTo(100, 100, 0, 100, 200, false, false));
// Crea la animación que sigue la ruta
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(4));
        pathTransition.setPath(path);
        pathTransition.setNode(getImageView());
        pathTransition.setCycleCount(Animation.INDEFINITE);
    }

    public ImageView getImageView() {
        return imageView;
    }


    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getVidas() {
        return vidas;
    }

    public Circle disparar(double x, double y, double projectileRadius) {
        Circle c = new Circle(x,y,projectileRadius);
        return c;
    }
}