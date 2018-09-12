package nl.knokko.server.connection.protocol;

import java.io.IOException;

import nl.knokko.server.connection.io.ServerConnectionIO;
import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BooleanArrayBitInput;
import static nl.knokko.connection.ConnectionCode.*;

public class RealmServerConnectionProtocol implements ServerConnectionListener {
	
	private final ServerConnectionIO connection;
	
	private boolean closed;

	public RealmServerConnectionProtocol(ServerConnectionIO connection) {
		this.connection = connection;
	}

	@Override
	public void process(byte[] inputArray) {
		if(closed)
			return;
		/*
		if(inputArray.length == 0){
			closeConnection(KICK_CODE_CORRUPT);
			return;
		}
		try {
			BitInput input = new BooleanArrayBitInput(inputArray);
			byte type = input.readByte();
			if(type == MESSAGE_LOGIN){
				
			}
			else if(type == MESSAGE_LOGOUT){
				
			}
			else
				closeConnection(KICK_CODE_CORRUPT);
		} catch(Throwable thr){
			closeConnection(KICK_CODE_CORRUPT);
		}
		*/
	}
	
	public void closeConnection(byte reason){
		/*
		closed = true;
		try {
			connection.sendToClient(MESSAGE_KICK, reason);
		} catch (IOException ex) {}
		connection.close();
		*/
	}
}
