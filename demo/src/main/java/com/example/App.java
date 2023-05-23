package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view_model.ViewModel;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene scene3;
    public static Scene scene2;
    public static Scene scene1;
    public static Stage stage;
    public static SecondaryController sc;
   
    
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoaderThird = new FXMLLoader(App.class.getResource("third.fxml"));
        scene3 = new Scene(fxmlLoaderThird.load(), 640, 480);
        ThirdController tc = fxmlLoaderThird.getController();
        FXMLLoader fxmlLoaderSecondary = new FXMLLoader(App.class.getResource("secondary.fxml"));
        scene2 = new Scene(fxmlLoaderSecondary.load(), 640, 480);
        sc = fxmlLoaderSecondary.getController();
        FXMLLoader fxmlLoaderPrimary = new FXMLLoader(App.class.getResource("primary.fxml"));
        scene1 = new Scene(fxmlLoaderPrimary.load(), 640, 480);
        PrimaryController pc = fxmlLoaderPrimary.getController();
        stage.setScene(scene3);
        stage.show();
        Model m = new Model(6200);
        
        ViewModel vm = new ViewModel(m, pc, sc, tc);
        if(pc!=null){
            pc.init(vm);
        }
        if(tc!=null){
            tc.init(vm);
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene2.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();

    }

    public static void main(String[] args) {
        launch();
    }

}