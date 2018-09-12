package nl.knokko.server.connection.listener.protocol;

import nl.knokko.connection.user.Username;
import nl.knokko.server.connection.handler.MainServerConnectionHandler;
import nl.knokko.server.connection.handler.MainServerConnectionState.RealmState;
import nl.knokko.server.main.MainServer;
import nl.knokko.util.bits.BitInput;
import static nl.knokko.connection.ConnectionCode.StC;

public class ProtocolLogIn1 implements MainReceiveProtocol {

	@Override
	public void process(BitInput input, MainServerConnectionHandler handler) {
		if(handler.getState().getAccount() != null || handler.getState().getRealmState() != RealmState.NOTHING){
			handler.getSpeaker().closeConnection(StC.Kick.CODE_CORRUPT);
			return;
		}
		Username name = new Username(input);
		if(!MainServer.getAccountManager().hasUsername(name)){
			handler.getSpeaker().failLogin(StC.Auth.LoginFail.CODE_NO_NAME);
			return;
		}
		if(MainServer.getAccountManager().isOnline(name)){
			handler.getSpeaker().failLogin(StC.Auth.LoginFail.CODE_ALREADY);
			return;
		}
		//MainServer.getAccountManager().getAccount(name);
		/*
		ServerPassword password = ServerPassword.fromClientInput(input);
		try {
			account = MainServer.getAccountManager().login(name, password);
			connection.sendToClient(MESSAGE_LOGIN);
		} catch(IllegalArgumentException ex){
			handler.getSpeaker().failLogin(StC.Auth.LoginFail.CODE_WRONG_PASSWORD);
		}
		*/
	}
}