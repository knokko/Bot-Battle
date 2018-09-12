package nl.knokko.server.connection.listener.protocol;

import nl.knokko.server.connection.handler.MainServerConnectionHandler;
import nl.knokko.util.bits.BitInput;

public abstract class ParentReceiveProtocol implements MainReceiveProtocol {
	
	private final MainReceiveProtocol[] protocols;
	
	public ParentReceiveProtocol(int amount){
		protocols = new MainReceiveProtocol[amount];
	}
	
	protected void register(byte code, MainReceiveProtocol protocol){
		protocols[code - Byte.MIN_VALUE] = protocol;
	}

	@Override
	public void process(BitInput input, MainServerConnectionHandler handler) {
		protocols[input.readByte() - Byte.MIN_VALUE].process(input, handler);
	}
}