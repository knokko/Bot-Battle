package nl.knokko.server.connection.listener.factory;

import nl.knokko.server.connection.io.ServerConnectionIO;
import nl.knokko.server.connection.listener.ServerConnectionListener;

public interface ListenerFactory {
	
	ServerConnectionListener createClientListener(ServerConnectionIO io);
}