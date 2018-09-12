package nl.knokko.server.main.account;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import nl.knokko.util.bits.BitHelper;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class ServerPassword {
	
	public static ServerPassword fromClientInput(BitInput input){
		return new ServerPassword(input.readLong(), input.readLong(), input.readLong(), input.readLong(), input.readLong());
	}
	
	public static ServerPassword fromDataInput(BitInput input){
		return new ServerPassword(input.readLong(), input.readLong(), input.readLong(), input.readLong(), input.readLong(),
				input.readLong(), input.readLong(), input.readLong(), input.readLong(), input.readLong());
	}
	
	private final long l1;
	private final long l2;
	private final long l3;
	private final long l4;
	private final long l5;
	private final long l6;
	private final long l7;
	private final long l8;
	private final long l9;
	private final long l10;

	public ServerPassword(long code1, long code2, long code3, long code4, long code5) {
		ByteBuffer buffer1 = ByteBuffer.allocate(40);
		buffer1.putLong(code1);
		buffer1.putLong(code2);
		buffer1.putLong(code3);
		buffer1.putLong(code4);
		buffer1.putLong(code5);
		buffer1.flip();
		SecureRandom random1 = new SecureRandom(buffer1.array());
		boolean[] ba1 = new boolean[2400];
		for(int i = 0; i < ba1.length; i++)
			ba1[i] = random1.nextBoolean();
		byte[] bytes2 = new byte[300];
		for(int i = 0; i < bytes2.length; i++)
			bytes2[i] = BitHelper.byteFromBinary(ba1, i * 8);
		SecureRandom random2 = new SecureRandom(bytes2);
		ByteBuffer buffer2 = ByteBuffer.allocate(80);
		for(int i = 0; i < 10; i++){
			buffer2.putInt(random2.nextInt());
			buffer2.putInt(random2.nextInt());
		}
		buffer2.flip();
		l1 = buffer2.getLong();
		l2 = buffer2.getLong();
		l3 = buffer2.getLong();
		l4 = buffer2.getLong();
		l5 = buffer2.getLong();
		l6 = buffer2.getLong();
		l7 = buffer2.getLong();
		l8 = buffer2.getLong();
		l9 = buffer2.getLong();
		l10 = buffer2.getLong();
	}
	
	public ServerPassword(long l1, long l2, long l3, long l4, long l5, long l6, long l7, long l8, long l9, long l10){
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		this.l5 = l5;
		this.l6 = l6;
		this.l7 = l7;
		this.l8 = l8;
		this.l9 = l9;
		this.l10 = l10;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof ServerPassword){
			int dif = 0;
			ServerPassword p = (ServerPassword) other;
			if(p.l1 != l1)
				dif++;
			if(p.l2 != l2)
				dif++;
			if(p.l3 != l3)
				dif++;
			if(p.l4 != l4)
				dif++;
			if(p.l5 != l5)
				dif++;
			if(p.l6 != l6)
				dif++;
			if(p.l7 != l7)
				dif++;
			if(p.l8 != l8)
				dif++;
			if(p.l9 != l9)
				dif++;
			if(p.l10 != l10)
				dif++;
			return dif == 0;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "{" + l1 + "," + l2 + "," + l3 + "," + l4 + "," + l5 + "," + l6 + "," + l7 + "," + l8 + "," + l9 + "," + l10 + "}";
	}
	
	public void save(BitOutput output){
		output.addLongs(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10);
	}
}
