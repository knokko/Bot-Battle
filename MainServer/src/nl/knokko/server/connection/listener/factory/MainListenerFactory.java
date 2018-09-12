package nl.knokko.server.connection.listener.factory;

import nl.knokko.server.connection.handler.MainServerConnectionHandler;
import nl.knokko.server.connection.io.ServerConnectionIO;
import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.server.connection.listener.factory.ListenerFactory;

public class MainListenerFactory implements ListenerFactory {

	public ServerConnectionListener createClientListener(ServerConnectionIO io) {
		MainServerConnectionHandler handler = new MainServerConnectionHandler(io);
		//TODO add the handler to some kind of list
		return handler.getListener();
	}
}