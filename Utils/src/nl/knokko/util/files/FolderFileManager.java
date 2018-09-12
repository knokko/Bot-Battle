package nl.knokko.util.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nl.knokko.util.bits.BitHelper;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;
import nl.knokko.util.bits.BitInputStream;
import nl.knokko.util.bits.BitOutputStream;
import nl.knokko.util.bits.BooleanArrayBitInput;

public class FolderFileManager implements FileManager {
	
	private final File folder;

	public FolderFileManager(File baseFolder) {
		folder = baseFolder;
		folder.mkdir();
	}

	public BitInput openInput(String path) throws IOException {
		return new BitInputStream(new FileInputStream(getPath(path)));
	}

	public BitOutput openOutput(String path) throws IOException {
		return new BitOutputStream(new FileOutputStream(getPath(path)));
	}

	public boolean[] getBits(String path) throws IOException {
		return new BooleanArrayBitInput(getBytes(path)).getAllBits();
	}

	public byte[] getBytes(String path) throws IOException {
		File file = new File(getPath(path));
		if(file.length() > Integer.MAX_VALUE)
			throw new IOException("File too large! (" + file.length() + ")");
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream input = new FileInputStream(file);
		input.read(bytes);
		input.close();
		return bytes;
	}

	public void setBits(boolean[] bits, String path) throws IOException {
		int size = bits.length / 8;
		if(size * 8 < bits.length)
			size++;
		byte[] bytes = new byte[size];
		for(int i = 0; i < bytes.length; i++)
			bytes[i] = BitHelper.byteFromBinary(bits, i * 8);
		setBytes(bytes, path);
	}

	public void setBytes(byte[] bytes, String path) throws IOException {
		FileOutputStream output = new FileOutputStream(getPath(path));
		output.write(bytes);
		output.close();
	}
	
	protected String getPath(String path){
		int lastSeparator = path.lastIndexOf("/");
		if(lastSeparator != -1)
			new File(folder + File.separator + path.substring(0, lastSeparator)).mkdirs();
		return folder + File.separator + path;
	}
}
