package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import model.ErrorType;
import model.Model;

public class Board {

	private Tile board[][] = new Tile[15][15];
	private int wordMult[][] = new int[15][15];
	private int letterMult[][] = new int[15][15];
	private Socket socket;
	private PrintWriter outToServer;
	private Scanner inFromServer;
	int portToServer;
	Model host;
	
	public Board(int portToServer, Model host) {
		this.portToServer = portToServer;
		this.host = host;

		for(int i = 0; i<wordMult.length; i++) {
			for(int j=0; j<wordMult.length; j++) {
				wordMult[i][j] = 1;
			}
		}
		
		for(int i = 0; i<letterMult.length; i++) {
			for(int j=0; j<letterMult.length; j++) {
				letterMult[i][j] = 1;
			}
		}
		
		wordMult[0][0]=3;
		wordMult[0][7]=3;
		wordMult[0][14]=3;
		wordMult[7][0]=3;
		wordMult[14][0]=3;
		wordMult[14][7]=3;
		wordMult[7][14]=3;
		wordMult[14][14]=3;
		
		wordMult[7][7]=2;
		
		wordMult[1][1]=2;
		wordMult[2][2]=2;
		wordMult[3][3]=2;
		wordMult[4][4]=2;
		
		wordMult[1][13]=2;
		wordMult[2][12]=2;
		wordMult[3][11]=2;
		wordMult[4][10]=2;

		wordMult[13][1]=2;
		wordMult[12][2]=2;
		wordMult[11][3]=2;
		wordMult[10][4]=2;

		wordMult[13][13]=2;
		wordMult[12][12]=2;
		wordMult[11][11]=2;
		wordMult[10][10]=2;
		
		letterMult[0][3]=2;
		letterMult[3][0]=2;
		letterMult[0][11]=2;
		letterMult[11][0]=2;
		letterMult[5][1]=3;
		letterMult[1][5]=3;
		letterMult[9][1]=3;
		letterMult[1][9]=3;
		letterMult[6][2]=2;
		letterMult[2][6]=2;
		letterMult[8][2]=2;
		letterMult[2][8]=2;
		letterMult[7][3]=2;
		letterMult[3][7]=2;
		letterMult[14][3]=2;
		letterMult[3][14]=2;
		letterMult[5][5]=3;
		letterMult[9][5]=3;
		letterMult[5][9]=3;
		letterMult[13][5]=3;
		letterMult[5][13]=3;
		letterMult[6][6]=2;
		letterMult[8][6]=2;
		letterMult[6][8]=2;
		letterMult[12][6]=2;
		letterMult[6][12]=2;
		letterMult[11][7]=2;
		letterMult[7][11]=2;
		letterMult[8][8]=2;
		letterMult[12][8]=2;
		letterMult[8][12]=2;
		letterMult[9][9]=3;
		letterMult[13][9]=3;
		letterMult[9][13]=3;
		letterMult[14][11]=2;
		letterMult[11][14]=2;
		
		for(int i = 0; i<board.length; i++) {
			for(int j=0; j<board.length; j++) {
				board[i][j] = null;
			}
		}
	}
	
	public Tile[][] getTile(){
		return Arrays.copyOf(board, board.length);
	}
	
