package nl.knokko.util.bits;

import java.util.Arrays;

public class BooleanArrayBitOutput extends BitOutput {
	
	/*
	static final byte[] BYTES = new byte[]{64,32,16,8,4,2,1};
	
	static boolean[] byteToBinary(byte b){
		boolean[] bools = new boolean[8];
		if(b >= 0)
			bools[7] = true;
		else {
			b++;
			b *= -1;
		}
		for(byte t = 0; t < 7; t++){
			if(b >= BYTES[t]){
				b -= BYTES[t];
				bools[t] = true;
			}
		}
		return bools;
	}
	
	static void byteToBinary(boolean[] bools, int index, byte b){
		if(b >= 0)
			bools[index + 7] = true;
		else {
			b++;
			b *= -1;
		}
		for(byte t = 0; t < 7; t++){
			if(b >= BYTES[t]){
				b -= BYTES[t];
				bools[index + t] = true;
			}
		}
	}
	*/
	
	private boolean[] booleans;
	
	private int writeIndex;
	
	public BooleanArrayBitOutput(){
		this(800);
	}

	public BooleanArrayBitOutput(int startCapacity) {
		booleans = new boolean[startCapacity];
	}

	@Override
	protected void addDirectBoolean(boolean value) {
		booleans[writeIndex++] = value;
	}

	@Override
	protected void addDirectByte(byte value) {
		BitHelper.byteToBinary(value, booleans, writeIndex);
		writeIndex += 8;
	}

	@Override
	public void increaseCapacity(int booleans) {
		this.booleans = Arrays.copyOf(this.booleans, writeIndex + booleans);
	}
	
	@Override
	public void terminate(){}
	
	public boolean[] getRawData(){
		return booleans;
	}
	
	public byte[] toBytes(){
		int size = booleans.length / 8;
		if(8 * size < booleans.length)
			size++;//rounding upwards
		byte[] bytes = new byte[size];
		for(int i = 0; i < size; i++){
			if(i < size - 1)
				bytes[i] = BitHelper.byteFromBinary(booleans, i * 8);
			else {
				boolean[] last = new boolean[8];
				for(int j = 0; j + i * 8 < booleans.length; j++)
					last[j] = booleans[i * 8 + j];//the remaining booleans will stay false
				bytes[i] = BitHelper.byteFromBinary(last);
			}
		}
		return bytes;
	}
}
