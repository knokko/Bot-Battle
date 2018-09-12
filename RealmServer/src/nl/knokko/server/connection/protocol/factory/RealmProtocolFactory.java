package nl.knokko.server.connection.protocol.factory;

import nl.knokko.server.connection.io.ServerConnectionIO;
import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.server.connection.listener.factory.ListenerFactory;
import nl.knokko.server.connection.protocol.RealmServerConnectionProtocol;

public class RealmProtocolFactory implements ListenerFactory {

	public RealmProtocolFactory() {}

	@Override
	public ServerConnectionListener createClientListener(ServerConnectionIO io) {
		return new RealmServerConnectionProtocol(io);
	}

}
