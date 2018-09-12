package nl.knokko.server.connection.listener;

public interface ServerConnectionListener {
	
	void process(byte[] input);
}