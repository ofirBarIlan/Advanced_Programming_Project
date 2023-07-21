package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import model.Model;

public class MyServer {

	int port;
	int maxThreads;
	volatile boolean stop;
	ClientHandler ch;
	ServerSocket server;
	ExecutorService executor;
	ArrayList<Socket> clients = new ArrayList<Socket>();
	
	// constructor of MyServer
	public MyServer(int port, ClientHandler ch, int maxThreads) {
		this.port=port;
		this.ch=ch;
		this.maxThreads=maxThreads;
	}
	
	// Creating the server's socket and start the server's operation in the background
	public void start() {
		stop=false;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		executor = Executors.newFixedThreadPool(this.maxThreads); // the thread pool that will handle the clients (the maximum number of clients that will be handled parallelly is maxThreads)
		new Thread(()->startsServer()).start(); //start the server's operation in the background (in new thread)
	}
	
	private void startsServer() {
		try {
			server.setSoTimeout(1000); // in every second the server will wait again for the customer (it will repeat to the while condition)
			while(!stop) {
				try {
					Socket client=server.accept(); // wait for a client
					URL url = new URL("http://localhost:"+port);
           			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           			connection.setRequestMethod("GET");

            // Send the request

					// handle the client parallelly as a new task of the thread pool
					executor.execute(()->{
						try {
							// create new instance of the client handler
                            Class<? extends ClientHandler> clientHandlerClass = this.ch.getClass();
                            ClientHandler currCh = clientHandlerClass.getDeclaredConstructor().newInstance();
							clients.add(client); // add the client to the clients list (for the notifyGuests method)
                            currCh.handleClient(client.getInputStream(), client.getOutputStream()); // handle the client
							clients.remove(client); // remove the client from the clients list (for the notifyGuests method)
                            currCh.close(); // close the current client handler
							client.close(); // close the client
						} catch (Exception e) {
							e.printStackTrace();
						}
						finally {
							if(!client.isClosed()) // if the client is not closed, so close it.
								try {
									client.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					});
					
				} catch(SocketTimeoutException e) {}
			}			
			
			executor.shutdown();
			
			// wait for tasks to complete execution 
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			server.close(); // close the server
		} catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			if(!executor.isTerminated()) // if the executor is not terminated after the shutdown command, so do shutdownNow
				executor.shutdownNow();
			if(!server.isClosed()) // if the server is not closed, so close it.
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	//close the server's operation in the background and afterwards close the server
	public void close() {
		stop=true;		
	}

	public void notifyGuests(String message) {
		for(Socket client : clients) {
			try {
				PrintWriter outToClient = new PrintWriter(client.getOutputStream());
				outToClient.println(message);
				outToClient.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}