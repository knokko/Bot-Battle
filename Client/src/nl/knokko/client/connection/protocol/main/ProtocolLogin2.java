package nl.knokko.client.connection.protocol.main;

import nl.knokko.client.Client;
import nl.knokko.client.connection.listener.ReceiveProtocol;
import nl.knokko.client.connection.state.ClientMainConnectionState;
import nl.knokko.client.gui.menu.MenuLogin;
import nl.knokko.connection.ConnectionCode;
import nl.knokko.util.bits.BitInput;

public class ProtocolLogin2 implements ReceiveProtocol {

	@Override
	public void process(BitInput input) {
		if(Client.getMainConnectionState().auth().getAuthState() != ClientMainConnectionState.Auth.AUTH_STATE_LOGIN_2)
			throw new IllegalStateException();
		boolean result = input.readBoolean();
		if(result){
			//TODO read all account info the server sent
			Client.getMainConnectionState().auth().setAuthState(ClientMainConnectionState.Auth.AUTH_STATE_LOGGED_IN);
		}
		else {
			Client.getMainConnectionState().auth().setAuthState(ClientMainConnectionState.Auth.AUTH_STATE_NOTHING);
			MenuLogin.getInstance().setError(ConnectionCode.StC.Auth.LoginFail.asString(input.readByte()));
		}
	}
}