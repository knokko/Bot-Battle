package nl.knokko.server.main.account;

import nl.knokko.connection.user.Identifier;
import nl.knokko.connection.user.PassModifier;
import nl.knokko.connection.user.Password;
import nl.knokko.connection.user.Username;

public class Account {
	
	private Identifier id;
	private Username username;
	
	private Password password;
	private PassModifier passMod;
	
	Account(Identifier id, Username username, Password password, PassModifier passMod){
		this.id = id;
		this.username = username;
		this.password = password;
		this.passMod = passMod;
	}
	
	@Override
	public String toString(){
		return username + " (" + id + ")";
	}
	
	public Identifier getID(){
		return id;
	}
	
	public Username getUsername(){
		return username;
	}
	
	public Password getPassword(){
		return password;
	}
	
	public PassModifier getPasswordModifier(){
		return passMod;
	}
}
