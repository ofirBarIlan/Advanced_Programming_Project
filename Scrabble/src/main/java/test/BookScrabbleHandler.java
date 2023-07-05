package test;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
        //System.out.println("handle client");
        String line = in.nextLine();
        String[] args = line.split(",");
        //System.out.println("after next line");

        String[] booksAndWord = new String[args.length - 1];
        //get all the args except the first one
        for (int i = 1; i < args.length; i++) {
            booksAndWord[i - 1] = args[i];
        }
        
        if (args[0].equals("Q")) {
            //System.out.println("got word "+booksAndWord[booksAndWord.length-1]);
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
            int numPlayers = Integer.parseInt(args[args.length-1]);
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

            String currentPlayer = args[args.length-2];
            GameData gd = new GameData(names, scores, hands, board, numPlayers, currentPlayer);

            Document gameDocument = gd.toDocument();
            // Get a reference to the collection
            MongoCollection<Document> game_collection = database.getCollection("GameSaves");
            System.out.println(gd.getNames());
            // Insert the document into the collection
            game_collection.insertOne(gameDocument);
            
            //Document playerdoc = player_collection.find(Filters.eq("name", "dani")).first();
            System.out.println(getAllDocumentsMatching(game_collection,"currentPlayer","Tomer"));       

        }
        
        out.flush();
        in.close();
        out.close();
        
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    static ArrayList<Document> getAllDocumentsMatching(MongoCollection<Document> player_collection,String fieldName,String fieldValue){
        ArrayList<Document> list=new ArrayList<Document>();
        MongoCursor<Document> it=player_collection.find(Filters.eq(fieldName, fieldValue)).iterator();
        while(it!=null&&it.hasNext()) {
            list.add(it.next());
        }
        return list;
	}
}
