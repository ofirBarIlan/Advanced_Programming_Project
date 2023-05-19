


// Create the Model class here. The model represents the data that the application uses. (MVVM)

package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;


import test.MyServer;

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

    boolean isHost = false;

    // List of all players in the game
    ArrayList<String> players = new ArrayList<String>();
    int curPlayer = 0;
    String me;

    // Constructor
    public Model(int port){
        try{
            this.socket=new Socket("localhost", port);
            this.outToServer=new PrintWriter(socket.getOutputStream());
            this.inFromServer=new Scanner(socket.getInputStream());
        }catch (Exception e){
            System.out.println("Exception was thrown");
        }
        gameState = GameState.Idle;
    }

    public void close() {
		inFromServer.close();
        outToServer.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }	
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
        this.isHost = true;
        int port = 6000;
        startServer(port);
        me = name;
        players.add(name);
        return roomNumber;
    }

    public void startServer(int port){
        assert isHost;
        MyServer hostServer = new MyServer(port, new GuestHandler(this), 3);
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

        // Connect to the server
        try{
            this.socket=new Socket("localhost", port);
            this.outToServer=new PrintWriter(socket.getOutputStream());
            this.inFromServer=new Scanner(socket.getInputStream());
        }catch (Exception e){
            System.out.println("Exception was thrown");
        }

        // Send Connect request to the host, with the room number
        outToServer.println("joinGame,"+roomNumber+","+name);
        outToServer.flush();

        // Get the response from the host
        String response=inFromServer.next();

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


    // Create function tryWord here. This function will take in a word (string), a direction (boolean), and a position ((int, int)) and return a boolean.
    // This function will check if the word is in the dictionary and if it is, it will check if the word can be placed in the board at the given position and direction.
    // If the word can be placed, the function will return true. Otherwise, it will return false.
    public boolean tryWord(String word, boolean direction, int[] position, String name){
        assert isHost;
        // Check if it is the player's turn
        if(!name.equals(players.get(curPlayer))){
            return false;
        }
        // Send the word, direction, and position to the server
        String dir = direction ? "Down" : "Right";
        outToServer.println("tryWord,"+word+","+dir+","+position[0]+","+position[1]);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        if(result){
            curPlayer = (curPlayer + 1) % players.size();
        }
        return result;
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
        outToServer.println("TryWord,"+word+","+dir+","+position[0]+","+position[1]+","+name);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

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
        
}