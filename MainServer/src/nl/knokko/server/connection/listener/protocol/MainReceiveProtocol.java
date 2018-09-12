package nl.knokko.server.connection.listener.protocol;

import nl.knokko.server.connection.handler.MainServerConnectionHandler;
import nl.knokko.util.bits.BitInput;

public interface MainReceiveProtocol {
	
	void process(BitInput input, MainServerConnectionHandler handler);
}