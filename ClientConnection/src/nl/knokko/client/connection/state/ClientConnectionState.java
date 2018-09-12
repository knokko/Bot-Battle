package nl.knokko.client.connection.state;

public abstract class ClientConnectionState {
	
	private static final byte CONNECT_STATE_DEFAULT = 0;
	private static final byte CONNECT_STATE_CONNECTING = 1;
	private static final byte CONNECT_STATE_CONNECTED = 2;
	
	private byte connectState;
	
	public boolean isOpen(){
		return connectState != CONNECT_STATE_DEFAULT;
	}
	
	public boolean isConnecting(){
		return connectState == CONNECT_STATE_CONNECTING;
	}
	
	public boolean isConnected(){
		return connectState == CONNECT_STATE_CONNECTED;
	}
	
	public void setClosed(){
		connectState = CONNECT_STATE_DEFAULT;
	}
	
	public void setConnecting(){
		connectState = CONNECT_STATE_CONNECTING;
	}
	
	public void setConnected(){
		connectState = CONNECT_STATE_CONNECTED;
	}
}