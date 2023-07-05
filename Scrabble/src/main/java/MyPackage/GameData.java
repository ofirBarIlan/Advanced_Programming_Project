package MyPackage;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;

public class GameData implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int objectCounter=0;
	ArrayList<String> names;
	ArrayList<Integer> scores;
	Integer numPlayers;
	ArrayList<String> hands;
	ArrayList<String> board;
	String currentPlayer;
	
	// Default constructor
	public GameData() {
		
		
	}
	// Constructor
	public GameData(ArrayList<String> names, ArrayList<Integer> scores, ArrayList<String> hands, ArrayList<String> board, Integer numPlayers, String currentPlayer){
		this.names=names;
		this.scores=scores;
		this.numPlayers=numPlayers;
		this.hands=hands;
		this.board=board;
		this.currentPlayer=currentPlayer;		
	}
	
	@Override
	public String toString() {
		return "GameData [names=" + names + ",currentPlayer="+currentPlayer.toString()+ "]";
		
	}

	public Document toDocument( ) {
		//Take all data members and append to Document;
		Document document = new Document("serialVersionUID", serialVersionUID);
		document.append("names",names);
		document.append("scores",scores);
		document.append("numPlayers",numPlayers);
		document.append("hands",hands);
		document.append("board", board);
		document.append("currentPlayer", currentPlayer);
		
		return document;
	}
	
	public static GameData fromDocument(Document d) {
		
		ArrayList<String> names = (ArrayList<String>) d.getList("names",String.class);
		ArrayList<Integer> scores = (ArrayList<Integer>) d.getList("scores",Integer.class);
		ArrayList<String> hands = (ArrayList<String>) d.getList("hands",String.class);
		ArrayList<String> board = (ArrayList<String>) d.getList("board",String.class);
		
		String currentPlayer = d.getString("currentPlayer");
		Integer numPlayers = d.getInteger("numPlayers");
		
		GameData gd = new GameData(names, scores, hands, board, numPlayers, currentPlayer);
		return gd;		
	}
	
	public ArrayList<String> getNames() {
		return names;
	}

	public ArrayList<Integer> getScores() {
		return scores;
	}

	public ArrayList<String> getHands() {
		return hands;
	}

	public ArrayList<String> getBoard() {
		return board;
	}

	public Integer getNumPlayers() {
		return numPlayers;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}


	//public static intListFromDoc()
	
//	public MyPlayer(String name) {
//		this.numbers=new ArrayList<Integer>();
//		Random rand =new Random();
//		numbers.add(9);
//		numbers.add(11);
//		this.numbers.add((Integer)rand.nextInt());
//		this.numbers.add((Integer)rand.nextInt());
//		this.numbers.add((Integer)rand.nextInt());
//		this.numbers.add((Integer)rand.nextInt());
//		this.name=name;
//		objectCounter++;
//		this.id=objectCounter;
//	}
}
