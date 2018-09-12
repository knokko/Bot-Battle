package nl.knokko.client.connection.io;

import java.io.IOException;

public interface ClientConnectionIO {
	
	void connect(String ip, int port) throws IOException;
	
	void sendToServer(byte... bytes) throws IOException;
	
	byte[] readFromServer() throws IOException;
	
	void close();
}
