


// Create the Model class here. The model represents the data that the application uses. (MVVM)

package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Model{

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
    Socket server;
    int roomNumber;

    // Constructor
    public Model(int port){
        try{
            this.server=new Socket("localhost", port);
            this.outToServer=new PrintWriter(server.getOutputStream());
            this.inFromServer=new Scanner(server.getInputStream());
        }catch (Exception e){
            System.out.println("Exception was thrown");
        }
        gameState = GameState.Idle;
    }

    public void close() {
		inFromServer.close();
        outToServer.close();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }	
	}


    // Create function startRoom here. This function will return a room number (int). (Host)
    public int startRoom(){
        // Check if the game is in the correct state
        if(gameState != GameState.Idle){
            return -1;
        }
        gameState = GameState.WAITING_FOR_PLAYERS;

        // Generate a random room number
        int roomNumber = (int)(Math.random() * 100000);
        this.roomNumber = roomNumber;
        return roomNumber;
    }

    // Create function startGame here. (Host)
    public void startGame(){
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_START){
            assert false;
        }

        // Set the game state
        gameState = GameState.PLAYING;

        // Notify the guests that the game has started
        // TODO
    }

    public boolean startGameAsGuest(){
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_START){
            return false;
        }

        // Set the game state
        gameState = GameState.PLAYING;

        return true;
    }

    // Create function joinGameAsClient here. This function will take in a room number (int) and return a boolean. (Guest)
    public boolean joinGameAsGuest(int roomNumber){
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_PLAYERS){
            return false;
        }
        gameState = GameState.WAITING_FOR_START;

        // Send the room number to the server
        outToServer.println("joinGame,"+roomNumber);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        return result;
    }

    // Create function joinGame here. This function will take in a room number (int) and return a boolean. (Host)
    public boolean joinGame(int roomNumber){
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

        return true;
    }


    // Create function tryWord here. This function will take in a word (string), a direction (boolean), and a position ((int, int)) and return a boolean.
    // This function will check if the word is in the dictionary and if it is, it will check if the word can be placed in the board at the given position and direction.
    // If the word can be placed, the function will return true. Otherwise, it will return false.
    public boolean tryWord(String word, boolean direction, int[] position){
        // Send the word, direction, and position to the server
        String dir = direction ? ">" : "<";
        outToServer.println("tryWord,"+word+","+dir+","+position[0]+","+position[1]);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        return result;
    }

    public boolean challenge(String word){
        // Send the word to the server
        outToServer.println("challenge,"+word);
        outToServer.flush();

        // Get the response from the server
        String response=inFromServer.next();

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        return result;
    }
}