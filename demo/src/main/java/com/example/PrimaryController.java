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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import view_model.ViewModel;


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

    @FXML
    BoardDisplayer boardDisplayer;
    @FXML
    Label scoreTitle;
    @FXML
    Label scoreLabel; 
    @FXML
    Label spacingLabel;
    @FXML
    Label directionLabel;
    @FXML
    Label enterWordLabel;
    @FXML
    TextField word;
    @FXML
    Button sendButton;
    @FXML
    public Button challengeButton;
    @FXML 
    Label instructionLabel;
    @FXML
    ComboBox<String> directionBox;

    @FXML 
    Label handsLabel;

    ObservableList<String> directionList = FXCollections.observableArrayList("Down", "Right"); // For ComboBox
    
    IntegerProperty row;
    IntegerProperty col;
    BooleanProperty isValid;


    // Constructor - happens only once
    public PrimaryController(){   
        row = new SimpleIntegerProperty();
        col = new SimpleIntegerProperty();
        isValid = new SimpleBooleanProperty();
       
    }

    void init(ViewModel vm){
        this.vm = vm;
        challengeButton.managedProperty().bind(challengeButton.visibleProperty());
        challengeButton.setVisible(false);
        // Set items for comboBox
        directionBox.setValue("Right");
        directionBox.setItems(directionList);
        
        // Bind vm with view
        vm.direction.bind(directionBox.valueProperty());
        vm.word.bind(word.textProperty());
        vm.col.bind(col);
        vm.row.bind(row);
        vm.sendButton.bind(sendButton.pressedProperty());
        // vm.challengeButton.bind(challengeButton.pressedProperty());
        
        // Bind view with vm
        scoreLabel.textProperty().bind(vm.scoreLabel);
        handsLabel.textProperty().bind(vm.handsLabel);
        

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
               setRow(new SimpleIntegerProperty(((int)e.getY())));
               setCol(new SimpleIntegerProperty(((int)e.getX())));
            } 
         };  

        // Set font and size of text
        Font customFont = new Font("Arial", 20);
        scoreTitle.setFont(customFont);
        scoreLabel.setFont(customFont);
        spacingLabel.setFont(customFont);

        customFont = new Font("Arial", 14);
        directionLabel.setFont(customFont);
        enterWordLabel.setFont(customFont);
        instructionLabel.setFont(customFont);        
        
        boardDisplayer.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        
    }
    
    public void setHand(String letters){
        handsLabel.setText(letters);
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
            System.out.println(wordValue);
            String directionValue = vm.direction.getValue();
            
            System.out.println(directionValue);

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
            }
            boardDisplayer.setBoardData(boardData);       
        
        
    }

    public void updateBoard(String word, String dir, int row, int col) {
                
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != '_') {
                char letter = Character.toLowerCase(word.charAt(i));
                // put each letter on the board
                boardData[row][col] = letter - 'a' + 1;
            }
            if(dir.equalsIgnoreCase("right")){
                col++;
            }
            else if(dir.equalsIgnoreCase("down")){
                row++;
            }
        }
        boardDisplayer.setBoardData(boardData);       
    
    
    }

    public void setScore(Object score) {
    }

    public void onChallenge(){
        vm.challengeButton();
        challengeButton.setVisible(false);
        
    }

}