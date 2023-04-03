package test;

public class BookScrabbleHandler {
    //clientHandler
    ClientHandler clientHandler;

    public BookScrabbleHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        clientHandler.handleClient(inFromClient, outToClient);
    }
}
