package nl.knokko.server.main.realm;

import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.connection.realm.RealmName;
import nl.knokko.server.connection.code.RealmPassword;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class RealmAccount {
	
	private final RealmName name;
	private final RealmPassword password;
	private RealmLocation location;

	public RealmAccount(RealmName name, RealmPassword password, RealmLocation location) {
		this.name = name;
		this.password = password;
		this.location = location;
	}
	
	public RealmAccount(BitInput input){
		this.name = new RealmName(input);
		this.password = new RealmPassword(input);
		this.location = new RealmLocation(input);
	}
	
	@Override
	public String toString(){
		return name.toString();
	}
	
	public RealmName getName(){
		return name;
	}
	
	public RealmLocation getHost(){
		return location;
	}
	
	public void save(BitOutput output){
		name.save(output);
		password.save(output);
		location.save(output);
	}
	
	public boolean matchesPassword(RealmPassword password){
		return this.password.matches(password);
	}
	
	public boolean matchesHost(RealmLocation host){
		return location.matches(host);
	}
	
	public void moveHost(RealmLocation newLocation){
		location = newLocation;
	}
}