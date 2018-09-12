package nl.knokko.client.connection.listener;

import nl.knokko.client.Client;
import nl.knokko.client.connection.listener.ClientConnectionListener;
import nl.knokko.client.connection.protocol.main.*;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.ByteArrayBitInput;
import static nl.knokko.connection.ConnectionCode.StC.*;

public class ClientMainConnectionListener implements ClientConnectionListener {
	
	private static final ReceiveProtocol[][] PROTOCOLS;
	
	static {
		PROTOCOLS = new ReceiveProtocol[1][];
		setSize(Auth.CODE, 3);
		register(Auth.CODE, Auth.CODE_LOGIN_1, new ProtocolLogin1());
		register(Auth.CODE, Auth.CODE_LOGIN_2, new ProtocolLogin2());
		register(Auth.CODE, Auth.CODE_REGISTER, new ProtocolRegister());
	}
	
	private static void setSize(byte domain, int size){
		PROTOCOLS[domain - Byte.MIN_VALUE] = new ReceiveProtocol[size];
	}
	
	private static void register(byte domain, byte id, ReceiveProtocol protocol){
		PROTOCOLS[domain - Byte.MIN_VALUE][id - Byte.MIN_VALUE] = protocol;
	}

	@Override
	public void process(byte[] message) {
		BitInput input = new ByteArrayBitInput(message);
		int domain = input.readByte() - Byte.MIN_VALUE;
		int id = input.readByte() - Byte.MIN_VALUE;
		if(domain < PROTOCOLS.length)
			PROTOCOLS[domain][id].process(input);
		else
			Client.getMainLogger().println("Server sent invalid domain: " + (domain + Byte.MIN_VALUE));
	}
}