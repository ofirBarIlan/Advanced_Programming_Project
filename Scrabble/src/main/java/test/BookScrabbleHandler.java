package test;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCursor;

import com.mongodb.client.MongoCollection;

import MyPackage.GameData;

public class BookScrabbleHandler implements ClientHandler{
	// Connection settings
	public static String connectionString = "mongodb://localhost:27017";
	public static String databaseName = "MyDatabase";

	// Create a MongoDB client
	public static MongoClient  mongoClient = MongoClients.create(connectionString);

	// Get a reference to the database
	public static MongoDatabase  database = mongoClient.getDatabase(databaseName);
			
    //reads the first line and parses it.
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        
        Scanner in = new Scanner(inFromClient);
        PrintWriter out = new PrintWriter(outToClient);
        String line = in.nextLine();
        String[] args = line.split(",");

        String[] booksAndWord = new String[args.length - 1];
        //get all the args except the first one
        for (int i = 1; i < args.length; i++) {
            booksAndWord[i - 1] = args[i];
        }
        
        if (args[0].equals("Q")) {
            boolean found = DictionaryManager.get().query(booksAndWord);
            if (found) {
                out.println("true");
            } else {
                out.println("false");
            }
        } else if (args[0].equals("C")) {
            boolean found = DictionaryManager.get().challenge(booksAndWord);
            if (found) {
                out.println("true");
            } else {
                out.println("false");
            }
        }
        else if (args[0].equals("S")){
            int numPlayers = Integer.parseInt(args[args.length-2]);
            ArrayList<String> names = new ArrayList<String>();
            for(int i=1; i<numPlayers+1; i++)
            {
                names.add(args[i]);
            }

            ArrayList<Integer> scores = new ArrayList<Integer>();
            for(int i=numPlayers+1; i<2*numPlayers+1; i++)
            {
                scores.add(Integer.parseInt(args[i]));
            }

            ArrayList<String> hands = new ArrayList<String>();
            for(int i=2*numPlayers+1; i<3*numPlayers+1; i++)
            {
                hands.add(args[i]);
            }

            ArrayList<String> board = new ArrayList<String>();
            for(int i=3*numPlayers+1; i<3*numPlayers+16; i++)
            {
                board.add(args[i]);
            }

            String currentPlayer = args[args.length-3];
            int roomNumber = Integer.parseInt(args[args.length-1]);
            GameData gd = new GameData(names, scores, hands, board, numPlayers, currentPlayer, roomNumber);

            Document gameDocument = gd.toDocument();
            // Get a reference to the collection
            MongoCollection<Document> game_collection = database.getCollection("GameSaves");

            // Insert the document into the collection
            game_collection.insertOne(gameDocument);
        }else if (args[0].equals("L")) {
            // Get a reference to the collection
            MongoCollection<Document> game_collection = database.getCollection("GameSaves");
            ArrayList<Document> list = getAllDocumentsMatching(game_collection,"roomNumber", Integer.parseInt(args[1]));
                        
            Document doc = list.get(0);

            // get the names
            ArrayList<String> names = (ArrayList<String>) doc.get("names");

            // convert to string
            String namesStr = "";
            for(String n :names)
            {
                namesStr += n + ",";
            }

            // get the scores
            ArrayList<Integer> scores = (ArrayList<Integer>) doc.get("scores");

            // convert to string
            String scoresStr = "";
            for(Integer s :scores)
            {
                scoresStr += s + ",";
            }
            
            // get the hands
            ArrayList<String> hands = (ArrayList<String>) doc.get("hands");

            // convert to string
            String handsStr = "";
            for(String h :hands)
            {
                handsStr += h + ",";
            }
            
            // get the board
            ArrayList<String> board = (ArrayList<String>) doc.get("board");

            // convert to string
            String boardStr = "";
            for(String b :board)
            {
                boardStr += b + ",";
            }
            
            // get the current player
            String currentPlayer = doc.getString("currentPlayer");
            
            // get the number of players
            int numPlayers = doc.getInteger("numPlayers");
            
            out.println(namesStr+scoresStr+handsStr+boardStr+currentPlayer+","+numPlayers);
        }
        
        out.flush();
        in.close();
        out.close();        
    }

    @Override
    public void close() {
        
    }

    // Find matching for strings
    static ArrayList<Document> getAllDocumentsMatching(MongoCollection<Document> player_collection,String fieldName,String fieldValue){
        ArrayList<Document> list=new ArrayList<Document>();
        MongoCursor<Document> it=player_collection.find(Filters.eq(fieldName, fieldValue)).iterator();
        while(it!=null&&it.hasNext()) {
            list.add(it.next());
        }
        return list;
	}
    
    // Find matching for int
    static ArrayList<Document> getAllDocumentsMatching(MongoCollection<Document> player_collection,String fieldName,int fieldValue){
        ArrayList<Document> list=new ArrayList<Document>();
        MongoCursor<Document> it=player_collection.find(Filters.eq(fieldName, fieldValue)).iterator();
        while(it!=null&&it.hasNext()) {
            list.add(it.next());
        }
        return list;
	}
}
