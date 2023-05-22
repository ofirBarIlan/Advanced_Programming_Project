package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import view_model.ViewModel;

// added observer
public class PrimaryController extends Observable implements Initializable, Observer {
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
        App.stage.setScene(App.scene2);
    }
    private static final int TW = 27;
    private static final int DW = 28;
    private static final int TL = 29;
    private static final int DL = 30;
    private static final int E = 0;
    private static final int S = 31;
    boolean mousePushed;
    ViewModel vm;
    
    // Replace later with real one
    int[][] boardData = {
        {TW,E,E,DL,E,E,E,TW,E,E,E,DL,E,E,TW},
        {E,DW,E,E,E,TL,E,E,E,TL,E,E,E,DW,E},
        {E,E,DW,E,E,E,DL,E,DL,E,E,E,DW,E,E},
        {DL,E,E,DW,E,E,E,DL,E,E,E,DW,E,E,DL},
        {E,E,E,E,DW,E,E,E,E,E,DW,E,E,E,E},
        {E,TL,E,E,E,TL,E,E,E,TL,E,E,E,TL,E},
        {E,E,DL,E,E,E,DL,E,DL,E,E,E,DL,E,E},
        {TW,E,E,DL,E,E,E,S,E,E,E,DL,E,E,TW},
        {E,E,DL,E,E,E,DL,E,DL,E,E,E,DL,E,E},
        {E,TL,E,E,E,TL,E,E,E,TL,E,E,E,TL,E},
        {E,E,E,E,DW,E,E,E,E,E,DW,E,E,E,E},
        {DL,E,E,DW,E,E,E,DL,E,E,E,DW,E,E,DL},
        {E,E,DW,E,E,E,DL,E,DL,E,E,E,DW,E,E},
        {E,DW,E,E,E,TL,E,E,E,TL,E,E,E,DW,E},
        {TW,E,E,DL,E,E,E,TW,E,E,E,DL,E,E,TW},
    };

    // int score;
    @FXML
    BoardDisplayer boardDisplayer;
    @FXML
    Label scoreTitle; //added changed name
    @FXML
    Label scoreLabel; //added score label
    @FXML
    Label spacingLabel;
    @FXML
    Label directionLabel;
    @FXML
    Label enterWordLabel;
    @FXML
    TextField direction;
    @FXML
    TextField word;
    @FXML
    Button sendButton;
    
    IntegerProperty row;
    IntegerProperty col;
    BooleanProperty isValid;


    //TODO add score
    // Constructor - happens only once
    public PrimaryController(){   
        row = new SimpleIntegerProperty();
        col = new SimpleIntegerProperty();
        isValid = new SimpleBooleanProperty();
       
    }

    void init(ViewModel vm){
        this.vm = vm;
        vm.direction.bind(direction.textProperty());
        vm.word.bind(word.textProperty());
        vm.col.bind(col);
        vm.row.bind(row);
        vm.sendButton.bind(sendButton.pressedProperty());
        

        //scoreLabel.textProperty().bind(vm.scoreLabel);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
               setRow(new SimpleIntegerProperty(((int)e.getY())));
               setCol(new SimpleIntegerProperty(((int)e.getX())));
            } 
         };  

        Font customFont = new Font("Arial", 20);
        scoreTitle.setFont(customFont);
        spacingLabel.setFont(customFont);
        customFont = new Font("Arial", 14);
        directionLabel.setFont(customFont);
        enterWordLabel.setFont(customFont);

        customFont = new Font("Arial", 20);
        scoreLabel.setFont(customFont);
        
        
        boardDisplayer.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        
    }
    
   
    protected void setCol(SimpleIntegerProperty simpleIntegerProperty) {
        col.set((int)Math.floor((simpleIntegerProperty.getValue()*boardData.length)/boardDisplayer.getWidth()));
    }

    protected void setRow(SimpleIntegerProperty simpleIntegerProperty) {
        row.set((int)Math.floor((simpleIntegerProperty.getValue()*boardData[0].length)/boardDisplayer.getHeight()));
    }

    // From Initializable - can be called 
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {        
        boardDisplayer.setBoardData(boardData);    
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public void updateBoard() {
        
            // go over word
            String wordValue = word.getText();
            String directionValue = direction.getText();

            for (int i = 0; i < wordValue.length(); i++) {
                if (wordValue.charAt(i) != '_') {
                    char letter = Character.toLowerCase(wordValue.charAt(i));
                    // put each letter on the board
                    boardData[row.get()][col.get()] = letter - 'a' + 1;
                }
                if(directionValue.equalsIgnoreCase("right")){
                    col.set(col.get() + 1);
                }
                else if(directionValue.equalsIgnoreCase("down")){
                    row.set(row.get() + 1);
                }
                else if(directionValue.equalsIgnoreCase("up")){
                    row.set(row.get() - 1);
                }
            }
            boardDisplayer.setBoardData(boardData);
        
        
        
    }

    public void setScore(Object score) {
    }

   

}