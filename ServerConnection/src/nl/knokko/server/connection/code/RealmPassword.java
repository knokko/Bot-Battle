package nl.knokko.server.connection.code;

import java.util.Random;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class RealmPassword {
	
	private final int i1;
	private final int i2;
	private final int i3;
	private final int i4;
	private final int i5;
	private final int i6;
	private final int i7;
	private final int i8;
	private final int i9;
	private final int i10;

	private RealmPassword(int l1, int l2, int l3, int l4, int l5, int i6, int i7, int i8, int i9, int i10) {
		this.i1 = l1;
		this.i2 = l2;
		this.i3 = l3;
		this.i4 = l4;
		this.i5 = l5;
		this.i6 = i6;
		this.i7 = i7;
		this.i8 = i8;
		this.i9 = i9;
		this.i10 = i10;
	}
	
	public RealmPassword(BitInput input){
		this(input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readInt(),
				input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readInt());
	}
	
	public RealmPassword(){
		this(new Random());
	}
	
	private RealmPassword(Random r){
		this(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt());
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof RealmPassword)
			return matches((RealmPassword) other);
		return false;
	}
	
	@Override
	public String toString(){
		return "(" + i1 + "," + i2 + "," + i3 + "," + i4 + "," + i5 + "," + i6 + "," + i7 + "," + i8 + "," + i9 + "," + i10 + ")";
	}
	
	public boolean matches(RealmPassword other){
		return i1 == other.i1 && i2 == other.i2 && i3 == other.i3 && i4 == other.i4 && i5 == other.i5 && i6 == other.i6 && i7 == other.i7 && i8 == other.i8 && i9 == other.i9 && i10 == other.i10;
	}
	
	public void save(BitOutput output){
		output.addInts(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10);
	}
}
