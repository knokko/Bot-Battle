package nl.knokko.connection.realm;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class RealmLocation {
	
	private final String host;
	private final int port;
	
	public RealmLocation(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public RealmLocation(BitInput input){
		this.host = input.readJavaString();
		this.port = input.readInt();
	}
	
	@Override
	public String toString(){
		return host + ":" + port;
	}
	
	@Override
	public boolean equals(Object other){
		return other instanceof RealmLocation && matches((RealmLocation) other);
	}
	
	public void save(BitOutput output){
		output.addJavaString(host);
		output.addInt(port);
	}
	
	public boolean matches(RealmLocation other){
		return host.equals(other.host) && port == other.port;
	}
	
	public String getHostName(){
		return host;
	}
	
	public int getPort(){
		return port;
	}
}
