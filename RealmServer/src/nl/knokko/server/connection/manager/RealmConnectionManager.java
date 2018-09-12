package nl.knokko.server.connection.manager;

import java.util.Collection;

import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.server.connection.listener.factory.ListenerFactory;

public class RealmConnectionManager implements ServerConnectionManager {

	public RealmConnectionManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(int port, ListenerFactory protocols) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ServerConnectionListener> getAllConnections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientConnectionIO createClientConnection(RealmLocation location) {
		return new nl.knokko.client.connection.io.SocketConnectionIO();
	}
}
