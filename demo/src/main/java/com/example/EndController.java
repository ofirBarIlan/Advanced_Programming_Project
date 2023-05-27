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
    
    public void init(ViewModel vm) {
        this.vm = vm;


                
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