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
import model.ErrorType;
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
    public int isValid; // 1= valid, 2=Out of board, 3=...
    int rowToSend, colToSend;
    String wordToSend, directionToSend;

    int roomNum, port;
    
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

        isValid = 1;
        
       
        //viewmodel-game to model        
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

    private String getDirection() {
        return this.directionToSend.toLowerCase() ;
    } 

    // from opening page - thirdController
    public void startRoomFromModel(String name) {
        m.startRoom(name);
    }
    public boolean joinRoomFromModel(int roomNum, int port, String name) {
        return m.joinGameAsGuest(roomNum, port, name);
        //return true; // for checking
    }

    private void sendData(Boolean newval){
        if(newval==true && (getDirection().equals("down") || getDirection().equals("up") || getDirection().equals("right"))){
        int loc[] = {rowToSend, colToSend};
        System.out.println(wordToSend + " " + directionToSend + " " + loc[0] + " " + loc[1]);
        ErrorType result = m.tryWordVM(wordToSend, directionToSend, loc);
        if(result == ErrorType.SUCCESS){
            pc.setScore(m.getScore());
            scoreLabel.set(String.valueOf(m.getScore()));
            pc.updateBoard();
        }
        else if (result==ErrorType.NOT_A_WORD)
        {
            // add text to label
        }
        else if (result==ErrorType.OUT_OF_BOUNDS)
        {
            // add text to label
        }
        else if (result==ErrorType.DID_NOT_USE_EXISTING_LETTERS)
        {
            // add text to label
        }
        else if (result==ErrorType.OVERRODE_EXISTING_LETTERS)
        {
            // add text to label
        }
        else if (result==ErrorType.DO_NOT_HAVE_LETTERS)
        {
            // add text to label
        }
        else if (result==ErrorType.NOT_IN_CENTER)
        {
            // add text to label
        }
        else if (result==ErrorType.BAD_LOCATION)
        {
            // add text to label
        }
        else if (result==ErrorType.NOT_YOUR_TURN)
        {
            // add text to label
        }
        else if (result==ErrorType.LAST)
        {
            // add text to label

        }

        }
        else{
            // add text to label             
        }
    }

    @Override
    public void update(Observable o, Object arg) { // gets word, direction , position
        
    }

}