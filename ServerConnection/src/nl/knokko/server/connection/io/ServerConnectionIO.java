package nl.knokko.server.connection.io;

import java.io.IOException;

public interface ServerConnectionIO {
	
	void sendToClient(byte... bytes) throws IOException;
	
	byte[] readFromClient() throws IOException;
	
	void close();
	
	String getHost();
	
	int getPort();
}