	public boolean boardLegal(Word w) {
		
		int flag = 0;
		if(w.getVertical()) {
			if((w.getRow()+w.getTiles().length)>board.length) {	
				host.setError(ErrorType.OUT_OF_BOUNDS);
				return false;
			}
		}
		else if(!w.getVertical()) {
			if(w.getCol()+w.getTiles().length>board.length) {
				host.setError(ErrorType.OUT_OF_BOUNDS);
				return false;
			}
		}
		if(w.getRow()<0 || w.getCol()<0) {
			host.setError(ErrorType.OUT_OF_BOUNDS);
			return false;
		}
		if(w.getVertical()) {
			for(int i=w.getRow(); i<w.getRow()+w.getTiles().length;i++) {
				if((board[i][w.getCol()]!=null) && !(w.getTiles()[i-w.getRow()].equals(board[i][w.getCol()]))) {
					host.setError(ErrorType.OVERRODE_EXISTING_LETTERS);
					return false;
				}
				else if((board[i][w.getCol()]!=null) && (w.getTiles()[i-w.getRow()].equals(board[i][w.getCol()]))) {
					flag = 1;
				}
				if(i==7 && w.getCol()==7 && board[7][7]==null) {
					flag = 1;
				}
				if((board[i][w.getCol()]==null) && (i+1<w.getRow()+w.getTiles().length && board[i+1][w.getCol()]!=null)
						|| (i-1>=0 && board[i-1][w.getCol()]!=null) 
						|| (w.getCol()+1<w.getTiles().length && board[i][w.getCol()+1]!=null)
						|| (w.getCol()-1>=0 && board[i][w.getCol()-1]!=null)) {
					flag=1;
				}
			}
		}
		else if(!w.getVertical()) {
			for(int i=w.getCol(); i<w.getCol()+w.getTiles().length;i++) {
				if((board[w.getRow()][i]!=null) && !(w.getTiles()[i-w.getCol()].equals(board[w.getRow()][i]))) {
					host.setError(ErrorType.OVERRODE_EXISTING_LETTERS);
					return false;
				}
				else if((board[w.getRow()][i]!=null) && (w.getTiles()[i-w.getCol()].equals(board[w.getRow()][i]))) {
					flag = 1;
				}
				if(i==7 && w.getRow()==7 && board[7][7]==null) {
					flag = 1;
				}
				if((board[w.getRow()][i]==null) && (i+1<w.getCol()+w.getTiles().length && board[w.getRow()][i+1]!=null)
						|| (i-1>=0 && board[w.getRow()][i-1]!=null) 
						|| (w.getRow()+1<w.getTiles().length && board[w.getRow()+1][i]!=null)
						|| (w.getRow()-1>=0 && board[w.getRow()-1][i]!=null)) {
					flag=1;
				}
			}
		}
		
		if(flag==0) {
			host.setError(ErrorType.DID_NOT_USE_EXISTING_LETTERS);
			return false;
		}
		return true;
	}
	
	public boolean dictionaryLegal(Word w) {
		try{
            this.socket=new Socket("localhost", portToServer);
            this.outToServer=new PrintWriter(socket.getOutputStream());
            this.inFromServer=new Scanner(socket.getInputStream());
        }catch (Exception e){
            System.out.println("dictionaryLegal: Could not connect to server");
        }
		String word="";
		for(Tile t: w.getTiles())
		{
			word=word+t.letter;
		}
		System.out.println("Sending to server "+word);
		// Check if the word is in the dictionary, using the server
        String books = "t1.txt";
        outToServer.println("Q,"+books+","+word);
        outToServer.flush();
		System.out.println("sent to server");

        // Get the response from the server
        String response=inFromServer.next();
		System.out.println("RESPONDED " + response);
        // Parse the response
        Boolean result = Boolean.parseBoolean(response);
		if(!result) {
			host.setError(ErrorType.NOT_A_WORD);
		}
		return result;
	}
	
