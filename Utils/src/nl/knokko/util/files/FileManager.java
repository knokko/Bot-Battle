package nl.knokko.util.files;

import java.io.IOException;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public interface FileManager {
	
	BitInput openInput(String path) throws IOException;
	
	BitOutput openOutput(String path) throws IOException;
	
	boolean[] getBits(String path) throws IOException;
	
	byte[] getBytes(String path) throws IOException;
	
	void setBits(boolean[] bits, String path) throws IOException;
	
	void setBytes(byte[] bytes, String path) throws IOException;
}
