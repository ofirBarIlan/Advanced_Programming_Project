package com.example;

import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import view_model.ViewModel;

public class ThirdController {
    ViewModel vm;
    String name;
    Scanner scanner;

    public void init(ViewModel vm){
        this.vm = vm;
        scanner = new Scanner(System.in);
    }

    @FXML
    private void switchToSecondaryAsHost() throws IOException {
        System.out.println("Enter name");
        name = scanner.nextLine();
        vm.startRoomFromModel(name);
        App.setRoot("secondary");
        App.stage.setScene(App.scene2);
    }
    @FXML
    private void switchToSecondaryAsGuest() throws IOException {
        System.out.println("Enter name");
        name = scanner.nextLine();
        System.out.println("Enter room num");
        String roomNum = scanner.nextLine();
        vm.joinRoomFromModel(Integer.parseInt(roomNum), 6100, name);
        App.setRoot("secondary");
        App.stage.setScene(App.scene2);
    }
}