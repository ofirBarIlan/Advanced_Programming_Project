package server;

import test.BookScrabbleHandler;
import test.*;

public class ServerRun {
    public static void main(String[] args){
    	boolean ok=true;
		int port=6200;
        ClientHandler ch = new BookScrabbleHandler();
		MyServer s=new MyServer(port, ch,3);
		int c = Thread.activeCount();
		s.start();
    }
}
