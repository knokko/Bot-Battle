package nl.knokko.server.connection.handler;

import static nl.knokko.connection.ConnectionCode.StC.*;
import static nl.knokko.server.connection.code.RealmConnectionCode.MESSAGE_FAILED_REALM_TEST;
import static nl.knokko.server.connection.code.RealmConnectionCode.MESSAGE_TESTED_REALM;
import static nl.knokko.server.connection.code.RealmConnectionCode.REQUEST_APPROVED;
import static nl.knokko.server.connection.code.RealmConnectionCode.REQUEST_DENIED;

import java.io.IOException;

import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.connection.realm.RealmName;
import nl.knokko.server.connection.handler.MainServerConnectionState.RealmState;
import nl.knokko.server.connection.io.ServerConnectionIO;
import nl.knokko.server.main.MainServer;

public class MainServerConnectionSpeaker {
	
	private final MainServerConnectionHandler handler;
	private final MainServerConnectionState state;
	private final ServerConnectionIO connection;
	
	public MainServerConnectionSpeaker(MainServerConnectionHandler handler, ServerConnectionIO connection){
		this.handler = handler;
		this.state = handler.getState();
		this.connection = connection;
	}
	
	public MainServerConnectionHandler getHandler(){
		return handler;
	}
	
	public void verifyRealmAddress(RealmName name, RealmLocation address){
		try {
			state.setRealmState(RealmState.TESTING);
			state.setRealmName(name);
			connection.sendToClient(REQUEST_APPROVED);
			new Thread(new LocationVerifier(address)).start();
		} catch (IOException e) {
			closeConnection(Kick.CODE_IO_ERROR);
		}
	}
	
	public void denyRequest(){
		try {
			state.setRealmState(RealmState.TRYING);
			connection.sendToClient(REQUEST_DENIED);
		} catch(IOException ioex){
			closeConnection(Kick.CODE_IO_ERROR);
		}
	}
	
	public void failLogin(byte reason){
		try {
			connection.sendToClient(Auth.CODE, Auth.CODE_LOGIN_FAIL, reason);
		} catch(IOException ioex){
			closeConnection(Kick.CODE_IO_ERROR);
		}
	}
	
	private void confirmRealmAddress(RealmLocation address){
		if(handler.getState().getRealmState() == RealmState.TESTING){
			handler.getState().setRealmState(RealmState.ENABLED);
			MainServer.getRealmManager().directChangeLocation(handler.getState().getRealName(), address);
			try {
				connection.sendToClient(MESSAGE_TESTED_REALM);
			} catch(IOException ioex){
				closeConnection(Kick.CODE_IO_ERROR);
			}
		}
		else
			MainServer.getConsole().println("The testing process was interupted.");
	}
	
	private void failRealmAddress(){
		if(handler.getState().getRealmState() == RealmState.TESTING){
			MainServer.getRealmManager().failLocation(handler.getState().getRealName());
			try {
				connection.sendToClient(MESSAGE_FAILED_REALM_TEST);
			} catch(IOException ioex){
				closeConnection(Kick.CODE_IO_ERROR);
			}
		}
		else
			MainServer.getConsole().println("The server test failed and was interupted for realm " + handler.getState().getRealName());
	}
	
	public void closeConnection(byte reason){
		handler.getState().close();
		try {
			connection.sendToClient(Kick.CODE, reason);
		} catch (IOException ex) {}
		connection.close();
		if(handler.getState().getRealmState() == RealmState.WAITING)
			MainServer.getRealmManager().cancelRequest(handler.getState().getRealName());
		if(handler.getState().getRealmState() == RealmState.ENABLED)
			MainServer.getRealmManager().disable(handler.getState().getRealName());
	}
	
	private class LocationVerifier implements Runnable {
		
		private final ClientConnectionIO client;
		private final RealmLocation address;
		
		private LocationVerifier(RealmLocation address){
			this.client = MainServer.getConnectionManager().createClientConnection(address);
			this.address = address;
		}
		
		@Override
		public void run(){
			try {
				client.connect(address.getHostName(), address.getPort());
				client.sendToServer(MESSAGE_TESTED_REALM);
				byte[] received = client.readFromServer();
				if(received.length == 1 && received[0] == MESSAGE_TESTED_REALM)
					confirmRealmAddress(address);
				else
					failRealmAddress();
				client.close();
			} catch(IOException ioex){
				failRealmAddress();
				client.close();
			}
		}
	}
}