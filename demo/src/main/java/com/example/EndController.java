package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import view_model.ViewModel;

import javafx.scene.text.Font;

public class EndController {
    static ViewModel vm;

    @FXML
    Button exitButton;

    @FXML
    Label scoreLabel;

    @FXML
    Label nameLabel; 
    
    @FXML
    BorderPane rootPane;

    public void init(ViewModel vm) {
        this.vm = vm;

        String root = System.getProperty("user.dir")+"\\src\\main\\";
        InputStream stream;
        try {
            stream = new FileInputStream(root+"resources\\Images\\backgroundImage.jpeg");            
            Image img = new Image(stream);
            BackgroundImage backgroundImage = new BackgroundImage(img,null,null, null, null);
            Background background = new Background(backgroundImage);
            rootPane.setBackground(background);           

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }              
                
    }
       
    public void start(String name, String score){

    }

    @FXML
    private void endGameOnAction() throws IOException {

       System.exit(0);
    }
    
    
    public void endGame(String name, String score) throws IOException {
        App.setRoot("gameOver");
        App.stage.setScene(App.scene4);
        scoreLabel.setText(score);
        nameLabel.setText(name);        
    }
}