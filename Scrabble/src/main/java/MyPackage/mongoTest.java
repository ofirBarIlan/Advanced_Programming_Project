package MyPackage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;
import com.mongodb.client.model.Filters;
public class mongoTest {
	
	// Connection settings
		public static String connectionString = "mongodb://localhost:27017";
		public static String databaseName = "MyDatabase";
		// Create a MongoDB client
		public static MongoClient  mongoClient = MongoClients.create(connectionString);
		// Get a reference to the database
		public static MongoDatabase  database = mongoClient.getDatabase(databaseName);
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//communicate with mongodb server:
		// Convert GameState object to Document
		System.out.println("test = "+testConnection(mongoClient));
		
		ArrayList<String> names = new ArrayList();
		names.add("Yanai");
		names.add("Ofir");
		names.add("Tomer");
		
		ArrayList<Integer> scores = new ArrayList();
		scores.add(3);
		scores.add(6);
		scores.add(2);
		
		Integer numPlayers = 3;
		
		ArrayList<String> hands = new ArrayList();
		hands.add("-----cccccbbbbb");
		hands.add("-----aaaaabbbbb");
		hands.add("-----zzzzzbbbbb");
		
		ArrayList<String> board = new ArrayList();
		board.add("-----cccccbbbbb");
		board.add("-----aaaaabbbbb");
		board.add("-----zzzzzbbbbb");
		
		String currentPlayer = "Tomer";
		
		GameData gd=new GameData(names, scores, hands, board, numPlayers, currentPlayer,0);
		//MyPlayer p2=new MyPlayer("dani");
		//MyPlayer p3=new MyPlayer("moshon");
		Document gameDocument = gd.toDocument();
		// Get a reference to the collection
		MongoCollection<Document> game_collection = database.getCollection("GameSaves");
		System.out.println(gd.names);
		// Insert the document into the collection
		game_collection.insertOne(gameDocument);
		//player_collection.insertOne(p2.toDocument());
		//player_collection.insertOne(p3.toDocument());
		//now we want to get an object from the data base:
		// Retrieve the document from the collection
		
		//Document playerdoc = player_collection.find(Filters.eq("name", "dani")).first();
		System.out.println(getAllDocumentsMatching(game_collection,"currentPlayer","Tomer"));
	
		// Convert the Document to GameState object
		//MyPlayer loadedPlayer = MyPlayer.fromDocument(playerdoc);
		//System.out.println("loaded player is :"+loadedPlayer );
	}
	
	
	static ArrayList<Document> getAllDocumentsMatching(MongoCollection<Document> player_collection,String fieldName,String fieldValue){
		ArrayList<Document> list=new ArrayList<Document>();
		MongoCursor<Document> it=player_collection.find(Filters.eq(fieldName, fieldValue)).iterator();
		while(it!=null&&it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
	
	public void saveGame(ArrayList<String> names, ArrayList<Integer> scores, ArrayList<String> hands, ArrayList<String> board, Integer numPlayers, String currentPlayer) {
		
	}
	
	public static boolean testConnection(MongoClient mongoClient) {
        try {
            // Attempt to retrieve the database names
            String s=mongoClient.listDatabaseNames().first();
            System.out.println("first data base is "+s);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
	
	

}
