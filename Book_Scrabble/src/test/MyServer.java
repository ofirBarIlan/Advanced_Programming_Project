package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyServer {

	int port;
	int maxThreads;
	volatile boolean stop;
	ClientHandler ch;
	ServerSocket server;
	ExecutorService executor;

	//create a queue of sockets
	Queue<Socket> queue = new ConcurrentLinkedQueue<Socket>();
	
	public MyServer(int port, ClientHandler ch, int maxThreads) {
		this.port=port;
		this.ch=ch;
		this.maxThreads=maxThreads;
	}
	
	public void start() {
		stop=false;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		executor = Executors.newFixedThreadPool(this.maxThreads + 1);
		executor.execute(()->startsServer());
		
	}
	
	private void startsServer() {
		try {
			//ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while(!stop) {
				try {
					Socket client=server.accept();
					queue.add(client);
					// execute the client handler in a new thread, using the two arguments functional interface
					executor.execute(()->handleClientInNewThread());
					ch.close();
					client.close();
				}catch(SocketTimeoutException e) {}
			}
//			server.close();
	}catch(IOException e) {
		e.printStackTrace();
		}
	}
	
	private Object handleClientInNewThread() {
		Socket client = (Socket) queue.poll();
		try {
			ch.handleClient(client.getInputStream(), client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		stop=true;
		executor.shutdown();
		
		// wait until all the tasks have completed
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// close the server
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
