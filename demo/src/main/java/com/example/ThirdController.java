package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import view_model.ViewModel;

public class ThirdController {

    private ViewModel vm;

    @FXML
    private Label roomNumberLabel;
    @FXML
    private TextField roomNumberTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button guestButton;
    @FXML
    private Button hostButton;
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label portNumberLabel;
    @FXML
    private TextField portNumberTextField;

    @FXML
    private BorderPane rootPane;

    public void init(ViewModel vm) {
        this.vm = vm;
        // Bind the disable property of the buttons to the empty property of the nameTextField and roomNumberTextField
        guestButton.disableProperty().bind(nameTextField.textProperty().isEmpty());
        hostButton.disableProperty().bind(nameTextField.textProperty().isEmpty());
        singlePlayerButton.disableProperty().bind(nameTextField.textProperty().isEmpty());   

        
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

    @FXML
    private void switchToSecondaryAsHost() throws IOException {
        // Show port number label+text field
        portNumberLabel.setVisible(true);
        portNumberTextField.setVisible(true);

        String name = nameTextField.getText();
        String portNum = portNumberTextField.getText();

        // Check if the port number is valid (not empty and numeric)
        if (portNum.isEmpty()) {
            errorLabel.setText("Empty port number");
            errorLabel.setVisible(true);
            return;
        } else if (!portNum.matches("\\d+")) {
            errorLabel.setText("Port must be a valid number");
            errorLabel.setVisible(true);
            return;
        }
        
        
        errorLabel.setVisible(false);

        // Join the room
        if(vm.startRoomFromModel(name, Integer.parseInt(portNumberTextField.getText())) > 0){
                        
            App.setRoot("secondary");
            App.stage.setScene(App.scene2);
        }
        else{
            errorLabel.setText("Invalid data, try again");
            errorLabel.setVisible(true);
        }
        
    }

    @FXML
    private void switchToSecondaryAsSinglePlayer() throws IOException {
        String name = nameTextField.getText();
        vm.startRoomFromModel(name, 6000);
        
        vm.startGame();
        App.setRoot("primary");
        App.stage.setScene(App.scene1);
        
    }

    @FXML
    private void switchToSecondaryAsGuest() throws IOException {
        // Show room number label+text field
        // roomNumberLabel.setVisible(true); 
        // roomNumberTextField.setVisible(true);

        // Show port number label+text field
        portNumberLabel.setVisible(true);
        portNumberTextField.setVisible(true);

        String name = nameTextField.getText();
        // String roomNum = roomNumberTextField.getText();
        String portNum = portNumberTextField.getText();
        
        
        
        // Check if the room number is valid (not empty and numeric)
        // if (roomNum.isEmpty()) {
        //     errorLabel.setText("Empty room number");
        //     errorLabel.setVisible(true);
        //     return;
        // } else if (!roomNum.matches("\\d+")) {
        //     errorLabel.setText("Room must be a valid number");
        //     errorLabel.setVisible(true);
        //     return;
        // }
        // Check if the port number is valid (not empty and numeric)
        if (portNum.isEmpty()) {
            errorLabel.setText("Empty port number");
            errorLabel.setVisible(true);
            return;
        } else if (!portNum.matches("\\d+")) {
            errorLabel.setText("Port must be a valid number");
            errorLabel.setVisible(true);
            return;
        }
        
        
        errorLabel.setVisible(false);
        
        // Join the room
        name = nameTextField.getText();
        if(vm.joinRoomFromModel(Integer.parseInt("0"), Integer.parseInt(portNumberTextField.getText()), name)){
            
            
            App.setRoot("secondary");
            App.stage.setScene(App.scene2);
        }
        else{
            errorLabel.setText("Invalid data, try again");
            errorLabel.setVisible(true);
        }

        

    }

    @FXML
    private void switchToSecondaryLoadGameAsHost() throws IOException{
        // Show port number label+text field
        portNumberLabel.setVisible(true);
        portNumberTextField.setVisible(true);

        // Show room number label+text field
        roomNumberLabel.setVisible(true); 
        roomNumberTextField.setVisible(true);

        String name = nameTextField.getText();
        String portNum = portNumberTextField.getText();

        // Check if the port number is valid (not empty and numeric)
        if (portNum.isEmpty()) {
            errorLabel.setText("Empty port number");
            errorLabel.setVisible(true);
            return;
        } else if (!portNum.matches("\\d+")) {
            errorLabel.setText("Port must be a valid number");
            errorLabel.setVisible(true);
            return;
        }
        
        
        errorLabel.setVisible(false);

        // Join the room
        if(vm.startRoomFromModel(name, Integer.parseInt(portNumberTextField.getText())) > 0){
                        
            App.setRoot("secondary");
            App.stage.setScene(App.scene2);
        }
        else{
            errorLabel.setText("Invalid data, try again");
            errorLabel.setVisible(true);
        }
    }

}