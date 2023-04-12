package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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

					// handle the client parallelly as a new task of the thread pool
					executor.execute(()->{
						try {
							// create new instance of the client handler
                            Class<? extends ClientHandler> clientHandlerClass = this.ch.getClass();
                            ClientHandler currCh = clientHandlerClass.getDeclaredConstructor().newInstance();
							
                            currCh.handleClient(client.getInputStream(), client.getOutputStream()); // handle the client
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

}