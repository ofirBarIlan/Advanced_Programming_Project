


// Create the Model class here. The model represents the data that the application uses. (MVVM)

package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import test.*;

public class Model extends Observable{

    // Define an enum for the different states of the game
    public enum GameState{
        Idle,
        WAITING_FOR_PLAYERS,
        WAITING_FOR_START,
        PLAYING,
        FINISHED
    }

    GameState gameState;
    PrintWriter outToServer;
    Scanner inFromServer;
    Socket socket;
    int roomNumber;
    MyServer hostServer;
    int scoreCalculated;
    int totalScore=0;
    ErrorType error;
    boolean isHost = false;
    // Board information
    Board board;

    Tile.Bag bag;

    // List of all players in the game
    ArrayList<String> players = new ArrayList<String>();
    int curPlayer = 0;
    String me;

    public class Result {
        public int score;
        public ErrorType errorType;
        public Result(String strFormat) {
            String[] args = strFormat.split(",");
            this.score = Integer.parseInt(args[0]);
            this.errorType = ErrorType.values()[Integer.parseInt(args[1])];
        }

        public Result(int score, ErrorType errorType) {
            this.score = score;
            this.errorType = errorType;
        }
    }

    // Constructor
    public Model(int port){
        
        board = new Board(port, this);
        gameState = GameState.Idle;
        bag = Tile.Bag.getBag();
    }

    public void close() {
		inFromServer.close();
        outToServer.close();
	}


    // Create function startRoom here. This function will return a room number (int). (Host)
    public int startRoom(String name){
        // Check if the game is in the correct state
        if(gameState != GameState.Idle){
            return -1;
        }
        gameState = GameState.WAITING_FOR_PLAYERS;
        
        // Generate a random room number
        int roomNumber = (int)(Math.random() * 100000);
        this.roomNumber = roomNumber;
        System.out.println(roomNumber);
        this.isHost = true;
        int port = 6100;
        startServer(port);
        me = name;
        players.add(name);
        return roomNumber;
    }

    public void startServer(int port){
        System.out.println("entered startserver");
        assert isHost;
        ClientHandler gh = new GuestHandler();
        ((GuestHandler)gh).setHost(this);
        hostServer = new MyServer(port, gh, 3);
        hostServer.start();
    }

    // Create function startGame here. (Host)
    public void startGame(){
        assert isHost;
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_START){
            assert false;
        }

        // Set the game state
        gameState = GameState.PLAYING;

