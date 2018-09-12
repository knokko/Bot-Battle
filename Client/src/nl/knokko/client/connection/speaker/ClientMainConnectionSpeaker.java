package nl.knokko.client.connection.speaker;

import java.io.IOException;

import nl.knokko.client.Client;
import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.client.connection.state.ClientMainConnectionState;
import nl.knokko.util.bits.BooleanArrayBitOutput;
import nl.knokko.util.bits.ByteArrayBitOutput;
import nl.knokko.util.hashing.ClientHashResult;
import nl.knokko.util.hashing.ClientHasher;
import nl.knokko.util.hashing.HashResult;
import nl.knokko.util.hashing.Hasher;
import nl.knokko.util.random.PseudoRandom;
import static nl.knokko.connection.ConnectionCode.CtS.*;

public class ClientMainConnectionSpeaker implements ClientConnectionSpeaker {
	
	private ClientConnectionIO output;
	
	private final ClientMainConnectionState state;
	
	private final AuthSpeaker auth;
	
	public ClientMainConnectionSpeaker(ClientMainConnectionState state){
		this.state = state;
		auth = new AuthSpeaker();
	}

	@Override
	public void setOutput(ClientConnectionIO output) {
		this.output = output;
	}
	
	public AuthSpeaker auth(){
		return auth;
	}
	
	private void send(byte... message){
		try {
			if(state.isConnected())
				output.sendToServer(message);
			else
				state.queueMessage(message);
		} catch(IOException io){
			Client.getMainLogger().println("Closed main connection because an IO error occured: " + io.getMessage());
			state.setClosed();
		}
	}
	
	public class AuthSpeaker {
		
		public void startLoginProcess(String username, String password){
			BooleanArrayBitOutput buffer = new BooleanArrayBitOutput();
			buffer.addBytes(Auth.CODE, Auth.CODE_LOGIN_1);
			buffer.addJavaString(username);
			byte[] message = buffer.getBytes();
			buffer.terminate();
			state.auth().setUsername(username);
			state.auth().setPassword(password);//needs to be stored for later in the login session
			send(message);
		}
		
		public void secondLoginProcess(){
			ClientHashResult result = ClientHasher.clientHash(state.auth().getPassword(), state.auth().getSalt());
			state.auth().clearPassword();
			state.auth().clearSalt();
			HashResult tempResult = Hasher.tempHash(result.getHashResult(), state.auth().getTempHasher1(), state.auth().getTempHasher2());
			state.auth().clearTempHasher();
			ByteArrayBitOutput buffer = new ByteArrayBitOutput(162);
			buffer.addBytes(Auth.CODE, Auth.CODE_LOGIN_2);
			buffer.addLongs(tempResult.get());
			buffer.addLongs(result.getEncryptor());
			send(buffer.getRawBytes());
		}
		
		public void register(String username, String password){
			//password.length + salt.length should be dividable through 40
			int saltLength = 40 - (password.length() - password.length() / 40 * 40);
			PseudoRandom random = new PseudoRandom();
			String salt = new String(random.nextChars(saltLength));
			ClientHashResult result = ClientHasher.clientHash(password, salt);
			HashResult encrypted = Hasher.encrypt(result.getHashResult(), result.getEncryptor());
			BooleanArrayBitOutput buffer = new BooleanArrayBitOutput(642 + username.length());//it's an estimate
			buffer.addBytes(Auth.CODE, Auth.CODE_REGISTER);
			buffer.addJavaString(username);
			buffer.addLongs(encrypted.get());
			send(buffer.getBytes());
		}
	}
}