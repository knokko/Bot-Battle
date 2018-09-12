package nl.knokko.client.connection.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketConnectionIO implements ClientConnectionIO {
	
private static final int LENGTH = 254;
	
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	public void connect(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	public void sendToServer(byte... bytes) throws IOException {
		if(bytes.length <= LENGTH)
			out.write(bytes.length - 128);
		else {
			out.write(127);
			out.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
		}
		out.write(bytes);
	}

	public byte[] readFromServer() throws IOException {
		int length;
		byte first = (byte) in.read();
		if(first == 127){
			byte[] lengthArray = new byte[4];
			in.read(lengthArray);
			length = ByteBuffer.wrap(lengthArray).getInt();
		}
		else
			length = first + 128;
		byte[] data = new byte[length];
		in.read(data);
		return data;
	}

	public void close() {
		try {
			socket.close();
		} catch(IOException ex){}
	}
}
