


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
    int scoreCalculated=10;
    boolean isHost = false;
    // Board information
    Board board;

    Tile.Bag bag;

    // List of all players in the game
    ArrayList<String> players = new ArrayList<String>();
    int curPlayer = 0;
    String me;

    // Constructor
    public Model(int port){
        
        board = new Board(port);
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

    public boolean tryWordVM(String word, String direction, int[] position){
        boolean dir=true;
        if(direction.equals("right")){
            dir = true;
        }
        else if(direction.equals("down")){
            dir = false;
        }
        if(isHost){
            return tryWord(word, dir, position, me);
        }
        else{
            return tryWordAsGuest(word, dir, position, me);
        }
    }
    
    public void giveUpVM(){
        if(isHost){
            giveUp(me);
        }
        else{
            giveUpAsGuest(me);
        }
    }

    public boolean challengeVM(String word){
        if(isHost){
            return challenge(word, me);
        }
        else{
            return challengeAsGuest(word, me);
        }
    }

    // Create function tryWord here. This function will take in a word (string), a direction (boolean), and a position ((int, int)) and return a boolean.
    // This function will check if the word is in the dictionary and if it is, it will check if the word can be placed in the board at the given position and direction.
    // If the word can be placed, the function will return true. Otherwise, it will return false.
    public boolean tryWord(String word, boolean direction, int[] position, String name){
        System.out.println("start tryWord " + word);
       // Check if it is the player's turn
        if(!name.equals(players.get(curPlayer))){
            System.out.println("Not the player's turn");
            return false;
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
            return true;
        }
        return false;
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
        // Send the word to the server
        outToServer.println("challenge,"+word);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        curPlayer = (curPlayer + 1) % players.size();
        return result;
    }

    public boolean tryWordAsGuest(String word, boolean direction, int[] position, String name){
        assert !isHost;
        // Send the word, direction, and position to the server
        String dir = direction ? "Down" : "Right";
        // Send the word to the server and get the response
        String response = sendOnPort("TryWord,"+word+","+dir+","+position[0]+","+position[1]+","+name, 6100);

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        return result;
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
        return scoreCalculated;
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
}