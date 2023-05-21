package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import test.ClientHandler;

public class GuestHandler implements ClientHandler{

    PrintWriter out;
	Scanner in;	
    static Model host;

    public GuestHandler() {
    }

    public void setHost(Model h) {
        this.host = h;
        if (host==null){
            System.out.println("host is null");
        }
        else{
            System.out.println("Host is " + host.getScore());
        }
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        
        System.out.println("handle client");
        if (this.host==null){
            System.out.println("host is null2");
        }
        in = new Scanner(inFromClient);
        out = new PrintWriter(outToClient);
       
        String line = in.nextLine();
        String[] args = line.split(",");

        if (args[0].equals("joinGame")) {
            System.out.println("Join game");
            assert false;
            int roomNumber = Integer.parseInt(args[1]);
            String name = args[2];
            boolean success = this.host.joinGame(roomNumber, name);
            if (success) {
                out.println("true");
            } else {
                out.println("false");
            }
        } else if (args[0].equals("TryWord")) {
            String word = args[1];
            boolean dir = args[2].equals("Down");
            int row = Integer.parseInt(args[3]);
            int col = Integer.parseInt(args[4]);
            int[] pos = {row, col};
            String name = args[5];
            boolean success = host.tryWord(word, dir, pos, name);
            if (success) {
                out.println("true");
            } else {
                out.println("false");
            }
        } else if (args[0].equals("Challenge")) {
            String word = args[1];
            String name = args[2];
            boolean success = host.challenge(word, name);
            if (success) {
                out.println("true");
            } else {
                out.println("false");
            }
        } else if (args[0].equals("giveUp")) {
            String name = args[1];
            host.giveUp(name);
        }
        
        out.flush();
    }

    @Override
    public void close() {
        in.close();
        out.close();
    }
}
