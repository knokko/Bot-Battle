package nl.knokko.client.connection.listener;

public interface ClientConnectionListener {
	
	void process(byte[] input);
}
