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
    
    // Initializes the controller class.
    public void init(ViewModel vm) {
        this.vm = vm;

        startGameButton.managedProperty().bind(startGameButton.visibleProperty());
        startGameButton.setVisible(false);

        try {
            String root = System.getProperty("user.dir")+"\\src\\main\\";
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