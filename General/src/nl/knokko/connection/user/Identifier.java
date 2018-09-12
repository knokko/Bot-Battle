package nl.knokko.connection.user;

public class Identifier implements Comparable<Identifier> {
	
	private long index;

	public Identifier(long index) {
		this.index = index;
	}
	
	@Override
	public String toString(){
		return "Account ID {" + index + "}";
	}
	
	@Override
	public int hashCode(){
		return (int) index;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Identifier)
			return index == ((Identifier)other).index;
		return false;
	}
	
	public int compareTo(Identifier other){
		if(index > other.index)
			return 1;
		if(index < other.index)
			return -1;
		return 0;
	}
	
	public long getIndex(){
		return index;
	}
}
