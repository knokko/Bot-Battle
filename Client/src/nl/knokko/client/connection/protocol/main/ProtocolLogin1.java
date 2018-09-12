package nl.knokko.client.connection.protocol.main;

import nl.knokko.client.Client;
import nl.knokko.client.connection.listener.ReceiveProtocol;
import nl.knokko.client.connection.state.ClientMainConnectionState;
import nl.knokko.util.bits.BitInput;

public class ProtocolLogin1 implements ReceiveProtocol {

	@Override
	public void process(BitInput input) {
		if(Client.getMainConnectionState().auth().getAuthState() != ClientMainConnectionState.Auth.AUTH_STATE_LOGIN_1)
			throw new IllegalStateException();
		String salt = input.readJavaString();
		long temp1 = input.readLong();
		long temp2 = input.readLong();
		Client.getMainConnectionState().auth().setTempHasher(temp1, temp2);
		Client.getMainConnectionState().auth().setSalt(salt);
		Client.getMainConnectionState().auth().setAuthState(ClientMainConnectionState.Auth.AUTH_STATE_LOGIN_2);
		Client.getMainConnectionSpeaker().auth().secondLoginProcess();
	}
}