        // Notify the guests that the game has started
        // TODO
    }

    // Single player version
    public boolean startGameAsGuest(String name){
        // Check if the game is in the correct state
        if(gameState != GameState.Idle){
            return false;
        }

        // Set the game state
        gameState = GameState.PLAYING;

        me = name;
        players.add(name);
        return true;
    }

    // Create function joinGameAsClient here. This function will take in a room number (int) and return a boolean. (Guest)
    public boolean joinGameAsGuest(int roomNumber, int port, String name){
        assert !isHost;
        // Check if the game is in the correct state
        if(gameState != GameState.Idle){
            return false;
        }

        // Connect to the host server
        try{
            this.socket=new Socket("localhost", port);
            this.outToServer=new PrintWriter(socket.getOutputStream());
            this.inFromServer=new Scanner(socket.getInputStream());
        }catch (Exception e){
            System.out.println("joinGameAsGuest: could not connect to the host server");
        }

        // Send Connect request to the host, with the room number
        outToServer.println("joinGame,"+roomNumber+","+name);
        outToServer.flush();

        System.out.println(port);
        // Get the response from the host
        String response=inFromServer.next();
        System.out.println("test2");

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        if(result){
            this.roomNumber = roomNumber;
            this.isHost = false;
            gameState = GameState.WAITING_FOR_START;
            me = name;
        }

        return result;
    }

    // Create function joinGame here. This function will take in a room number (int) and return a boolean. (Host)
    public boolean joinGame(int roomNumber, String name){
        assert isHost;
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_PLAYERS && gameState != GameState.WAITING_FOR_START){
            return false;
        }
        // Check if the room number is correct
        if(this.roomNumber != roomNumber){
            return false;
        }

        // Set the game state
        gameState = GameState.WAITING_FOR_START;

        players.add(name);
        return true;
    }

    public ErrorType tryWordVM(String word, String direction, int[] position){
        boolean dir=true;
        if(direction.equals("Right")){
            dir = true;
        }
        else if(direction.equals("Down")){
            dir = false;
        }
        Result result;
        if(isHost){
            result = tryWord(word, dir, position, me);
        }
        else{
            result = tryWordAsGuest(word, dir, position, me);
        }
        scoreCalculated = result.score;
        error = result.errorType;
        totalScore += scoreCalculated;
        return error;
    }
    
    public void giveUpVM(){
        if(isHost){
            giveUp(me);
        }
        else{
            giveUpAsGuest(me);
        }
    }

    public void challengeVM(String word, int row, int col, String direction ){
        boolean dir =true;
        if(direction.equals("Right")){
            dir = true;
        }
        else if(direction.equals("Down")){
            dir = false;
        }

        char[] charArray = word.toCharArray();
        for(int i=0; i<word.length(); i++){
            if (word.charAt(i) == '_') {
                
                if(dir){
                    if(board.getTile()[row][col+i] != null)
                        charArray[i] = board.getTile()[row][col+i].letter;
                }
                else{
                    if(board.getTile()[row+i][col] != null)
                        charArray[i] = board.getTile()[row+i][col].letter;
                }                
            }
        }
        word = new String(charArray);

        Tile[] tiles = new Tile[word.length()];
        for(int i=0; i<word.length(); i++){

                // Change here to get the tile from array of Tiles that get in the beginning
                Tile T = bag.getTile(word.charAt(i));
                if(T != null){
                    // bag.letterAmounts[word.charAt(i)-'A'] ++;
                }
                else{
                    // board[row][col]
                }
                tiles[i] = T;
            
        }
        Word w = new Word(tiles, row, col, dir);
        if(isHost){
            
            if (challenge(word, me)){
                totalScore += board.getScore(w) + 5;

            }
            else{
                totalScore-=1000;
            }
        }
        else{
           if (challengeAsGuest(word, me)){
                totalScore += board.getScore(w) + 5;
            }
        }
    }

    // Create function tryWord here. This function will take in a word (string), a direction (boolean), and a position ((int, int)) and return a boolean.
    // This function will check if the word is in the dictionary and if it is, it will check if the word can be placed in the board at the given position and direction.
    // If the word can be placed, the function will return true. Otherwise, it will return false.
    public Result tryWord(String word, boolean direction, int[] position, String name){
        System.out.println("start tryWord " + word);
       // Check if it is the player's turn
        if(!name.equals(players.get(curPlayer))){
            System.out.println("Not the player's turn");
            return new Result(0, ErrorType.NOT_YOUR_TURN);
        }
        // Check if the word is board legal
        Tile[] tiles = new Tile[word.length()];
        for(int i=0; i<word.length(); i++){
            System.out.println(word.charAt(i));
            if (word.charAt(i) == '_') {
                tiles[i] = null;
            }
            else{
                tiles[i] = bag.getTile(word.charAt(i));
            }
        }
        Word w = new Word(tiles, position[0], position[1], !direction);
        scoreCalculated = board.tryPlaceWord(w);
        if(scoreCalculated>0){
            curPlayer = (curPlayer + 1) % players.size();
            // notifyGuests(name+","+word+","+direction+","+position[0]+","+position[1]+","+scoreCalculated);
            // if (name!=me) {
            //     setChanged();
            //     notifyObservers(word+","+direction+","+position[0]+","+position[1]);
            // }
            error = ErrorType.SUCCESS;
        }
        return new Result(scoreCalculated, error);
    }

    public void giveUp(String name){
        assert isHost;
        // Check if it is the player's turn
        if(!name.equals(players.get(curPlayer))){
            assert false;
        }

        curPlayer = (curPlayer + 1) % players.size();
    }

    public boolean challenge(String word, String name){
        assert isHost;
        // Check if it is the player's turn
        if(!name.equals(players.get(curPlayer))){
            return false;
        }
        // get all new words - getWords()

        
        // Send the word to the server
        this.outToServer.println("C,"+word);
        this.outToServer.flush();

        // Get the response from the server
        
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        curPlayer = (curPlayer + 1) % players.size();
        return result;
    }

    public Result tryWordAsGuest(String word, boolean direction, int[] position, String name){
        assert !isHost;
        // Send the word, direction, and position to the server
        String dir = direction ? "Down" : "Right";
        // Send the word to the server and get the response
        String response = sendOnPort("TryWord,"+word+","+dir+","+position[0]+","+position[1]+","+name, 6100);

        // Parse the response as Result
        return new Result(response);
    }

    public void giveUpAsGuest(String name){
        assert !isHost;
        // Send the word to the server
        outToServer.println("GiveUp,"+name);
        outToServer.flush();
    }

    public boolean challengeAsGuest(String word, String name){
        assert !isHost;
        // Send the word to the server
        outToServer.println("Challenge,"+word+","+name);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        return result;
    }

    public void notifyGuests(String message){
        assert isHost;
        // Send the message from the hostServer to the guests
        hostServer.notifyGuests(message);
    }

    public int getScore() {
        return totalScore;
    }
   
    public String sendOnPort(String message, int port){
        try{
            // Socket socket=new Socket("localhost", port);
            // PrintWriter outToServer=new PrintWriter(socket.getOutputStream());
            // Scanner inFromServer=new Scanner(socket.getInputStream());
            outToServer.println(message);
            outToServer.flush();
            String response=inFromServer.next();
            // inFromServer.close();
            // outToServer.close();
            // socket.close();
            return response;
        }catch (Exception e){
            System.out.println("sendOnPort exception");
        }
        return null;
    }

    public boolean isHost(){
        return isHost;
    }

    public int getRoomNumber(){
        return roomNumber;
    }

    public void setError(ErrorType e) {
        error = e;
    }

    private void nextPlayer() {
        curPlayer = (curPlayer + 1) % players.size();
        
    }
}