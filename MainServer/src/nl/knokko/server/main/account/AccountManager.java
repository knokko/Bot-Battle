package nl.knokko.server.main.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import nl.knokko.connection.user.Identifier;
import nl.knokko.connection.user.PassModifier;
import nl.knokko.connection.user.Password;
import nl.knokko.connection.user.TempPassHasher;
import nl.knokko.connection.user.Username;
import nl.knokko.server.main.MainServer;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class AccountManager {
	
	private Map<Username,Identifier> nameMap;
	private Collection<Account> onlineAccounts;
	
	private long nextID;

	public AccountManager() {}
	
	public void onStart(){
		try {
			BitInput nameInput = MainServer.getFileManager().openInput("names.map");
			long bound = nameInput.readLong();
			nameMap = createNameMap(bound - Long.MIN_VALUE);
			for(long id = Long.MIN_VALUE; id < bound; id++)
				nameMap.put(new Username(nameInput), new Identifier(id));
			nameInput.terminate();
			nextID = bound;
		} catch(Exception ex){
			MainServer.getConsole().println("Couldn't load name map: " + ex.getLocalizedMessage());
			nameMap = createNameMap(0);
			nextID = Long.MIN_VALUE;
			MainServer.getConsole().println("Created a new name map");
		}
		onlineAccounts = createOnlinePlayerCollection();
	}
	
	public void onEnd(){
		for(Account account : onlineAccounts)
			saveAccount(account);
		onlineAccounts.clear();
		try {
			Username[] array = new Username[nameMap.size()];
			BitOutput nameOutput = MainServer.getFileManager().openOutput("names.map");
			nameOutput.addLong(array.length + Long.MIN_VALUE);
			Set<Entry<Username,Identifier>> set = nameMap.entrySet();
			for(Entry<Username,Identifier> entry : set)
				array[(int) (entry.getValue().getIndex() - Long.MIN_VALUE)] = entry.getKey();
			for(Username name : array)
				name.save(nameOutput);
			nameOutput.terminate();
		} catch(IOException ex){
			MainServer.getConsole().println("Can't save name map! " + ex.getLocalizedMessage());
		}
	}
	
	public Account createAccount(Username name, Password password, PassModifier mod){
		if(nameMap.get(name) != null)
			throw new IllegalArgumentException("Name (" + name + ") already in use!");
		Account account = new Account(new Identifier(nextID), name, password, mod);
		nameMap.put(name, account.getID());
		nextID++;
		onlineAccounts.add(account);
		return account;
	}
	
	public Account getAccount(Identifier id){
		if(id == null)
			throw new NullPointerException();
		try {
			BitInput input = MainServer.getFileManager().openInput("accounts/" + id.getIndex() + ".acc");
			Username name = new Username(input);
			Password password = Password.load(input);
			PassModifier mod = new PassModifier(input);
			input.terminate();
			return new Account(id, name, password, mod);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load player data with id " + id.getIndex() + ": " + e.getLocalizedMessage());
		}
	}
	
	/*
	public Account login(Username name, Password password, TempPassHasher hasher){
		Identifier id = getIdentifier(name);
		if(id == null)
			throw new IllegalArgumentException("There is no account with name " + name);
		try {
			BitInput input = MainServer.getFileManager().openInput("accounts/" + id.getIndex() + ".acc");
			new Username(input);
			//ServerPassword current = ServerPassword.fromDataInput(input);
			Password current = Password.load(input);
			if(!current.hashed(hasher).equals(password)){//TODO use a temp password hasher
				input.terminate();
				throw new IllegalArgumentException("Password is incorrect!");
			}
			PassModifier passMod = new PassModifier(input);
			Account account = new Account(id, name, password, passMod);
			onlineAccounts.add(account);
			return account;
		} catch(IOException ex){
			throw new RuntimeException("Failed to load player data with id " + id.getIndex() + ": " + ex.getLocalizedMessage());
		}
	}
	*/
	
	public void logOut(Account account){
		saveAccount(account);
		onlineAccounts.remove(account);
	}
	
	public boolean isOnline(Username name){
		return isOnline(getIdentifier(name));
	}
	
	public boolean isOnline(Identifier id){
		return onlineAccounts.contains(id);
	}
	
	public Identifier getIdentifier(Username name){
		if(nameMap == null)
			throw new IllegalStateException("nameMap has not yet been loaded");
		return nameMap.get(name);
	}
	
	public boolean hasUsername(Username name){
		return getIdentifier(name) != null;
	}
	
	protected Map<Username,Identifier> createNameMap(long capacity){
		return new TreeMap<Username,Identifier>();
	}
	
	protected Collection<Account> createOnlinePlayerCollection(){
		return new ArrayList<Account>();
	}
	
	protected void saveAccount(Account account){
		try {
			BitOutput output = MainServer.getFileManager().openOutput("accounts/" + account.getID().getIndex() + ".acc");
			account.getUsername().save(output);
			account.getPassword().save(output);
			output.terminate();
		} catch(IOException ex){
			MainServer.getConsole().println("Failed to save account " + account + ": " + ex.getLocalizedMessage());
		}
	}
}