	public ArrayList<Word> getWords(Word w){
		ArrayList<Word> words = new ArrayList<Word>();
		words.add(w);
		if(w.getVertical()) {
			int temp2;
			for(int i=w.getRow(); i<w.getRow()+w.getTiles().length;i++) {
				int temp = w.getCol();
				if(board[i][w.getCol()]==null) {
				ArrayList<Tile> tileTemp = new ArrayList<Tile>();

				temp2=temp;
				while(temp-1>=0 && board[i][temp-1]!=null) {
					temp = temp-1;
				}
				int temp3=temp;
				while(temp<board.length && (board[i][temp]!=null || temp==w.getCol())) {
					if(temp==w.getCol()) {
						tileTemp.add(w.getTiles()[i-w.getRow()]);
					}
					else {
						tileTemp.add(board[i][temp]);
					}
					temp++;
				}
				if(tileTemp.size()>1) {
					Tile[] tList = new Tile[tileTemp.size()];
					Word wNew;
					for(int j=0; j<tileTemp.size(); j++) {
						tList[j] = tileTemp.get(j);
					}
					wNew=new Word(tList,i,temp3,false);
					words.add(wNew);
				}
			}
			int tempI = w.getRow();
			ArrayList<Tile> tileTemp = new ArrayList<Tile>();
			int tempI2 = tempI;
			while(tempI-1>=0 && board[tempI-1][w.getCol()]!=null) {
				tempI--;
			}
			if(tempI<tempI2) {
			while(tempI<board.length && (board[tempI][w.getCol()]!=null || (tempI>=w.getRow() && (tempI<w.getRow()+w.getTiles().length)))) {
				if((tempI>=w.getRow() && (tempI<w.getRow()+w.getTiles().length))) {
					tileTemp.add(w.getTiles()[tempI-w.getRow()]);
				}
				else {
					tileTemp.add(board[tempI][w.getCol()]);
				}
				tempI++;
			}
			if(tileTemp.size()>1) {
				Tile[] tList = new Tile[tileTemp.size()];
				Word wNew;
				for(int j=0; j<tileTemp.size(); j++) {
					tList[j] = tileTemp.get(j);
				}
				wNew=new Word(tList,tempI2,w.getCol(),true);
				words.add(wNew);
			}
			}
			}
		}
		
		else  {
			
			int temp2;
			for(int i=w.getCol(); i<w.getCol()+w.getTiles().length;i++) {
				int temp = w.getRow();
				if(board[w.getRow()][i]==null) {
				ArrayList<Tile> tileTemp = new ArrayList<Tile>();
				temp2=temp;

				while(temp>=1 && board[temp-1][i]!=null) {
					
					temp = temp-1;
				}
				int temp3 = temp;
				while(temp<board.length && (board[temp][i]!=null || temp==w.getRow())) {
					if(temp==w.getRow()) {
						tileTemp.add(w.getTiles()[i-w.getCol()]);
					}
					else {
						tileTemp.add(board[temp][i]);
					}
					temp++;
				}
				if(tileTemp.size()>1) {
					Tile[] tList = new Tile[tileTemp.size()];
					Word wNew;
					for(int j=0; j<tileTemp.size(); j++) {
						tList[j] = tileTemp.get(j);
					}
					wNew=new Word(tList,temp3,i,true);
					words.add(wNew);
				}
			}
			int tempI = w.getCol();
			ArrayList<Tile> tileTemp = new ArrayList<Tile>();
			int tempI2 = tempI;
			while(tempI-1>=0 && board[w.getRow()][tempI-1]!=null) {
				tempI--;
			}
			
			if(tempI<tempI2) {
			while(tempI<board.length && (board[w.getRow()][tempI]!=null || (tempI>=w.getCol() && (tempI<w.getCol()+w.getTiles().length)))) {
				
				if((tempI>=w.getCol() && (tempI<w.getCol()+w.getTiles().length))) {
					tileTemp.add(w.getTiles()[tempI-w.getCol()]);
				}
				else {
					tileTemp.add(board[w.getRow()][tempI]);
				}
				tempI++;
			}
			if(tileTemp.size()>1) {
				Tile[] tList = new Tile[tileTemp.size()];
				Word wNew;
				for(int j=0; j<tileTemp.size(); j++) {
					tList[j] = tileTemp.get(j);
				}
				wNew=new Word(tList,w.getRow(),tempI2,false);
				words.add(wNew);
			}
			}
		}
		}
		
		return words;
	}
	
	public int getScore(Word w) {
		int score = 0;
		
		if(w.getVertical()) {
			int counter = 1;
			for(int i = 0; i <w.getTiles().length; i++) {
				score+=w.getTiles()[i].score * letterMult[i+w.getRow()][w.getCol()];
				if(board[i+w.getRow()][w.getCol()]==null) {
					counter*=wordMult[i+w.getRow()][w.getCol()];
				}
			}
			score*=counter;
		}
		else {
			int counter = 1;
			for(int i = 0; i <w.getTiles().length; i++) {
				score+=w.getTiles()[i].score * letterMult[w.getRow()][w.getCol()+i];
				if(board[w.getRow()][i+w.getCol()]==null) {
					counter *=wordMult[w.getRow()][w.getCol()+i];
				}
			}
			score*=counter;
		}
		return score;
	}
	
	public int tryPlaceWord(Word w) {
		System.out.println("start tryPlaceWord");
		for(int i = 0; i<w.getTiles().length; i++) {
			if(w.getTiles()[i]==null) {
				if(w.getVertical()) {
					if(board[w.getRow()+i][w.getCol()]==null) {
						host.setError(ErrorType.BAD_LOCATION);
						return 0;
					}
					w.setTile(i,board[w.getRow()+i][w.getCol()]);
				}
				else {
					if(board[w.getRow()][i+w.getCol()]==null) {
						host.setError(ErrorType.BAD_LOCATION);
						return 0;
					}
					w.setTile(i, board[w.getRow()][i+w.getCol()]);
				}
				
			}
		}
			
		int score = 0;
		if(!boardLegal(w)) {
			return 0;
		}
		ArrayList<Word> wordArr = getWords(w);
		for(int i = 0; i<wordArr.size(); i++) {
			if(!dictionaryLegal(wordArr.get(i))) {
				return 0;
			}
			else {
				score = score + getScore(wordArr.get(i));
			}
		}
		for(int i = 0; i<w.getTiles().length; i++) {
			if(w.getVertical()) {
				board[w.getRow()+i][w.getCol()]=w.getTiles()[i];
			}
			else {
				board[w.getRow()][w.getCol()+i]=w.getTiles()[i];
			}
		}
		return score;
	}

	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}