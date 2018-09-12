package nl.knokko.server.connection.handler;

import nl.knokko.connection.realm.RealmName;
import nl.knokko.server.main.account.Account;

public class MainServerConnectionState {
	
	private final MainServerConnectionHandler handler;
	
	private Account account;
	private RealmName realm;
		
	private RealmState realmState;
		
	private boolean closed;
	
	public MainServerConnectionState(MainServerConnectionHandler handler){
		this.handler = handler;
	}
	
	public MainServerConnectionHandler getHandler(){
		return handler;
	}
	
	public Account getAccount(){
		return account;
	}
	
	public RealmState getRealmState(){
		return realmState;
	}
	
	public RealmName getRealName(){
		return realm;
	}
	
	public boolean isClosed(){
		return closed;
	}
	
	public void setAccount(Account acc){
		account = acc;
	}
	
	void setRealmState(RealmState newState){
		realmState = newState;
	}
	
	public void setRealmName(RealmName name){
		realm = name;
	}
	
	public void close(){
		closed = true;
	}
	
	public static enum RealmState {
		
		NOTHING,
		TRYING,
		WAITING,
		TESTING,
		ENABLED;
	}
}