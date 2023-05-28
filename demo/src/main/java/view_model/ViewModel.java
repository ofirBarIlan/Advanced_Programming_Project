package view_model;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.Attributes.Name;

import com.example.EndController;
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
    EndController ec;

    // Binding for primaryController
    public IntegerProperty row, col;
    public StringProperty word, direction, scoreLabel, roomNumLabel, curPlayer, handsLabel;
    public BooleanProperty sendButton, isMyTurn, challengeButton;

    int rowToSend, colToSend;
    String wordToSend, directionToSend;

    int roomNum, port;

    // Binding for secondaryController
    public BooleanProperty isHost;
    
    
    public ViewModel(Model m, PrimaryController pc, SecondaryController sc, ThirdController tc, EndController ec){
        this.m=m;
        this.pc = pc;
        this.sc = sc;
        this.tc = tc;
        this.ec = ec;
        m.addObserver(this);
        pc.addObserver(this);
        direction = new SimpleStringProperty();     // binded to Prim Controller
        row = new SimpleIntegerProperty();
        col = new SimpleIntegerProperty();
        word = new SimpleStringProperty();
        sendButton = new SimpleBooleanProperty(); 
        challengeButton = new SimpleBooleanProperty(); 

        //roomNumLabel = new SimpleStringProperty();  // binded to Sec Controller
        isHost = new SimpleBooleanProperty();       

        scoreLabel = new SimpleStringProperty();        
        isMyTurn = new SimpleBooleanProperty();
        curPlayer = new SimpleStringProperty();
        handsLabel = new SimpleStringProperty();


        //for secondaryController:
        
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
    public int startRoomFromModel(String name, int port) {
        int roomNum = m.startRoom(name, port);
        scoreLabel.set(String.valueOf(m.getScore()));
        //sc.roomNumLabel.setText(""+m.startRoom(name));
        // roomNumLabel.set(String.valueOf(m.startRoom(name)));
        System.out.println(roomNum);
        isHost.set(m.isHost());
        
        return roomNum;

    }
    public boolean joinRoomFromModel(int roomNum, int port, String name) {
        
        scoreLabel.set(String.valueOf(m.getScore()));
        return m.joinGameAsGuest(roomNum, port, name);
    }

    private void sendData(Boolean newval){
        if(newval==true){
        int loc[] = {rowToSend, colToSend};
        //System.out.println(wordToSend + " " + direction.getValue() + " " + loc[0] + " " + loc[1]);
        ErrorType result = m.tryWordVM(wordToSend, direction.getValue(), loc);
        scoreLabel.set(String.valueOf(m.getScore()));
        if(result == ErrorType.SUCCESS){
            // pc.setScore(m.getScore());
            
            pc.updateBoard();
        }
        else if (result==ErrorType.NOT_A_WORD)
        {
            // set visable button to true
            pc.challengeButton.setVisible(true);
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
        String[] args = ((String)arg).split(",");
        if(args[0].equals("addWord"))
        {
            String direction;
            String name = args[1];
            String word = args[2];
            if (Boolean.parseBoolean(args[3])){
                direction ="Right";
            }
            else{
                direction ="Down";
            }
            
            int row =  Integer.parseInt(args[4]);
            int col =  Integer.parseInt(args[5]);
            String scoreCalculated = args[6];

            pc.updateBoard(word, direction, row, col);
        } else if (args[0].equals("updateHand"))
        {
            String letters = "";
            for(int i=2;i<=args.length-1; i++){
                if(i!=2)
                {
                    letters+=","+args[i];
                }
                else{
                    letters+= args[i];
                }
            }
            handsLabel.set(letters);

        }
        else if (args[0].equals("updateHandMiddleOfGame")) {
            String letters = "";
            for(int i=1;i<=args.length-1; i++){
                if(i!=1)
                {
                    letters+=","+args[i];
                }
                else{
                    letters+= args[i];
                }
            }
            handsLabel.set(letters);
        }
        else if (args[0].equals("gameEnd")) {
            try {                
                Thread.sleep(1000);
                ec.endGame("Good game " + args[1] + "!","Your score is: " + args[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(args[0].equals("Now it is NOT your turn!")){
            pc.turnLabel.setText("Now it is NOT your turn!");
        } 
        else if(args[0].equals("Now it is your turn!")){
            pc.turnLabel.setText("Now it is your turn!");
        } 
    }

    public void setCurPlayer(String curPlayer, boolean isMyTurn) {
    }


    public void challengeButton() {
        
        m.challengeVM(word.getValue(), direction.getValue() );
        
        scoreLabel.set(String.valueOf(m.getScore()));
    }


    public void startGame() {
        if(m.isHost()){
            m.startGame();
        }
    }


    public void skipTurn() {
        m.skipTurn();
    }

}