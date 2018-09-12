package nl.knokko.server.connection.handler;

import nl.knokko.server.connection.listener.ServerConnectionListener;
import nl.knokko.server.connection.listener.protocol.MainReceiveProtocol;
import nl.knokko.server.connection.listener.protocol.ProtocolAuthenticate;
import nl.knokko.server.main.MainServer;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.ByteArrayBitInput;

import static nl.knokko.connection.ConnectionCode.StC.Kick.*;
import static nl.knokko.connection.ConnectionCode.CtS.*;

public class MainServerConnectionListener implements ServerConnectionListener {
	
	private static final MainReceiveProtocol[] PROTOCOLS;
	
	static {
		PROTOCOLS = new MainReceiveProtocol[4];
		register(Auth.CODE, new ProtocolAuthenticate());
	}
	
	private static void register(byte id, MainReceiveProtocol protocol){
		PROTOCOLS[id - Byte.MIN_VALUE] = protocol;
	}
	
	private final MainServerConnectionHandler handler;
	
	public MainServerConnectionListener(MainServerConnectionHandler handler){
		this.handler = handler;
	}

	public void process(byte[] inputArray) {
		if(handler.getState().isClosed())
			return;
		if(inputArray.length == 0){
			handler.getSpeaker().closeConnection(CODE_CORRUPT);
			return;
		}
		try {
			BitInput input = new ByteArrayBitInput(inputArray);
			PROTOCOLS[input.readByte() - Byte.MIN_VALUE].process(input, handler);
			/*
			BitInput input = new BooleanArrayBitInput(inputArray);
			byte type = input.readByte();
			if(type == MESSAGE_LOGOUT)
				closeConnection(KICK_CODE_LOGGED_OUT);
			else if(type == MESSAGE_LOGIN){
				if(account != null || realmState != RealmState.NOTHING){
					closeConnection(KICK_CODE_CORRUPT);
					return;
				}
				Username name = new Username(input);
				if(!MainServer.getAccountManager().hasUsername(name)){
					connection.sendToClient(MESSAGE_LOGIN_FAIL, LOGIN_CODE_NO_NAME);
					return;
				}
				if(MainServer.getAccountManager().isOnline(name)){
					connection.sendToClient(MESSAGE_LOGIN_FAIL, LOGIN_CODE_ALREADY);
					return;
				}
				ServerPassword password = ServerPassword.fromClientInput(input);
				try {
					account = MainServer.getAccountManager().login(name, password);
					connection.sendToClient(MESSAGE_LOGIN);
				} catch(IllegalArgumentException ex){
					connection.sendToClient(MESSAGE_LOGIN_FAIL, LOGIN_CODE_WRONG_PASSWORD);
				}
			}
			else if(type == MESSAGE_REGISTER){
				if(account != null || realmState != RealmState.NOTHING){
					closeConnection(KICK_CODE_CORRUPT);
					return;
				}
				Username name = new Username(input);
				if(MainServer.getAccountManager().hasUsername(name)){
					connection.sendToClient(MESSAGE_REGISTER_FAIL, REGISTER_CODE_NAME_IN_USE);
					return;
				}
				account = MainServer.getAccountManager().createAccount(name, ServerPassword.fromClientInput(input));
			}
			else if(type == MESSAGE_ENABLE_REALM){
				if(account != null || (realmState != RealmState.NOTHING && realmState != RealmState.TRYING)){
					closeConnection(KICK_CODE_CORRUPT);
					return;
				}
				RealmName realmName = new RealmName(input);
				byte result = MainServer.getRealmManager().requestEnable(this, realmName, new RealmPassword(input), new RealmLocation(connection.getHost(), connection.getPort()));
				if(result == ENABLE_NO_NAME || result == ENABLE_WRONG_PASSWORD || result == ENABLE_ALREADY || result == ENABLE_MOVE_BUSY || result == ENABLE_MOVE_AUTO_DENIED)
					realmState = RealmState.TRYING;
				if(result == ENABLE_NEW_HOST)
					realmState = RealmState.WAITING;
				if(result == ENABLE_SUCCEED){
					realmState = RealmState.ENABLED;
					realm = realmName;
				}
				connection.sendToClient(MESSAGE_ENABLE_REALM, result);
			}
			else if(type == MESSAGE_CREATE_REALM){
				if(account != null || (realmState != RealmState.NOTHING && realmState != RealmState.TRYING)){
					closeConnection(KICK_CODE_CORRUPT);
					return;
				}
				byte result = MainServer.getRealmManager().requestCreate(this, new RealmName(input), new RealmPassword(input), new RealmLocation(connection.getHost(), connection.getPort()));
				if(result == CREATE_NAME_EXISTS || result == CREATE_AUTO_DENIED || result == CREATE_BUSY)
					realmState = RealmState.TRYING;
				if(result == CREATE_WAITING)
					realmState = RealmState.WAITING;
				connection.sendToClient(MESSAGE_CREATE_REALM, result);
			}
			else if(type == MESSAGE_DISABLE_REALM){
				if(account != null || realm == null){
					closeConnection(KICK_CODE_CORRUPT);
					return;
				}
				closeConnection(MESSAGE_LOGOUT);
			}
			else
				closeConnection(KICK_CODE_CORRUPT);
			*/
		} catch(Exception ex){
			ex.printStackTrace(MainServer.getConsole().getOutput());
			handler.getSpeaker().closeConnection(CODE_CORRUPT);
		}
	}
}
