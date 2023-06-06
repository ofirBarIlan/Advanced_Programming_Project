


// Create the Model class here. The model represents the data that the application uses. (MVVM)

package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.application.Platform;
import test.*;

public class Model extends Observable{
    int n = 3;
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
    // Create the dictionary to store player hands
    Map<String, ArrayList<Tile>> playerHands = new HashMap<>();

    int curPlayerIndex = 0;
    String curPlayerName;
    String me;
    private boolean tryWordFlag;

    private boolean testMode = false;


    private boolean respValid;
    private String resp;
    private int port;
    private int[] pos;

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
        this.port = port;
        board = new Board(port, this);
        gameState = GameState.Idle;
        bag = Tile.Bag.getBag();
    }

    public void close() {
		inFromServer.close();
        outToServer.close();
	}


    // Create function startRoom here. This function will return a room number (int). (Host)
    public int startRoom(String name, int port){
        // Check if the game is in the correct state
        if(gameState != GameState.Idle){
            return -1;
        }
        gameState = GameState.WAITING_FOR_PLAYERS;
        try{
            this.socket=new Socket("localhost", port);
            this.outToServer=new PrintWriter(socket.getOutputStream());
            this.inFromServer=new Scanner(socket.getInputStream());
        }catch (Exception e){
            //System.out.println("startRoom: Could not connect to server");
        }
        // Generate a random room number
        int roomNumber = (int)(Math.random() * 100000);
        this.roomNumber = roomNumber;
        //System.out.println(roomNumber);
        this.isHost = true;
        startServer(port);
        me = name;
        players.add(name);
        return roomNumber;
    }

    public void startServer(int port){
        //System.out.println("entered startserver");
        assert isHost;
        ClientHandler gh = new GuestHandler();
        ((GuestHandler)gh).setHost(this);
        hostServer = new MyServer(port, gh, 3);
        hostServer.start();
    }

    // playerHands is hashMap of name and Tiles for each player
    private void initHands(){
        for (String player : players)
        {
            // Initialize player's hand with random Tiles
            ArrayList<Tile> handTiles = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                handTiles.add(bag.getRand());
            }
            
            // Add the player's hand to the dictionary
            playerHands.put(player, handTiles);

            String letters = "";
             for (Tile t: handTiles)
             {
                 letters+=","+t.letter;
             }
            if (player.equals(me))
            {
                setChanged();
                notifyObservers("updateHand,"+player+letters);
            }
            else{
                notifyGuests("updateHand,"+player+letters);
            }
        }
    }

    // Create function startGame here. (Host)
    public void startGame(){
        assert isHost;
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_START){
            assert false;
        }

        determineTurns();

        initHands();

        // Set the game state
        gameState = GameState.PLAYING;

        // Notify the guests that the game has started
        // TODO
    }

    private void determineTurns() {
        String letters = "";
        Map<Character, String> playersStartingLetter = new HashMap<>();

        for(String player : players)
        {
            Tile t = bag.getRand();
            if(playersStartingLetter.get(t.letter) != null){
                bag.put(t);
                t = bag.getRand();
            }
            playersStartingLetter.put(t.letter, player);
            letters += t.letter;

            bag.put(t);
        }

        char[] charArray = letters.toCharArray();
        Arrays.sort(charArray);

        ArrayList<String> newOrder = new ArrayList<>();

        for(char c : charArray){
            newOrder.add(playersStartingLetter.get(c));
            //System.out.println("turn: " +playersStartingLetter.get(c));
        }

        players=newOrder;   
        
        if(players.get(0).equals(me)){
            setChanged();
            notifyObservers("Now it is your turn!");
        }
        else{
            notifyGuests("yourTurn," + players.get(0));
        }
        
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
            //Start a thread to listen to the host server
            Thread t = new Thread(new Runnable(){
                @Override
                public void run(){
                    //System.out.println("start listening to the host server");
                    while(true){
                        String message = inFromServer.next();
                        //System.out.println("got notifyGuests: "+message);
                        String[] args = message.split(",");
                        if(args[0].equals("addWord")){
                            //System.out.println("addWord");
                            setChanged();
                            notifyObservers(message);
                        }
                        else if(args[0].equals("resp")){
                            // set the response to be all the args except the first one
                            String response = "";
                            for(int i=1; i<args.length; i++){
                                response += args[i];
                                if(i != args.length-1){
                                    response += ",";
                                }
                            }
                            setResponse(response);
                        }
                        else if (args[0].equals("updateHand"))
                        {
                            if (args[1].equals(me)){
                                for(int i=2;i<args.length;i++)
                                {
                                    playerHands.get(me).add(Tile.Bag.getBag().getTile((args[i]).charAt(0)));
                                }
                                setChanged();
                                notifyObservers(message);
                            }
                        }
                        else if (args[0].equals("updateHandMiddleOfGame")){
                            if (args[1].equals(me)){
                                String tilesToDrop = args[2];
                                for (char c: tilesToDrop.toCharArray()){
                                    for (Tile t: playerHands.get(me)){
                                        if (t.letter == c){
                                            playerHands.get(me).remove(t);
                                            break;
                                        }
                                    }
                                }
                                String tilesToPick = args[3];
                                for (char c: tilesToPick.toCharArray()){
                                    playerHands.get(me).add(bag.getBag().getTile(c));
                                }
                                String letters = "";
                                for (Tile t: playerHands.get(me))
                                {
                                    letters+=","+t.letter;
                                }
                                final String finalLetters = letters;
                                if (!testMode){
                                    Platform.runLater(() -> {
                                        setChanged();
                                        notifyObservers("updateHandMiddleOfGame"+finalLetters);
                                    });
                                }
                            }
                        }
                        else if (args[0].equals("gameEnd")) {                            
                            if (!testMode){
                                Platform.runLater(() -> {
                                    setChanged();
                                    notifyObservers("gameEnd," + me + "," + totalScore);
                                }); 
                            }                                                       
                        }
                        else if (args[0].equals("yourTurn")){
                            //System.out.println("got to:" + message);
                            if(args[1].equals(me)){
                                if (!testMode){
                                    Platform.runLater(() -> {
                                        setChanged();
                                        notifyObservers("Now it is your turn!");
                                    }); 
                                } 
                            }
                        }
                        else if (args[0].equals("notYourTurn")){
                            //System.out.println("got to:" + message);
                            if(args[1].equals(me)){
                                if (!testMode){
                                    Platform.runLater(() -> {
                                        setChanged();
                                        notifyObservers("Now it is NOT your turn!");
                                    });  
                                }
                            }
                        }
                    }
                }
            });
            t.start();
        }catch (Exception e){
            //System.out.println("joinGameAsGuest: could not connect to the host server");
        }

        // Send Connect request to the host, with the room number
        outToServer.println("joinGame,"+roomNumber+","+name);
        outToServer.flush();

        //System.out.println(port);
        // Get the response from the host
        String response=getResponseFromHost();
        //System.out.println("test2");

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        if(result){
            this.roomNumber = roomNumber;
            this.isHost = false;
            gameState = GameState.WAITING_FOR_START;
            me = name;
            playerHands.put(me, new ArrayList<Tile>());
        }

        return result;
    }

    private void setResponse(String string) {
        this.resp = string;
        this.respValid = true;
    }

    private String getResponseFromHost() {
        while(!this.respValid){
            try{
                Thread.sleep(100);
            }catch (Exception e){
                //System.out.println("getResponse exception");
            }
        }
        this.respValid = false;
        return this.resp;
    }

    // Create function joinGame here. This function will take in a room number (int) and return a boolean. (Host)
    public boolean joinGame(int roomNumber, String name){
        assert isHost;
        // Check if the game is in the correct state
        if(gameState != GameState.WAITING_FOR_PLAYERS && gameState != GameState.WAITING_FOR_START){
            return false;
        }
        // Check if the room number is correct
        // if(this.roomNumber != roomNumber){
        //     return false;
        // }

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

        pos = position;
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

    public Tile[] convertStringToTiles(String word, int row, int col, boolean dir, String name){
        ArrayList<Tile> convertedTiles = new ArrayList<Tile>();
        
        
        for(int i=0; i<word.length(); i++){
            boolean flag = false;
            if (word.charAt(i) == '_') {
                
                if(dir){
                    if(board.getTile()[row][col+i] != null){
                        convertedTiles.add(board.getTile()[row][col+i]);
                        flag = true;
                    }
                }
                else{
                    if(board.getTile()[row+i][col] != null){
                        convertedTiles.add(board.getTile()[row+i][col]);
                        flag = true;
                    }
                }                                     
            }
            else{
                if (testMode)
                {
                    Tile t = bag.getTile(word.charAt(i));
                    if (t != null)
                    {
                        convertedTiles.add(t);
                        bag.put(t);
                        flag = true;
                    }
                    else{
                        for (String player: players)
                        {
                            for(Tile tile : playerHands.get(player)){
                                if(tile.letter == word.charAt(i)){
                                    convertedTiles.add(tile);
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag)
                                break;
                        }
                    }

                }else{
                    for(Tile tile : playerHands.get(name)){
                        if(tile.letter == word.charAt(i)){
                            convertedTiles.add(tile);
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if(!flag){
                tryWordFlag = true;
            }
        }
        Tile[] tilesArray = convertedTiles.toArray(new Tile[convertedTiles.size()]);
        return tilesArray;
    }

    public void challengeVM(String word, String direction ){
        boolean dir =true;
        int row = pos[0];
        int col = pos[1];
        if(direction.equals("Right")){
            dir = false;
        }
        else if(direction.equals("Down")){
            dir = true;
        }
       
        Tile[] tiles = convertStringToTiles(word, row, col, dir, me);
        
        Word w = new Word(tiles, row, col, dir);
        ArrayList<Word> words = board.getWords(w);
        boolean flag = false;
        for(Word wordToCheck: words)
        {
            String wordStr = "";
            for (Tile t: wordToCheck.getTiles())
            {
                wordStr+=t.letter;
            }
            if(isHost){

                if (!challenge(wordStr, me)){
                    flag = true;
                }
            }
            else{
               if (!challengeAsGuest(wordStr, me)){
                    flag = true;
                }
            }
        }
        if (flag)
            totalScore-=1000;
        else{totalScore += board.getScore(w) + 5;}
    }

    // Create function tryWord here. This function will take in a word (string), a direction (boolean), and a position ((int, int)) and return a boolean.
    // This function will check if the word is in the dictionary and if it is, it will check if the word can be placed in the board at the given position and direction.
    // If the word can be placed, the function will return true. Otherwise, it will return false.
    public Result tryWord(String word, boolean direction, int[] position, String name){
        //System.out.println("start tryWord " + word);
        word = word.toUpperCase();
       // Check if it is the player's turn
        if(!name.equals(players.get(curPlayerIndex))){
            //System.out.println("Not the player's turn");
            return new Result(0, ErrorType.NOT_YOUR_TURN);
        }

        tryWordFlag = false;

        Tile[] tiles = convertStringToTiles(word, position[0], position[1], direction, name);
        if(tryWordFlag){
            return new Result(0, ErrorType.DO_NOT_HAVE_LETTERS);
        }

        Word w = new Word(tiles, position[0], position[1], !direction);
        scoreCalculated = board.tryPlaceWord(w);
        if(scoreCalculated>0){
            // remove tiles from player's hand (only if the tile is not "_")
            for(char c : word.toCharArray()){
                if(c != '_'){
                    for(Tile tile : playerHands.get(name)){
                        if(c == tile.letter){
                            playerHands.get(name).remove(tile);
                            break;
                        }
                    }
                    if(name.equals(me)){
                        // get new tiles from bag
                        playerHands.get(name).add(bag.getRand());
                    }
                }
            }
            if (!name.equals(me))
            {
                // notify the guests to update their hands
                String tilesToDrop = "";
                for(char c : word.toCharArray()){
                    if(c != '_'){
                        tilesToDrop += c;
                    }
                }
                String tilesToAdd = "";
                for(char c: tilesToDrop.toCharArray()){
                    Tile tile = bag.getRand();
                    if (tile == null)
                        break;
                    playerHands.get(name).add(tile);
                    tilesToAdd += tile.letter;
                }
                notifyGuests("updateHandMiddleOfGame,"+name+","+tilesToDrop+","+tilesToAdd);
            }
            else
            {
                // notify the view to update the hand
                String letters = "";
                for(Tile t : playerHands.get(name)){
                    letters += ","+t.letter;
                }
                setChanged();
                notifyObservers("updateHandMiddleOfGame"+letters);
            }
            nextPlayer();
            notifyGuests("addWord,"+name+","+word+","+direction+","+position[0]+","+position[1]+","+scoreCalculated);
            if (!name.equals(me)) {
                setChanged();
                notifyObservers("addWord,"+name+","+word+","+direction+","+position[0]+","+position[1]+","+scoreCalculated);
            }
            error = ErrorType.SUCCESS;
        }
        return new Result(scoreCalculated, error);
    }

    public void giveUp(String name){
        assert isHost;
        // Check if it is the player's turn
        if(!name.equals(players.get(curPlayerIndex))){
            assert false;
        }

        nextPlayer();
    }

    public boolean challenge(String word, String name){
        assert isHost;
        // Check if it is the player's turn
        if(!name.equals(players.get(curPlayerIndex))){
            return false;
        }
        // get all new words - getWords()

        String books = "Harray Potter.txt";
        // Send the word to the server
        String response = sendOnPort("C,"+books+","+word, port);

        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
        nextPlayer();
        return result;
    }

    public Result tryWordAsGuest(String word, boolean direction, int[] position, String name){
        assert !isHost;
        // Send the word, direction, and position to the server
        String dir = direction ? "Down" : "Right";
        // Send the word to the server and get the response
        outToServer.println("TryWord,"+word+","+dir+","+position[0]+","+position[1]+","+name);
        outToServer.flush();
        String response=getResponseFromHost();

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
        String response = getResponseFromHost();

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
            this.socket=new Socket("https://localhost",6200);
            this.outToServer=new PrintWriter(socket.getOutputStream());
            this.inFromServer=new Scanner(socket.getInputStream());
            outToServer.println(message);
            outToServer.flush();
            String response=inFromServer.next();
            return response;
        }catch (Exception e){
            //System.out.println("sendOnPort exception");
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
        if(players.get(curPlayerIndex).equals(me)){
            setChanged();
            notifyObservers("Now it is NOT your turn!");
        }
        else{
            notifyGuests("notYourTurn," + players.get(curPlayerIndex));
        }

        if(curPlayerIndex==players.size()-1){
            n--;
        }
        if(isHost && n==0){
            endGame();
        }
        curPlayerIndex = (curPlayerIndex + 1) % players.size();
        curPlayerName = players.get(curPlayerIndex);

        if(players.get(curPlayerIndex).equals(me)){
            if (!testMode){
                Platform.runLater(() -> {
                    setChanged();
                    notifyObservers("Now it is your turn!");
                });
            }
        }
        else{
            notifyGuests("yourTurn," + players.get(curPlayerIndex));
        }
    }

    private void endGame() {
        if (!testMode){
            Platform.runLater(() -> {
                setChanged();
                notifyObservers("gameEnd," + me + "," + totalScore);
            });
        }
        notifyGuests("gameEnd,");
    }


    // Unit tests, to test the functions in the class
    public static void main(String[] args) {
        int serverPort = 6200;
        int hostPort = 6100;
        String host = "host";
        String guest1 = "guest1";
        Model hostModel = new Model(serverPort);
        Model guestModel1 = new Model(hostPort);

        // create observers
        Observer hostView = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                //System.out.println("hostView: " + arg);
            }
        };

        Observer guestView1 = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                //System.out.println("guestView1: " + arg);
            }
        };

        // add the observables to the observers
        hostModel.addObserver(hostView);
        guestModel1.addObserver(guestView1);
        

        hostModel.setTestMode();
        guestModel1.setTestMode();

        // test startRoom
        int roomNumber = hostModel.startRoom(host,6100);
        if (roomNumber<0)
            System.out.println("test startRoom failed");
        else {
            System.out.println("test startRoom success");
        }
        if (!guestModel1.joinGameAsGuest(roomNumber, hostPort, guest1))
            System.out.println("test joinGameAsGuest failed");
        else {
            System.out.println("test joinGameAsGuest success");
        }
        hostModel.startGame();

        ArrayList<String> playersList = hostModel.getPlayers();

        // create an action queue to test the functions
        Queue<String> actionQueue = new ConcurrentLinkedQueue<>();
        // insert actions to the queue
        actionQueue.add("hello,Right,7,7");
        actionQueue.add("_ost,Down,7,9");
        actionQueue.add("_ay,Right,9,9");
        actionQueue.add("_ell,Down,9,11");

        int n = 2;
        int j = 0;
        while (j<n)
        {
            j++;
            // use a loop to test tryWord and tryWordAsGuest, by the order of the players list.
            for (int i = 0; i < playersList.size(); i++) {
                String player = playersList.get(i);
                String action = actionQueue.poll();
                // parse the action
                String[] args1 = action.split(",");
                String word = args1[0];
                boolean dir = args1[1].equals("Right");
                int row = Integer.parseInt(args1[2]);
                int col = Integer.parseInt(args1[3]);
                if (player.equals(host))
                {
                    // test tryWord
                    Result result = hostModel.tryWord(word, dir, new int[]{row, col}, host);
                    //System.out.println("score: " + result.score + ", errorType: " + result.errorType);
                    if (result.score == 0 && result.errorType != ErrorType.SUCCESS)
                        System.out.println("test tryWord failed");
                    else {
                        System.out.println("test tryWord success");
                    }
                }
                else if (player.equals(guest1))
                {
                    // test tryWordAsGuest
                    Result result1 = guestModel1.tryWordAsGuest(word, dir, new int[]{row, col}, guest1);
                    //System.out.println("score: " + result1.score + ", errorType: " + result1.errorType);
                    if (result1.score == 0 && result1.errorType != ErrorType.SUCCESS)
                        System.out.println("test tryWordAsGuest failed");
                    else {
                        System.out.println("test tryWordAsGuest success");
                    }
                }
            }

        }

        System.out.println("done!");

    }

    public void skipTurn() {
        if(isHost){
            nextPlayer();
        }
        else{
            outToServer.println("skip," + me);
            outToServer.flush();
        }

    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setTestMode() {
        testMode = true;
    }

}