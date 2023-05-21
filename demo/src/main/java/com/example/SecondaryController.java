package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import view_model.ViewModel;

public class SecondaryController {
    ViewModel vm;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
        App.stage.setScene(App.scene1);
    }
    public void init(ViewModel vm){
        this.vm = vm;
    }
}