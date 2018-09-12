package nl.knokko.server.connection.handler;

import nl.knokko.server.connection.io.ServerConnectionIO;

public class MainServerConnectionHandler {
	
	private final MainServerConnectionState state;
	private final MainServerConnectionListener listener;
	private final MainServerConnectionSpeaker speaker;
	
	public MainServerConnectionHandler(ServerConnectionIO connection){
		state = new MainServerConnectionState(this);
		listener = new MainServerConnectionListener(this);
		speaker = new MainServerConnectionSpeaker(this, connection);
	}
	
	public MainServerConnectionState getState(){
		return state;
	}
	
	public MainServerConnectionListener getListener(){
		return listener;
	}
	
	public MainServerConnectionSpeaker getSpeaker(){
		return speaker;
	}
}