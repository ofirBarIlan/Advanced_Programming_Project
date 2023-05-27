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

public class EndController {
    static ViewModel vm;

    @FXML
    Button exitButton;
    
    public void init(ViewModel vm) {
        this.vm = vm;

                
    }
       

    @FXML
    private void endGame() throws IOException {

       System.exit(0);
    }
}