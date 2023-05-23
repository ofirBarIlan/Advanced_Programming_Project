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


public class ViewModel extends Observable implements Observer{
    Model m;
    PrimaryController pc;
    SecondaryController sc;
    ThirdController tc;

    // Binding for primaryController
    public IntegerProperty row, col;
    public StringProperty word, direction, scoreLabel, roomNumLabel, curPlayer;
    public BooleanProperty sendButton, isMyTurn;

    int rowToSend, colToSend;
    String wordToSend, directionToSend;

    int roomNum, port;

    // Binding for secondaryController
    public BooleanProperty isHost;
    
    public ViewModel(Model m, PrimaryController pc, SecondaryController sc, ThirdController tc){
        this.m=m;
        this.pc = pc;
        this.sc = sc;
        this.tc = tc;
        m.addObserver(this);
        pc.addObserver(this);
        direction = new SimpleStringProperty();     // binded to Prim Controller
        row = new SimpleIntegerProperty();
        col = new SimpleIntegerProperty();
        word = new SimpleStringProperty();
        sendButton = new SimpleBooleanProperty(); 

        roomNumLabel = new SimpleStringProperty();  // binded to Sec Controller
        isHost = new SimpleBooleanProperty();       

        scoreLabel = new SimpleStringProperty();        
        isMyTurn = new SimpleBooleanProperty();
        curPlayer = new SimpleStringProperty();


        //for secondaryController:
        sendButton = new SimpleBooleanProperty();
        // Bind from Model - uncomment after there are isMyTurn,curPlayer, totalScore in model
        // isMyTurn.bind(m.isMyTurn);
        // curPlayer.bind(m.curPlayer);
        // scoreLabel.bind(m.totalScore);

        // Bind here isHost - need to change in model isHost to BooleanProperty
        isHost.set(m.isHost());
                
       
        //viewmodel-game to model        
        direction.addListener((obs,oldval,newval) -> setDirection((String)newval));
        row.addListener((obs,oldval,newval) -> setRow((int)newval));
        col.addListener((obs,oldval,newval) -> setCol((int)newval));
        word.addListener((obs,oldval,newval) -> setWord((String)newval));
        sendButton.addListener((obs,oldval,newval) -> sendData(newval));
       
    }


    // Get new score from Model and changes in View
    public void setScore(String newScore) {
        scoreLabel.set(newScore);
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

    // from thirdController - starts room and updates the room number
    public void startRoomFromModel(String name) {
        roomNumLabel.set(String.valueOf(m.startRoom(name)));
        isHost.set(m.isHost());

    }
    public boolean joinRoomFromModel(int roomNum, int port, String name) {
        return m.joinGameAsGuest(roomNum, port, name);
    }

    private void sendData(Boolean newval){
        if(newval==true){
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