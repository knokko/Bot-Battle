package nl.knokko.client.connection.listener;

import nl.knokko.util.bits.BitInput;

public interface ReceiveProtocol {
	
	void process(BitInput input);
}