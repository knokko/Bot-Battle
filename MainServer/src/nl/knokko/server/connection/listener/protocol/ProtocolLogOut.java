package nl.knokko.server.connection.listener.protocol;

import nl.knokko.server.connection.handler.MainServerConnectionHandler;
import nl.knokko.util.bits.BitInput;

public class ProtocolLogOut implements MainReceiveProtocol {

	@Override
	public void process(BitInput input, MainServerConnectionHandler handler) {
		handler.getSpeaker().closeConnection(nl.knokko.connection.ConnectionCode.StC.Kick.CODE_LOGGED_OUT);
	}
}