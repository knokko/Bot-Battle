package nl.knokko.util.bits;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends BitOutput {
	
	protected boolean[] subData;
	protected int subIndex;
	
	protected OutputStream output;

	public BitOutputStream(OutputStream output) {
		this.output = output;
	}

	@Override
	protected void addDirectBoolean(boolean value) {
		if(subData != null)
			subData[subIndex++] = value;
		else {
			subData = new boolean[]{value, false, false, false, false, false, false, false};
			subIndex = 1;
		}
		if(subIndex == 8){
			subIndex = 0;
			try {
				output.write(BitHelper.byteFromBinary(subData));
			} catch(IOException ex){
				throw new IllegalStateException(ex);
			}
			subData = null;
		}
	}

	@Override
	protected void addDirectByte(byte value) {
		try {
			if(subIndex == 0)
				output.write(value);
			else {
				boolean[] bValue = BitHelper.byteToBinary(value);
				int index = 0;
				for(; subIndex < 8; subIndex++)
					subData[subIndex] = bValue[index++];
				output.write(BitHelper.byteFromBinary(subData));
				subIndex = 0;
				subData = new boolean[8];
				for(; index < 8; index++)
					subData[subIndex++] = bValue[index];
			}
		} catch(IOException ex){
			throw new IllegalStateException(ex);
		}
	}
	
	@Override
	protected void addDirectBytes(byte... value){
		if(subIndex == 0){
			try {
				output.write(value);
			} catch(IOException ex){
				throw new IllegalStateException(ex);
			}
		}
		else
			super.addDirectBytes(value);
	}

	@Override
	public void increaseCapacity(int booleans) {}

	@Override
	public void terminate() {
		try {
			if(subData != null){
				subData = null;
				output.write(BitHelper.byteFromBinary(subData));//don't forget to send the last bits
			}
			subIndex = 0;
			output.close();
		} catch(IOException ex){}
	}
}
