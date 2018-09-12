package nl.knokko.util.bits;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends BitInput {
	
	private InputStream input;
	
	private boolean[] leftBits;
	private int leftIndex;

	public BitInputStream(InputStream input) {
		if(input == null)
			throw new NullPointerException();
		this.input = input;
	}

	@Override
	protected boolean readDirectBoolean() {
		try {
			if(leftIndex != 0){
				boolean result = leftBits[leftIndex];
				leftIndex++;
				if(leftIndex == 8){
					leftIndex = 0;
					leftBits = null;
				}
				return result;
			}
			leftBits = BitHelper.byteToBinary((byte) input.read());
			leftIndex = 1;
			return leftBits[0];
		} catch(IOException ex){
			throw new IllegalStateException(ex);
		}
	}

	@Override
	protected byte readDirectByte() {
		try {
			if(leftIndex == 0)
				return (byte) input.read();
			boolean[] newBools = BitHelper.byteToBinary((byte) input.read());
			boolean[] result = new boolean[8];
			int index = 0;
			for(;leftIndex < 8; leftIndex++)
				result[index++] = leftBits[leftIndex];
			leftIndex = 0;
			for(; index < 8; index++)
				result[index] = newBools[leftIndex++];
			leftBits = newBools;
			return BitHelper.byteFromBinary(result);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void increaseCapacity(int booleans) {}

	@Override
	public void terminate() {
		try {
			input.close();
			input = null;
			leftBits = null;
			leftIndex = -1;
		} catch (IOException e) {}
	}

	@Override
	public void skip(long amount) {
		try {
			if(leftIndex != 0){
				for(; leftIndex < 8; leftIndex++)
					amount--;
				leftBits = null;
				leftIndex = 0;
			}
			long bytes = amount / 8;
			leftIndex = (int) (amount - bytes * 8);
			input.skip(bytes);
			if(leftIndex != 0)
				leftBits = BitHelper.byteToBinary((byte) input.read());
		} catch(IOException ex){
			throw new IllegalStateException(ex);
		}
	}
	
	@Override
	public void readBytes(byte[] bytes, int startIndex, int amount){
		try {
			if(leftIndex == 0)
				input.read(bytes, startIndex, amount);
			else
				super.readBytes(bytes, startIndex, amount);
		} catch(IOException ex){
			throw new IllegalStateException(ex);
		}
	}
}
