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

    // Constructor
    public GuestHandler() {
    }

    // setter for host
    public void setHost(Model h) {
        this.host = h;
    }

    // handles the client
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        in = new Scanner(inFromClient);
        out = new PrintWriter(outToClient);
        boolean close = false;
        while(!close)
        {
            String line = in.nextLine();
            String[] args = line.split(",");

            if (args[0].equals("joinGame")) {
                assert false;
                int roomNumber = Integer.parseInt(args[1]);
                String name = args[2];
                boolean success = this.host.joinGame(roomNumber, name);
                if (success) {
                    out.println("resp,"+name+","+"true");
                } else {
                    out.println("resp,"+name+","+"false");
                }
            } else if (args[0].equals("TryWord")) {
                String word = args[1];
                boolean isRight = args[2].equals("Right");
                int row = Integer.parseInt(args[3]);
                int col = Integer.parseInt(args[4]);
                int[] pos = {row, col};
                String name = args[5];
                Result result = host.tryWord(word, isRight, pos, name);
                out.println("resp,"+name+","+result.score + "," + result.errorType.ordinal());
            } else if (args[0].equals("Challenge")) {
                String word = args[1];
                String name = args[2];
                String dir = args[3];
                int row = Integer.parseInt(args[4]);
                int col = Integer.parseInt(args[5]);
                boolean success = host.challenge(word, name, dir, row, col);
                if (success) {
                    out.println("resp,"+name+","+"true");
                } else {
                    out.println("resp,"+name+","+"false");
                }
            } else if (args[0].equals("giveUp")) {
                String name = args[1];
                host.giveUp(name);
            } else if (args[0].equals("close")) {
                close = true;
            } else if (args[0].equals("skip")){
                host.skipTurn();
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
