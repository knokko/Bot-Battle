package nl.knokko.server.connection.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketConnectionIO implements ServerConnectionIO {
	
	private static final int LENGTH = 254;
	
	private final Socket socket;
	private final InputStream in;
	private final OutputStream out;

	public SocketConnectionIO(Socket clientSocket) throws IOException {
		socket = clientSocket;
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	public void sendToClient(byte... bytes) throws IOException {
		if(bytes.length <= LENGTH)
			out.write(bytes.length - 128);
		else {
			out.write(127);
			out.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
		}
		out.write(bytes);
	}

	public byte[] readFromClient() throws IOException {
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

	@Override
	public String getHost() {
		return socket.getInetAddress().getHostAddress();
	}

	@Override
	public int getPort() {
		return socket.getPort();
	}
}
