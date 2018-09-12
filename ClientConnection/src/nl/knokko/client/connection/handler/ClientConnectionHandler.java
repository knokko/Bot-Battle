package nl.knokko.client.connection.handler;

import java.io.IOException;
import java.io.PrintWriter;

import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.client.connection.listener.ClientConnectionListener;
import nl.knokko.client.connection.speaker.ClientConnectionSpeaker;
import nl.knokko.client.connection.state.ClientConnectionState;

public class ClientConnectionHandler implements Runnable {
	
	private final PrintWriter logger;
	
	private final ClientConnectionListener listener;
	private final ClientConnectionSpeaker speaker;
	private final ClientConnectionState state;
	private final ClientConnectionIO io;
	
	private String ip;
	private int port;

	public ClientConnectionHandler(PrintWriter logger, ClientConnectionListener listener, ClientConnectionSpeaker speaker, ClientConnectionState state, ClientConnectionIO io) {
		this.logger = logger;
		this.listener = listener;
		this.speaker = speaker;
		this.state = state;
		this.io = io;
	}

	public void start(String ip, int port) {
		this.ip = ip;
		this.port = port;
		state.setConnecting();
		new Thread(this).start();
	}
	
	public void stop(){
		state.setClosed();
		io.close();
	}
	
	public void run(){
		try {
			io.connect(ip, port);
			speaker.setOutput(io);
		} catch(IOException ex){
			logger.println("Failed to connect to address " + ip + ":" + port + " because " + ex.getMessage());
			stop();//just in case
			return;
		}
		state.setConnected();
		try {
			while(state.isConnected()){
				listener.process(io.readFromServer());
			}
		} catch(IOException ex){
			logger.println("An IO error occured while reading a message: " + ex.getLocalizedMessage());
			stop();
		}
	}
}
