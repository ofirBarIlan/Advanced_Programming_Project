package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import model.Model.Result;
import test.ClientHandler;

public class GuestHandler implements ClientHandler{

    PrintWriter out;
	Scanner in;
    static Model host;

    public GuestHandler() {
    }

    public void setHost(Model h) {
        this.host = h;
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        
        System.out.println("handle client");

        in = new Scanner(inFromClient);
        out = new PrintWriter(outToClient);
        boolean close = false;
        while(!close)
        {
            String line = in.nextLine();
            String[] args = line.split(",");

            if (args[0].equals("joinGame")) {
                System.out.println("Join game");
                assert false;
                int roomNumber = Integer.parseInt(args[1]);
                String name = args[2];
                boolean success = this.host.joinGame(roomNumber, name);
                if (success) {
                    out.println("resp,true");
                } else {
                    out.println("resp,false");
                }
            } else if (args[0].equals("TryWord")) {
                String word = args[1];
                boolean dir = args[2].equals("Down");
                int row = Integer.parseInt(args[3]);
                int col = Integer.parseInt(args[4]);
                int[] pos = {row, col};
                String name = args[5];
                Result result = host.tryWord(word, dir, pos, name);
                out.println("resp,"+result.score + "," + result.errorType.ordinal());
            } else if (args[0].equals("Challenge")) {
                String word = args[1];
                String name = args[2];
                boolean success = host.challenge(word, name);
                if (success) {
                    out.println("resp,true");
                } else {
                    out.println("resp,false");
                }
            } else if (args[0].equals("giveUp")) {
                String name = args[1];
                host.giveUp(name);
            } else if (args[0].equals("close")) {
                close = true;
            }

            out.flush();
        }
        
    }

    @Override
    public void close() {
        in.close();
        out.close();
    }
}
