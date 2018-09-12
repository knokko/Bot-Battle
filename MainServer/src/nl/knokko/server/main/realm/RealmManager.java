package nl.knokko.server.main.realm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.connection.realm.RealmName;
import nl.knokko.server.connection.code.RealmPassword;
import nl.knokko.server.connection.handler.MainServerConnectionHandler;
import nl.knokko.server.main.MainServer;
import nl.knokko.util.bits.BitInput;
import static nl.knokko.server.connection.code.RealmConnectionCode.*;

public class RealmManager {
	
	private final List<RealmAccount> activeRealms;
	
	private AccountToCreate accountToCreate;
	
	private AccountToMove accountToMove;
	
	private boolean denyNew;
	private boolean denyMove;

	public RealmManager() {
		activeRealms = new ArrayList<RealmAccount>(5);
	}
	
	public RealmAccount getRealm(RealmName name) throws IOException {
		RealmAccount ac = getActiveRealm(name);
		if(ac != null)
			return ac;
		BitInput input = MainServer.getFileManager().openInput("realms/" + name + ".rlm");
		RealmAccount realm = new RealmAccount(input);
		input.terminate();
		return realm;
	}
	
	public byte requestEnable(MainServerConnectionHandler protocol, RealmName name, RealmPassword pass, RealmLocation host){
		try {
			if(getActiveRealm(name) != null)
				return ENABLE_ALREADY;
			RealmAccount account = getRealm(name);
			if(!account.matchesPassword(pass))
				return ENABLE_WRONG_PASSWORD;
			if(!account.matchesHost(host)){
				if(denyMove)
					return ENABLE_MOVE_AUTO_DENIED;
				if(accountToMove != null)
					return ENABLE_MOVE_BUSY;
				accountToMove = new AccountToMove(account, host, protocol);
				MainServer.getConsole().println("The realm " + name + " wants to move to the address " + host);
				MainServer.getConsole().println("Use 'realm move approve' or 'realm move deny'");
				return ENABLE_NEW_HOST;
			}
			activeRealms.add(account);
			return ENABLE_SUCCEED;
		} catch(IOException ex){
			return ENABLE_NO_NAME;
		}
	}
	
	public byte requestCreate(MainServerConnectionHandler protocol, RealmName name, RealmPassword pass, RealmLocation host){
		if(denyNew)
			return CREATE_AUTO_DENIED;
		if(accountToCreate != null)
			return CREATE_BUSY;
		try {
			if(getRealm(name) != null)
				return CREATE_NAME_EXISTS;
		} catch(IOException ex){}
		accountToCreate = new AccountToCreate(new RealmAccount(name, pass, host), protocol);
		MainServer.getConsole().println("A new realm tries to register.");
		MainServer.getConsole().println("The name is " + name + "and the host location is " + host);
		MainServer.getConsole().println("The password is " + pass);
		MainServer.getConsole().println("Use 'realm create approve' or 'realm create deny' to send the answer.");
		return CREATE_WAITING;
	}
	
	public RealmAccount getActiveRealm(RealmName name){
		for(int i = 0; i < activeRealms.size(); i++){
			RealmAccount realm = activeRealms.get(i);
			if(realm.getName().equals(name))
				return realm;
		}
		return null;
	}
	
	public void disable(RealmName name){
		Iterator<RealmAccount> iterator = activeRealms.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getName().equals(name)){
				iterator.remove();
				return;
			}
		}
	}
	
	public void autoDenyNew(){
		denyNew = true;
	}
	
	public void manualDenyNew(){
		denyNew = false;
	}
	
	public void autoDenyMove(){
		denyMove = true;
	}
	
	public void manualDenyMove(){
		denyMove = false;
	}
	
	public void directChangeLocation(RealmName name, RealmLocation host){
		try {
			RealmAccount realm = getRealm(name);
			realm.moveHost(host);
			activeRealms.add(realm);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void cancelRequest(RealmName name){
		if(accountToCreate != null && accountToCreate.account.getName().equals(name)){
			accountToCreate = null;
			MainServer.getConsole().println("The request for creating a realm account has been cancelled.");
		}
		if(accountToMove != null && accountToMove.account.getName().equals(name)){
			accountToMove = null;
			MainServer.getConsole().println("The request for moving a realm has been cancelled.");
		}
	}
	
	public void failLocation(RealmName name){
		MainServer.getConsole().println("The realm server " + name + " can't be reached by the server and has been disabled.");
	}
	
	public String approveCreation(){
		if(accountToCreate == null)
			return "No realm account is waiting for your permission.";
		accountToCreate.handler.getSpeaker().verifyRealmAddress(accountToCreate.account.getName(), accountToCreate.account.getHost());
		RealmName name = accountToCreate.account.getName();
		accountToCreate = null;
		return "The realm " + name + " will be created, when it passes the server test.";
	}
	
	public String denyCreation(){
		if(accountToCreate == null)
			return "No realm account is waiting for your permission.";
		RealmName name = accountToCreate.account.getName();
		accountToCreate.handler.getSpeaker().denyRequest();
		accountToCreate = null;
		return "The creation of realm " + name + " has been denied.";
	}
	
	public String approveMove(){
		if(accountToMove == null)
			return "No realm account requested to be moved.";
		RealmName name = accountToMove.account.getName();
		RealmLocation loc = accountToMove.newAddress;
		accountToMove.handler.getSpeaker().verifyRealmAddress(name, loc);
		accountToMove = null;
		return "The realm " + name + " will move to " + loc + ", when is passes the server test.";
	}
	
	public String denyMove(){
		if(accountToMove == null)
			return "No realm account requested to be moved.";
		RealmName name = accountToMove.account.getName();
		RealmLocation loc = accountToMove.newAddress;
		accountToMove.handler.getSpeaker().denyRequest();
		accountToMove = null;
		return "You refused to move the realm " + name + " to " + loc;
	}
	
	private static class AccountToCreate {
		
		private final RealmAccount account;
		private final MainServerConnectionHandler handler;
		
		private AccountToCreate(RealmAccount account, MainServerConnectionHandler handler){
			this.account = account;
			this.handler = handler;
		}
	}
	
	private static class AccountToMove {
		
		private final RealmAccount account;
		private final RealmLocation newAddress;
		private final MainServerConnectionHandler handler;
		
		private AccountToMove(RealmAccount account, RealmLocation newAddress, MainServerConnectionHandler handler){
			this.account = account;
			this.newAddress = newAddress;
			this.handler = handler;
		}
	}
}
