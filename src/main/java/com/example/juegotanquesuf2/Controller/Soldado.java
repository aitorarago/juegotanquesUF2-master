package com.example.juegotanquesuf2.Controller;

import com.example.juegotanquesuf2.HelloApplication;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class Soldado {
    private int vida;
    private int dano;
    private ImageView image;
    private boolean impact;

    public TranslateTransition getTargetMotion() {
        return targetMotion;
    }

    private TranslateTransition targetMotion;
    private Scene scene;

    Soldado(int d){
        vida=100;
        dano=d;
        image= new ImageView(new Image(HelloApplication.class.getResource("soldado.gif").toExternalForm()));
        targetMotion = new TranslateTransition(Duration.seconds(2), getImage());
        targetMotion.setByX(500);
        targetMotion.setAutoReverse(true);
        targetMotion.setCycleCount(Animation.INDEFINITE);
        targetMotion.play();
    }




    public int getDano() {
        return dano;
    }
    public int getVida(){
        return vida;
    }
    public ImageView getImage() {
        return image;
    }
    public Circle disparar(double X,double Y,double proj){
        Circle c = new Circle(X,Y,proj);
        return c;
    }



    public void setVida(int vida) {
        this.vida = vida;
    }

    public void setImpact(boolean impact) {
        this.impact = impact;
    }

    public boolean isImpact() {
        return impact;
    }
}