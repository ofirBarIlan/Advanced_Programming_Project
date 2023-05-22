package com.example;

import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Label errorLabel;

    public void init(ViewModel vm) {
        this.vm = vm;
        // Bind the disable property of the buttons to the empty property of the nameTextField and roomNumberTextField
        guestButton.disableProperty().bind(nameTextField.textProperty().isEmpty());
        hostButton.disableProperty().bind(nameTextField.textProperty().isEmpty());
        
        // Hide the error label initially
        errorLabel.setVisible(false);
       
    }

    @FXML
    private void switchToSecondaryAsHost() throws IOException {
        String name = nameTextField.getText();
        vm.startRoomFromModel(name);
        App.setRoot("secondary");
        App.stage.setScene(App.scene2);
    }

    @FXML
    private void switchToSecondaryAsGuest() throws IOException {
        roomNumberLabel.setVisible(true); // Show the room number label
        roomNumberTextField.setVisible(true); // Show the room number text field

        String name = nameTextField.getText();
        String roomNum = roomNumberTextField.getText();
        
        // Check if the room number is valid (not empty and numeric)
        if (roomNum.isEmpty()) {
            errorLabel.setText("Empty room number");
            errorLabel.setVisible(true);
            return;
        } else if (!roomNum.matches("\\d+")) {
            errorLabel.setText("Room must be a valid number");
            errorLabel.setVisible(true);
            return;
        }
        
        errorLabel.setVisible(false);
        
        // Join the room
        name = nameTextField.getText();
        vm.joinRoomFromModel(Integer.parseInt(roomNum), 6100, name);
        App.setRoot("secondary");
        App.stage.setScene(App.scene2);

    }
}