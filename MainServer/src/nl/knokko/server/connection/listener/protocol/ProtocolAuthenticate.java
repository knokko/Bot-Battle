package nl.knokko.server.connection.listener.protocol;

import static nl.knokko.connection.ConnectionCode.CtS.Auth.*;

public class ProtocolAuthenticate extends ParentReceiveProtocol {

	public ProtocolAuthenticate() {
		super(3);
		register(CODE_LOGOUT, new ProtocolLogOut());
		register(CODE_LOGIN_1, new ProtocolLogIn1());
		//register(CODE_LOGIN_2, new ProtocolLogIn2());
		//register(CODE_REGISTER, new ProtocolRegister());
	}
}