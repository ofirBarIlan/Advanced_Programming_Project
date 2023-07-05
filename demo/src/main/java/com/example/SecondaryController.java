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

public class SecondaryController {
    static ViewModel vm;

    @FXML
    Button startGameButton;

    @FXML 
    public Label roomNumLabel;

    @FXML
    public Label roomTitleLabel;

    @FXML
    ImageView waitingImage;
    
    public void init(ViewModel vm) {
        this.vm = vm;

        // Bind with roomNumLabel in vm
        // roomNumLabel.textProperty().bind(vm.roomNumLabel);
        startGameButton.managedProperty().bind(startGameButton.visibleProperty());
        startGameButton.setVisible(false);
                 

        // Check why doesn't work
        try {
            String root = System.getProperty("user.dir")+"\\src\\main\\";
            //System.out.println(root);
            InputStream stream = new FileInputStream(root+"resources\\Images\\waiting.JPG");            
            Image image = new Image(stream);
            waitingImage.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {

        vm.startGame();
        App.setRoot("primary");
        App.stage.setScene(App.scene1);
    }
}