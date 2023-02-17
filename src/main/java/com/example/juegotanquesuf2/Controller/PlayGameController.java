package com.example.juegotanquesuf2.Controller;
import com.example.juegotanquesuf2.HelloApplication;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public class PlayGameController {
    @FXML
    private BorderPane scenaid;
    @FXML
    private Label oleadaid;
    @FXML
    private Label vidasid;
    @FXML
    private Label puntuacionid;
    private int oleada ;
    private int puntuacion = 0;
    private int vidas =3;
    private final double width = 800;
    private final double height = 800;

    private final double targetRadius = 35;
    private final double projectileRadius = 6;

    private final double weaponLength = 25;
    private final double tamqueratius = 22;

    private final double weaponX = width / 2;
    private final double weaponStartY = height;
    private final double weaponEndY = height - weaponLength;

    private final double targetStartX = targetRadius;
    private final double targetY = targetRadius * 2;
    private Point2D targetLocation;
    private Point2D tankCenter;
    private Timeline timeline;
    private String sonido = HelloApplication.class.getResource("disparo.mp3").toExternalForm();
    private Media sound = new Media(sonido);
    private MediaPlayer audioClip = new MediaPlayer(sound);
    private String sonidodos = HelloApplication.class.getResource("sonidofondo.mp3").toExternalForm();
    private Media fondo = new Media(sonidodos);
    private MediaPlayer fondoo = new MediaPlayer(fondo);
    private String explosionn = HelloApplication.class.getResource("explosion.mp3").toExternalForm();
    private Media exp = new Media(explosionn);
    private MediaPlayer explosionnnn = new MediaPlayer(exp);


    public void jugar(Stage stage, int ol,int vid) {
        oleada=ol;
        oleadaid=new Label();
        vidasid=new Label();
        puntuacionid=new Label();
        oleadaid.setText("Oledada: "+oleada);
        vidasid.setText("Vidas: "+vidas);
        puntuacionid.setText(" Puntuación: "+puntuacion);
        scenaid = new BorderPane();
        scenaid.setTop(oleadaid);
        audioClip.setVolume(70);
        fondoo.play();


        switch (oleada) {
            case 1 -> {nivel1(stage);vidas=vid;}
            case 2 -> {nivel2(stage);vidas=vid;}
            case 3 -> {nivel3(stage);vidas=vid;}
        }

    }

    private void nivel3(Stage stage) {
        Group root = new Group();
        ImageView backgroundView = new ImageView(new Image(HelloApplication.class.getResource("fondo3.jpg").toExternalForm()));
        backgroundView.setFitHeight(height);
        backgroundView.setFitWidth(width);
        root.getChildren().add(0, backgroundView);

        ArrayList<Helicopter> helicopters = new ArrayList<>();
        for (int i = 0; i <4; i++) {
            Helicopter helicopter = new Helicopter(i*200,targetY);
            if(i%2==0)helicopter.getImageView().setY(targetY);
            else helicopter.getImageView().setY(targetY+50);
            helicopter.getImageView().setFitHeight(targetRadius * 5);
            helicopter.getImageView().setFitWidth(targetRadius * 5);
            helicopters.add(helicopter);
        }


        Line weapon = new Line(weaponX, weaponStartY, weaponX, weaponEndY);
        weapon.setStrokeWidth(15);
        Circle base = new Circle(weaponX, weaponStartY, tamqueratius);
        base.setFill(Color.FORESTGREEN);
        Group tanque = new Group(weapon,base);


        TranslateTransition weaponMove = new TranslateTransition();
        weaponMove.setDuration(Duration.seconds(1));
        weaponMove.setNode(tanque);
        Label label = new Label("Puntuacion: " + puntuacion);
        label.setTextFill(Color.WHITESMOKE);
        label.setFont(new Font(24));
        label.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(label);

        Timeline timeline = new Timeline(
                // 0.017 ~= 60 FPS
                new KeyFrame(Duration.seconds(1), ae -> {
                    weaponMove.play();
                    int random = (int) (Math.random() * helicopters.size());
                    Point2D sold = helicopters.get(random).getImageView().localToParent(helicopters.get(random).getImageView().getX(), targetY);
                    Circle proy = helicopters.get(random).disparar(sold.getX(), sold.getY(), projectileRadius);
                    TranslateTransition shot = new TranslateTransition(Duration.seconds(1), proy);
                    shot.setToY(+height); // Se moverá hacia arriba en Y=0
                    audioClip.play();
                    shot.setOnFinished(event -> root.getChildren().remove(proy));
                    BooleanBinding hits = Bindings.createBooleanBinding(() -> {
                        Bounds tankBounds = weaponMove.getNode().getBoundsInParent();
                        Bounds projectileBounds = proy.getBoundsInParent();
                        Point2D tankCenter = new Point2D(tankBounds.getMinX() + tankBounds.getWidth() / 2, tankBounds.getMinY() + tankBounds.getHeight() / 2);
                        Point2D projectileCenter = new Point2D(projectileBounds.getMinX() + projectileBounds.getWidth() / 2, projectileBounds.getMinY() + projectileBounds.getHeight() / 2);
                        double distance = tankCenter.distance(projectileCenter);
                        double maxDistance = (tankBounds.getWidth() + tankBounds.getHeight() + proy.getRadius()) / 2;
                        return distance <= maxDistance;
                    }, tanque.translateXProperty(), tanque.translateYProperty(), proy.translateXProperty(), proy.translateYProperty());

                    hits.addListener((obs, wasHit, isNowHit) -> {
                        if (isNowHit) {
                            System.out.println("hit-1");
                            if(vidas==0){
                                weaponMove.stop();
                                ImageView explosion = new ImageView(new Image(HelloApplication.class.getResource("explosion.gif").toExternalForm()));
                                explosion.setX(tankCenter.getX()-tamqueratius);
                                explosion.setY(targetY+600);
                                root.getChildren().add(explosion);
                                explosionnnn.play();
                                root.getChildren().remove(tanque);
                                FadeTransition ft = new FadeTransition(Duration.seconds(2), explosion);
                                ft.setFromValue(1.0);
                                ft.setToValue(0.0);
                                ft.setOnFinished(event -> root.getChildren().remove(explosion));
                                ft.play();
                                if(!root.getChildren().contains(explosion)){
                                    Button seguir = new Button();
                                    seguir.setPrefWidth(90);
                                    seguir.setText("CONTINUAR");
                                    seguir.setOnAction(actionEvent -> jugar(stage, 1,3));

                                    Stage stage1 = new Stage();
                                    Label label1= new Label("Te has quedado sin vidas, empezar de nuevo?");
                                    label1.setTextAlignment(TextAlignment.CENTER);

                                    VBox vBox = new VBox(label1, seguir);
                                    vBox.setAlignment(Pos.CENTER);
                                    vBox.setPadding(new Insets(10, 20, 20, 20));
                                    Scene scene1 = new Scene(vBox);
                                    stage1.setScene(scene1);
                                    stage1.setTitle("OLEADA SUPERADA");
                                    stage1.show();
                                }
                            }
                            else vidas=vidas-1;
                        }
                    });
                    root.getChildren().add(proy);
                    shot.play();
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        root.getChildren().add(tanque);
        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                weaponMove.setByX(-100);
                weaponMove.play();
            }
            if (e.getCode() == KeyCode.RIGHT) {
                weaponMove.setByX(+100);
                weaponMove.play();
            }
            if (e.getCode() == KeyCode.SPACE) {
                weaponMove.play();
                audioClip.play();
                Point2D weaponEnd = tanque.localToParent(weaponX, weaponEndY);

                Circle projectile = new Circle(weaponEnd.getX(), weaponEnd.getY(), projectileRadius);

                TranslateTransition shot = new TranslateTransition(Duration.seconds(1), projectile);
                shot.setToY(-height); // Se moverá hacia arriba en Y=0
                shot.setOnFinished(event -> root.getChildren().remove(projectile));

                List<BooleanBinding> hits = new ArrayList<>();
                for (Helicopter helicopter1 : helicopters) {
                    BooleanBinding hit = Bindings.createBooleanBinding(() -> {
                        targetLocation = helicopter1.getImageView().localToParent(helicopter1.getImageView().getX(), targetY);
                        Point2D projectileLocation = projectile.localToParent(weaponEnd);
                        return (targetLocation.distance(projectileLocation) < targetRadius + projectileRadius);
                    }, projectile.translateXProperty(), projectile.translateYProperty());
                    hit.addListener((obs, wasHit, isNowHit) -> {
                        if (isNowHit) {
                            if(helicopter1.getVidas()==0){
                                System.out.println("Hit");
                                root.getChildren().remove(projectile);
                                shot.stop();
                                ImageView explosion = new ImageView(new Image(HelloApplication.class.getResource("explosion.gif").toExternalForm()));
                                explosion.setY(helicopter1.getImageView().getLayoutY());
                                explosion.setX(targetLocation.getX()-tamqueratius);
                                root.getChildren().add(explosion);
                                explosionnnn.play();
                                FadeTransition ft = new FadeTransition(Duration.seconds(2), explosion);
                                ft.setFromValue(1.0);
                                ft.setToValue(0.0);
                                ft.setOnFinished(event -> root.getChildren().remove(explosion));
                                ft.play();
                                root.getChildren().remove(helicopter1.getImageView());
                                puntuacion = puntuacion + 50;
                                label.setText("Puntuación: " + puntuacion);
                                helicopters.remove(helicopter1);
                            }
                            else{
                                helicopter1.setVidas(helicopter1.getVidas()-1);
                                System.out.printf("hit -1 vida"+helicopter1);
                            }
                            if(helicopters.size()==0){

                                Button seguir = new Button();
                                seguir.setPrefWidth(90);
                                seguir.setText("CONTINUAR");
                                seguir.setOnAction(actionEvent -> jugar(stage, 1,3));

                                Stage stage1 = new Stage();
                                Label label1= new Label("OLEADA FINALIZADA, HAS GANADO,\n PULSA CONTINUAR PARA VOLVER A LA OLEADA1");
                                label1.setTextAlignment(TextAlignment.CENTER);

                                VBox vBox = new VBox(label1, seguir);
                                vBox.setAlignment(Pos.CENTER);
                                vBox.setPadding(new Insets(10, 20, 20, 20));
                                Scene scene1 = new Scene(vBox);
                                stage1.setScene(scene1);
                                stage1.setTitle("HAS GANADO LA BATALLA, PERO NO LA GUERRA");
                                stage1.show();

                            }
                        }
                    });
                    hits.add(hit);
                }

                root.getChildren().add(projectile);
                stage.setScene(scene);
                shot.play();
            }
        });
        helicopters.forEach(helicopter1 ->  root.getChildren().add(1,helicopter1.getImageView()));
        stage.setScene(scene);
        stage.show();
    }

    private void nivel2(Stage stage) {
        Group root = new Group();
        ImageView backgroundView = new ImageView(new Image(HelloApplication.class.getResource("fondo2.png").toExternalForm()));
        backgroundView.setFitHeight(height);
        backgroundView.setFitWidth(width);
        root.getChildren().add(0, backgroundView);


        ArrayList<Soldado> soldados = new ArrayList<>();
        for (int i = 0; i < oleada * 4; i++) {
            Soldado soldado = new Soldado(i);
            soldado.getImage().setX(targetStartX + (i * 70));
            if(i%2==0)soldado.getImage().setY(targetY);
            else soldado.getImage().setY(targetY+50);
            soldado.getImage().setFitHeight(targetRadius * 2);
            soldado.getImage().setFitWidth(targetRadius * 2);
            soldados.add(soldado);
        }


        Line weapon = new Line(weaponX, weaponStartY, weaponX, weaponEndY);
        weapon.setStrokeWidth(15);
        Circle base = new Circle(weaponX, weaponStartY, tamqueratius);
        base.setFill(Color.FORESTGREEN);
        Group tanque = new Group(weapon,base);


        TranslateTransition weaponMove = new TranslateTransition();
        weaponMove.setDuration(Duration.seconds(1));
        weaponMove.setNode(tanque);
        Label label = new Label("Puntuacion: " + puntuacion);
        label.setTextFill(Color.WHITESMOKE);
        label.setFont(new Font(24));
        label.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(label);
        Timeline timeline = new Timeline(
                // 0.017 ~= 60 FPS
                new KeyFrame(Duration.seconds(1), ae -> {
                    weaponMove.play();
                    int random = (int) (Math.random() * soldados.size());
                    Point2D sold = soldados.get(random).getImage().localToParent(soldados.get(random).getImage().getX(), targetY);
                    Circle proy = soldados.get(random).disparar(sold.getX(), sold.getY(), projectileRadius);
                    TranslateTransition shot = new TranslateTransition(Duration.seconds(1), proy);
                    shot.setToY(+height); // Se moverá hacia arriba en Y=0
                    audioClip.play();
                    shot.setOnFinished(event -> root.getChildren().remove(proy));
                    BooleanBinding hits = Bindings.createBooleanBinding(() -> {
                        Bounds tankBounds = weaponMove.getNode().getBoundsInParent();
                        Bounds projectileBounds = proy.getBoundsInParent();
                        Point2D tankCenter = new Point2D(tankBounds.getMinX() + tankBounds.getWidth() / 2, tankBounds.getMinY() + tankBounds.getHeight() / 2);
                        Point2D projectileCenter = new Point2D(projectileBounds.getMinX() + projectileBounds.getWidth() / 2, projectileBounds.getMinY() + projectileBounds.getHeight() / 2);
                        double distance = tankCenter.distance(projectileCenter);
                        double maxDistance = (tankBounds.getWidth() + tankBounds.getHeight() + proy.getRadius()) / 2;
                        return distance <= maxDistance;
                    }, tanque.translateXProperty(), tanque.translateYProperty(), proy.translateXProperty(), proy.translateYProperty());

                    hits.addListener((obs, wasHit, isNowHit) -> {
                        if (isNowHit) {
                            System.out.println("hit-1");
                            if(vidas==0){
                                weaponMove.stop();
                                parar();
                                ImageView explosion = new ImageView(new Image(HelloApplication.class.getResource("explosion.gif").toExternalForm()));
                                explosion.setX(tankCenter.getX()-tamqueratius);
                                explosion.setY(targetY+600);
                                explosionnnn.play();
                                root.getChildren().add(explosion);
                                root.getChildren().remove(tanque);
                                FadeTransition ft = new FadeTransition(Duration.seconds(2), explosion);
                                ft.setFromValue(1.0);
                                ft.setToValue(0.0);
                                ft.setOnFinished(event -> root.getChildren().remove(explosion));
                                ft.play();
                                    Button seguir = new Button();
                                    seguir.setPrefWidth(90);
                                    seguir.setText("CONTINUAR");
                                    seguir.setOnAction(actionEvent -> jugar(stage, 1,3));

                                    Stage stage1 = new Stage();
                                    Label label1= new Label("Te has quedado sin vidas, empezar de nuevo?");
                                    label1.setTextAlignment(TextAlignment.CENTER);

                                    VBox vBox = new VBox(label1, seguir);
                                    vBox.setAlignment(Pos.CENTER);
                                    vBox.setPadding(new Insets(10, 20, 20, 20));
                                    Scene scene1 = new Scene(vBox);
                                    stage1.setScene(scene1);
                                    stage1.setTitle("OLEADA SUPERADA");
                                    stage1.show();
                            }
                            else vidas=vidas-1;
                        }
                    });
                    root.getChildren().add(proy);
                    shot.play();
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();



        root.getChildren().add(tanque);
        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                weaponMove.setByX(-100);
                weaponMove.play();
            }
            if (e.getCode() == KeyCode.RIGHT) {
                weaponMove.setByX(+100);
                weaponMove.play();
            }
            if (e.getCode() == KeyCode.SPACE) {
                weaponMove.play();
                audioClip.play();
                Point2D weaponEnd = tanque.localToParent(weaponX, weaponEndY);

                Circle projectile = new Circle(weaponEnd.getX(), weaponEnd.getY(), projectileRadius);

                TranslateTransition shot = new TranslateTransition(Duration.seconds(1), projectile);
                shot.setToY(-height); // Se moverá hacia arriba en Y=0
                shot.setOnFinished(event -> root.getChildren().remove(projectile));

                List<BooleanBinding> hits = new ArrayList<>();
                for (Soldado soldado : soldados) {
                    BooleanBinding hit = Bindings.createBooleanBinding(() -> {
                        targetLocation = soldado.getImage().localToParent(soldado.getImage().getX(), targetY);
                        Point2D projectileLocation = projectile.localToParent(weaponEnd);
                        return (targetLocation.distance(projectileLocation) < targetRadius + projectileRadius);
                    }, projectile.translateXProperty(), projectile.translateYProperty());
                    hit.addListener((obs, wasHit, isNowHit) -> {
                        if (isNowHit) {
                            if(soldado.isImpact()){
                                System.out.println("Hit");
                                root.getChildren().remove(projectile);
                                shot.stop();
                                soldado.getTargetMotion().stop();
                                ImageView explosion = new ImageView(new Image(HelloApplication.class.getResource("explosion.gif").toExternalForm()));
                                explosion.setY(soldado.getTargetMotion().getByY());
                                explosion.setX(targetLocation.getX()-targetRadius);
                                explosionnnn.play();
                                root.getChildren().add(explosion);
                                FadeTransition ft = new FadeTransition(Duration.seconds(2), explosion);
                                ft.setFromValue(1.0);
                                ft.setToValue(0.0);
                                ft.setOnFinished(event -> root.getChildren().remove(explosion));
                                ft.play();
                                root.getChildren().remove(soldado.getImage());
                                puntuacion = puntuacion + 50;
                                label.setText("Puntuación: " + puntuacion);
                                soldados.remove(soldado);
                            }
                            else soldado.setImpact(true);





                            if(soldados.size()==0){
                                timeline.stop();
                                Button seguir = new Button();
                                seguir.setPrefWidth(90);
                                seguir.setText("CONTINUAR");
                                seguir.setOnAction(actionEvent -> jugar(stage, ++oleada,3));

                                Stage stage1 = new Stage();
                                Label label1= new Label("OLEADA FINALIZADA, DESEAS CONTINUAR?");
                                label1.setTextAlignment(TextAlignment.CENTER);

                                VBox vBox = new VBox(label1, seguir);
                                vBox.setAlignment(Pos.CENTER);
                                vBox.setPadding(new Insets(10, 20, 20, 20));
                                Scene scene1 = new Scene(vBox);
                                stage1.setScene(scene1);
                                stage1.setTitle("OLEADA SUPERADA");
                                stage1.show();
                            }
                        }
                    });
                    hits.add(hit);
                }
                root.getChildren().add(projectile);
                stage.setScene(scene);
                shot.play();
            }
        });
        soldados.forEach(soldado ->  root.getChildren().add(1,soldado.getImage()));
        stage.setScene(scene);
        stage.show();
    }


    private void nivel1(Stage stage) {
        Group root = new Group();
        ImageView backgroundView = new ImageView(new Image(HelloApplication.class.getResource("fondo.jpg").toExternalForm()));
        backgroundView.setFitHeight(height);
        backgroundView.setFitWidth(width);
        root.getChildren().add(0, backgroundView);

        ArrayList<Soldado> soldados = new ArrayList<>();
        for (int i = 0; i < oleada * 4; i++) {
            Soldado soldado = new Soldado(i);
            soldado.getImage().setX(targetStartX + (i * 100));
            soldado.getImage().setY(targetY);
            soldado.getImage().setFitHeight(targetRadius * 2);
            soldado.getImage().setFitWidth(targetRadius * 2);
            soldados.add(soldado);
        }


        Line weapon = new Line(weaponX, weaponStartY, weaponX, weaponEndY);
        weapon.setStrokeWidth(15);
        Circle base = new Circle(weaponX, weaponStartY, tamqueratius);
        base.setFill(Color.FORESTGREEN);
        Group tanque = new Group(weapon,base);


        TranslateTransition weaponMove = new TranslateTransition();
        weaponMove.setDuration(Duration.seconds(1));
        weaponMove.setNode(tanque);

        Label label = new Label("Puntuacion: " + puntuacion);
        label.setTextFill(Color.WHITESMOKE);
        label.setFont(new Font(24));
        label.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(label);
        root.getChildren().add(tanque);


        timeline = new Timeline(
                // 0.017 ~= 60 FPS
                new KeyFrame(Duration.seconds(1), ae -> {
                    weaponMove.play();
                    int random = (int) (Math.random() * soldados.size());
                    Point2D sold = soldados.get(random).getImage().localToParent(soldados.get(random).getImage().getX(), targetY);
                    Circle proy = soldados.get(random).disparar(sold.getX(), sold.getY(), projectileRadius);
                    TranslateTransition shot = new TranslateTransition(Duration.seconds(1), proy);
                    shot.setToY(+height); // Se moverá hacia arriba en Y=0
                    audioClip.play();
                    shot.setOnFinished(event -> root.getChildren().remove(proy));
                    BooleanBinding hits = Bindings.createBooleanBinding(() -> {
                        Bounds tankBounds = weaponMove.getNode().getBoundsInParent();
                        Bounds projectileBounds = proy.getBoundsInParent();
                        tankCenter = new Point2D(tankBounds.getMinX() + tankBounds.getWidth() / 2, tankBounds.getMinY() + tankBounds.getHeight() / 2);
                        Point2D projectileCenter = new Point2D(projectileBounds.getMinX() + projectileBounds.getWidth() / 2, projectileBounds.getMinY() + projectileBounds.getHeight() / 2);
                        double distance = tankCenter.distance(projectileCenter);
                        double maxDistance = (tankBounds.getWidth() + tankBounds.getHeight() + proy.getRadius()) / 2;
                        return distance <= maxDistance;
                    }, tanque.translateXProperty(), tanque.translateYProperty(), proy.translateXProperty(), proy.translateYProperty());

                    hits.addListener((obs, wasHit, isNowHit) -> {
                        if (isNowHit) {
                            System.out.println("hit-1");
                            if(vidas==0){
                                weaponMove.stop();

                                ImageView explosion = new ImageView(new Image(HelloApplication.class.getResource("explosion.gif").toExternalForm()));
                                explosion.setX(tankCenter.getX()-tamqueratius);
                                explosion.setY(targetY+600);
                                root.getChildren().add(explosion);
                                explosionnnn.play();
                                root.getChildren().remove(tanque);
                                FadeTransition ft = new FadeTransition(Duration.seconds(2), explosion);
                                ft.setFromValue(1.0);
                                ft.setToValue(0.0);
                                ft.setOnFinished(event -> root.getChildren().remove(explosion));
                                ft.play();
                                    Button seguir = new Button();
                                    seguir.setPrefWidth(90);
                                    seguir.setText("CONTINUAR");
                                    seguir.setOnAction(actionEvent -> jugar(stage, 1,3));

                                    Stage stage1 = new Stage();
                                    Label label1= new Label("Te has quedado sin vidas, empezar de nuevo?");
                                    label1.setTextAlignment(TextAlignment.CENTER);

                                    VBox vBox = new VBox(label1, seguir);
                                    vBox.setAlignment(Pos.CENTER);
                                    vBox.setPadding(new Insets(10, 20, 20, 20));
                                    Scene scene1 = new Scene(vBox);
                                    stage1.setScene(scene1);
                                    stage1.setTitle("OLEADA SUPERADA");
                                    stage1.show();
                            }
                            else vidas=vidas-1;
                        }
                    });
                    root.getChildren().add(proy);
                    shot.play();
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                weaponMove.setByX(-100);
                weaponMove.play();
            }
            if (e.getCode() == KeyCode.RIGHT) {
                weaponMove.setByX(+100);
                weaponMove.play();
            }
            if (e.getCode() == KeyCode.SPACE) {
                weaponMove.play();
                audioClip.play();
                Point2D weaponEnd = tanque.localToParent(weaponX, weaponEndY);

                Circle projectile = new Circle(weaponEnd.getX(), weaponEnd.getY(), projectileRadius);

                TranslateTransition shot = new TranslateTransition(Duration.seconds(1), projectile);
                shot.setToY(-height); // Se moverá hacia arriba en Y=0
                shot.setOnFinished(event -> root.getChildren().remove(projectile));
                List<BooleanBinding> hits = new ArrayList<>();
                for (Soldado soldado : soldados) {
                    BooleanBinding hit = Bindings.createBooleanBinding(() -> {
                        targetLocation = soldado.getImage().localToParent(soldado.getImage().getX(), targetY);
                        Point2D projectileLocation = projectile.localToParent(weaponEnd);
                        return (targetLocation.distance(projectileLocation) < targetRadius + projectileRadius);
                    }, projectile.translateXProperty(), projectile.translateYProperty());
                    hit.addListener((obs, wasHit, isNowHit) -> {
                        if (isNowHit) {
                            System.out.println("Hit");
                            root.getChildren().remove(projectile);
                            shot.stop();
                            soldado.getTargetMotion().stop();
                            ImageView explosion = new ImageView(new Image(HelloApplication.class.getResource("explosion.gif").toExternalForm()));
                            explosion.setY(soldado.getTargetMotion().getByY());
                            explosion.setX(targetLocation.getX()-targetRadius);
                            root.getChildren().add(explosion);
                            explosionnnn.play();
                            FadeTransition ft = new FadeTransition(Duration.seconds(2), explosion);
                            ft.setFromValue(1.0);
                            ft.setToValue(0.0);
                            ft.setOnFinished(event -> root.getChildren().remove(explosion));
                            ft.play();
                            root.getChildren().remove(soldado.getImage());
                            puntuacion = puntuacion + 50;
                            label.setText("Puntuación: " + puntuacion);
                            soldados.remove(soldado);

                            if(soldados.size()==0){
                                timeline.stop();
                                Button seguir = new Button();
                                seguir.setPrefWidth(90);
                                seguir.setText("CONTINUAR");
                                seguir.setOnAction(actionEvent -> jugar(stage, ++oleada,3));

                                Stage stage1 = new Stage();
                                Label label1= new Label("OLEADA FINALIZADA, DESEAS CONTINUAR?");
                                label1.setTextAlignment(TextAlignment.CENTER);

                                VBox vBox = new VBox(label1, seguir);
                                vBox.setAlignment(Pos.CENTER);
                                vBox.setPadding(new Insets(10, 20, 20, 20));
                                Scene scene1 = new Scene(vBox);
                                stage1.setScene(scene1);
                                stage1.setTitle("OLEADA SUPERADA");
                                stage1.show();
                            }
                        }
                    });
                    hits.add(hit);
                }
                root.getChildren().add(projectile);
                stage.setScene(scene);
                shot.play();
            }
        });
        soldados.forEach(soldado ->  root.getChildren().add(1,soldado.getImage()));
        stage.setScene(scene);
        stage.show();
    }
    public void parar(){
        timeline.stop();
    }
}