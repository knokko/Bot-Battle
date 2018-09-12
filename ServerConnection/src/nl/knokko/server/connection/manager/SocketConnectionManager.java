package nl.knokko.server.connection.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.server.connection.io.SocketConnectionIO;
import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.server.connection.listener.factory.ListenerFactory;

public class SocketConnectionManager implements ServerConnectionManager, Runnable {
	
	private ServerSocket socket;
	private PrintWriter logger;
	private ListenerFactory protFactory;
	
	private Collection<ServerConnectionListener> connections;
	
	private boolean stopped;

	public SocketConnectionManager(PrintWriter logger) {
		logger.println("Called socket connection manager constructor.");
		this.logger = logger;
		this.connections = new ArrayList<ServerConnectionListener>(40);
	}
	
	@Override
	public void start(int port, ListenerFactory protFactory) throws Exception {
		logger.println("The server is using a socket connection manager with port " + port);
		this.protFactory = protFactory;
		socket = new ServerSocket(port);
		logger.println("Opened server socket at " + InetAddress.getLocalHost().getHostAddress() + ":" + socket.getLocalPort());
		new Thread(this).start();
	}
	
	
	@Override
	public void run(){
		while(!stopped){
			try {
				Socket clientSocket = socket.accept();
				connections.add(protFactory.createClientListener(new SocketConnectionIO(clientSocket)));
				logger.println("Accepted connection with " + clientSocket);
			} catch(IOException ex){
				logger.println("An IO Error occured at the socket connection manager: " + ex.getMessage());
				logger.println("Stopping socket...");
				stop();
			}
		}
		logger.println("The server socket has been stopped.");
	}
	
	@Override
	public void stop() {
		stopped = true;
		closeSocket();
		logger.println("The stop method has been called.");
	}
	
	@Override
	public Collection<ServerConnectionListener> getAllConnections() {
		return connections;
	}
	
	@Override
	public ClientConnectionIO createClientConnection(RealmLocation location){
		return new nl.knokko.client.connection.io.SocketConnectionIO();
	}
	
	private void closeSocket(){
		try {
			socket.close();
		} catch(Exception ex){}
	}
}
