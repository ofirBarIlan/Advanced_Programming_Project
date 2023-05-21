package view_model;

import java.util.Observable;
import java.util.Observer;

import com.example.PrimaryController;
import com.example.SecondaryController;
import com.example.ThirdController;

import javafx.beans.property.BooleanProperty;
// import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
// import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;
import model.Model_Stub;

// added Observable
public class ViewModel extends Observable implements Observer{
    Model m;
    PrimaryController pc;
    SecondaryController sc;
    ThirdController tc;
    public IntegerProperty row, col;
    public StringProperty word, direction, scoreLabel;
    public BooleanProperty sendButton;
    public boolean isValid;
    int rowToSend, colToSend;
    String wordToSend, directionToSend;
    
    public ViewModel(Model m, PrimaryController pc, SecondaryController sc, ThirdController tc){
        this.m=m;
        this.pc = pc;
        this.sc = sc;
        this.tc = tc;
        m.addObserver(this);
        pc.addObserver(this);
        direction = new SimpleStringProperty();
        row = new SimpleIntegerProperty();
        col = new SimpleIntegerProperty();
        word = new SimpleStringProperty();
        sendButton = new SimpleBooleanProperty();

        scoreLabel = new SimpleStringProperty();
        isValid = false;
        
       
        //viewmodel to model        
        direction.addListener((obs,oldval,newval) -> setDirection((String)newval));
        row.addListener((obs,oldval,newval) -> setRow((int)newval));
        col.addListener((obs,oldval,newval) -> setCol((int)newval));
        word.addListener((obs,oldval,newval) -> setWord((String)newval));
        sendButton.addListener((obs,oldval,newval) -> sendData(newval));
       
    }

    private void setWord(String newval) {
        wordToSend = newval;
    }

    private void setCol(int newval) {
        colToSend = newval;
    }

    private void setRow(int newval) {
        rowToSend = newval;
    }

    private void setDirection(String newval) {
        directionToSend = newval;
    }
    public void startRoomFromModel(String name) {
        m.startRoom(name);
    }
    public void joinRoomFromModel(int num, int port, String name) {
        if(m.joinGameAsGuest(num, port, name)){
            
        }
        else{
            System.exit(1);
        }
    }

    private String getDirection() {
        return this.directionToSend.toLowerCase() ;
    } 

    private void sendData(Boolean newval){
        if(newval==true && (getDirection().equals("down") || getDirection().equals("up") || getDirection().equals("right"))){
        int loc[] = {rowToSend, colToSend};
        isValid = m.tryWordVM(wordToSend, directionToSend, loc);
        if(isValid){
            pc.setScore(m.getScore());
            scoreLabel.set(String.valueOf(m.getScore()));
            pc.updateBoard();
        }

        }
        else{
            // add text to label             
        }
    }

    @Override
    public void update(Observable o, Object arg) {
    
    }

}