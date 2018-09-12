package nl.knokko.util.bits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BooleanArrayBitInput extends BitInput {
	
	/*
	public static byte byteFromBinary(boolean[] bools){
		byte b = 0;
		for(byte t = 0; t < 7; t++)
			if(bools[t])
				b += BooleanArrayBitOutput.BYTES[t];
		if(!bools[7]){
			b *= -1;
			b--;
		}
		return b;
	}
	
	public static byte byteFromBinary(boolean[] bools, int index){
		byte b = 0;
		for(byte t = 0; t < 7; t++)
			if(bools[t + index])
				b += BooleanArrayBitOutput.BYTES[t];
		if(!bools[index + 7]){
			b *= -1;
			b--;
		}
		return b;
	}
	*/
	
	private boolean[] data;
	
	private int index;
	private final int boundIndex;
	
	public BooleanArrayBitInput(boolean... data){
		this(data, 0, data.length);
	}

	public BooleanArrayBitInput(boolean[] data, int startIndex, int length) {
		this.data = data;
		this.index = startIndex;
		this.boundIndex = startIndex + length;
	}
	
	public BooleanArrayBitInput(byte[] bytes){
		boundIndex = Math.multiplyExact(bytes.length, 8);
		data = new boolean[boundIndex];
		int i = 0;
		for(byte b : bytes)
			BitHelper.byteToBinary(b, data, 8 * (i++));
	}
	
	public BooleanArrayBitInput(BooleanArrayBitOutput bits){
		this(bits.getRawData());
	}
	
	public static BooleanArrayBitInput fromFile(File file) throws IOException {
		if(Math.multiplyExact(file.length(), 8) > Integer.MAX_VALUE)
			throw new IOException("File too large! (" + file.length() + ")");
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream input = new FileInputStream(file);
		input.read(bytes);
		input.close();
		return new BooleanArrayBitInput(bytes);
	}

	@Override
	protected boolean readDirectBoolean() {
		return data[index++];
	}

	@Override
	protected byte readDirectByte() {
		index += 8;
		return BitHelper.byteFromBinary(data, index - 8);
	}

	@Override
	public void increaseCapacity(int booleans) {
		if(Math.addExact(index, booleans) >= boundIndex)
			throw new RuntimeException("End of input has been exceeded!");
	}

	@Override
	public void terminate() {
		data = null;
	}

	@Override
	public void skip(long amount) {
		if(amount < 0 || amount > Integer.MAX_VALUE)
			throw new IllegalArgumentException("Invalid amount: " + amount);
		index = Math.addExact(index, (int) amount);
	}
	
	public boolean[] getAllBits(){
		return data;
	}
}
