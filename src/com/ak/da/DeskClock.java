/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ak.da;

import com.ak.da.model.IpInformation;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Muhi
 */
public class DeskClock extends Application {
    
    private Stage stage;
    private double intiX;
    private double intiY;
    
    @Override
    public void start(Stage primaryStage) {
        
        //create stage which has set stage style transparent
        primaryStage = new Stage(StageStyle.TRANSPARENT);
        //create root node of scene, i.e. group
        Group rootGroup = new Group();
        //create scene with set width, height and color
        Scene scene = new Scene(rootGroup, 400, 100, Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        
        primaryStage.show();
        stage = primaryStage;
        
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        final Text time = new Text("12:00:00 AM");
        time.setFont(Font.font(Font.getDefault().getFamily(),FontWeight.BOLD, 50));
        time.setEffect(new Lighting());
        time.setFill(Color.web("black", 0.87));
        time.setBoundsType(TextBoundsType.VISUAL);
        time.setEffect(new DropShadow(10, Color.BLACK));
        
        Button btn = new Button("x");
        btn.setOnAction((ActionEvent event) -> {
            stage.close();
        });
        btn.setTooltip(new Tooltip("Close"));
        
        CheckBox check = new CheckBox();
        check.setAlignment(Pos.CENTER);
        check.setSelected(true);
        check.setTooltip(new Tooltip("Allways on top"));
        
        check.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                stage.setAlwaysOnTop(newValue);
            }
        });
        
        VBox vBox = new VBox();
        vBox.setSpacing(2);
        vBox.getChildren().addAll(btn, check);
        
        hBox.setSpacing(5);
        hBox.getChildren().addAll(time, vBox);
        
        hBox.setOnMousePressed((MouseEvent event) -> {
            intiX = event.getSceneX() - stage.getX();
            intiY = event.getSceneY() - stage.getY();
        });
        
        hBox.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getSceneX() - intiX);
            stage.setY(event.getSceneY() - intiY);
        });
        
        hBox.setOnMouseEntered((MouseEvent event) ->{
            vBox.setVisible(true);
        });
        hBox.setOnMouseExited((MouseEvent event) ->{
            vBox.setVisible(false);
        });
        
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                
                time.setText(sdf.format(cal.getTime()));
            }
        }.start();
        
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                try {
                    URL whatismyip = new URL("http://freegeoip.net/json/");
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            whatismyip.openStream()));
                    
                    String gsonLine = in.readLine(); //you get the IP as a String

                    Gson gson = new GsonBuilder()
                            .disableHtmlEscaping()
                            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                            .setPrettyPrinting()
                            .serializeNulls()
                            .create();
                    IpInformation IPDetails= new Gson().fromJson(gsonLine, IpInformation.class);
                    
                    System.out.println(
                            IPDetails.toString() + IPDetails.getCountry_name()
                    );
                    
                } catch (MalformedURLException ex) {
                    
                } catch (IOException ex) {
                    
                }
            }
            
        }.start();
        
       rootGroup.getChildren().addAll(hBox);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
