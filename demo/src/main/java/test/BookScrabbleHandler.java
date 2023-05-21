package test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler{

    //reads the first line and parses it.
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        
        Scanner in = new Scanner(inFromClient);
        PrintWriter out = new PrintWriter(outToClient);
        System.out.println("handle client");
        String line = in.nextLine();
        String[] args = line.split(",");
        System.out.println("after next line");

        String[] booksAndWord = new String[args.length - 1];
        //get all the args except the first one
        for (int i = 1; i < args.length; i++) {
            booksAndWord[i - 1] = args[i];
        }
        
        if (args[0].equals("Q")) {
            System.out.println("got word "+booksAndWord[booksAndWord.length-1]);
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
        
        out.flush();
        in.close();
        out.close();
        
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }
}
