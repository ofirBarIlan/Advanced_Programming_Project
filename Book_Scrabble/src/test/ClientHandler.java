package test;

import java.io.InputStream;
import java.io.OutputStream;
//Hi From tomer
public interface ClientHandler {
	void handleClient(InputStream inFromclient, OutputStream outToClient);
	void close();
	
}
