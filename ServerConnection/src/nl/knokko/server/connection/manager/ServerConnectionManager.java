package nl.knokko.server.connection.manager;

import java.util.Collection;

import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.server.connection.listener.factory.ListenerFactory;

public interface ServerConnectionManager {
	
	void start(int port, ListenerFactory protocols) throws Exception;
	
	void stop();
	
	Collection<ServerConnectionListener> getAllConnections();
	
	ClientConnectionIO createClientConnection(RealmLocation location);
}