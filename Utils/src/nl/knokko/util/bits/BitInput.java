package nl.knokko.util.bits;

public abstract class BitInput {
	
	static void checkBitCount(byte bits){
		if(bits < 0)
			throw new IllegalArgumentException("Number of bits ( + " + bits + ") can't be negative!");
		if(bits >= 64)
			throw new IllegalArgumentException("Number of bits ( + " + bits + ") can't be greater than 63!");
	}
	
	static long numberFromBinary(boolean[] bools, byte bits, boolean allowNegative){
		checkBitCount(bits);
		long number = 0;
		byte neg = (byte) (allowNegative ? 1 : 0);
		for(byte b = 0; b < bits; b++){
			if(bools[b + neg])
				number += get2Power((byte) (bits - b - 1));
		}
		if(allowNegative){
			if(!bools[0]){
				number = -number;
				number--;
			}
		}
		return number;
	}
	
	static long get2Power(byte index){
		long l = 1;
		for(byte b = 0; b < index; b++){
			l *= 2;
			if(l < 0)
				l = Long.MAX_VALUE;
		}
		return l;
	}
	
	static char makeChar(byte b0, byte b1) {
        return (char)((b1 << 8) | (b0 & 0xff));
    }
	
	static short makeShort(byte b0, byte b1) {
        return (short)((b1 << 8) | (b0 & 0xff));
    }
	
	static int makeInt(byte b0, byte b1, byte b2, byte b3) {
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }
	
	static long makeLong(byte b0, byte b1, byte b2, byte b3,byte b4, byte b5, byte b6, byte b7){
		return ((((long)b7       ) << 56) |
				(((long)b6 & 0xff) << 48) |
				(((long)b5 & 0xff) << 40) |
				(((long)b4 & 0xff) << 32) |
				(((long)b3 & 0xff) << 24) |
				(((long)b2 & 0xff) << 16) |
			(((long)b1 & 0xff) <<  8) |
			(((long)b0 & 0xff)      ));
	}
	
	static float fromInt(int i){
    	return Float.intBitsToFloat(i);
    }
	
	static double fromLong(long l){
    	return Double.longBitsToDouble(l);
    }
	
	protected abstract boolean readDirectBoolean();
	
	protected abstract byte readDirectByte();
	
	public abstract void increaseCapacity(int booleans);
	
	public abstract void terminate();
	
	public abstract void skip(long amount);
	
	protected char readDirectChar(){
		return makeChar(readDirectByte(), readDirectByte());
	}
	
	protected short readDirectShort(){
		return makeShort(readDirectByte(), readDirectByte());
	}
	
	protected int readDirectInt(){
		return makeInt(readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte());
	}
	
	protected float readDirectFloat(){
		return fromInt(readDirectInt());
	}
	
	protected long readDirectLong(){
		return makeLong(readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte());
	}
	
	protected double readDirectDouble(){
		return fromLong(readDirectLong());
	}
	
	public boolean readBoolean(){
		increaseCapacity(1);
		return readDirectBoolean();
	}
	
	public boolean[] readBooleans(int amount){
		boolean[] booleans = new boolean[amount];
		readBooleans(booleans);
		return booleans;
	}
	
	public void readBooleans(boolean[] booleans, int startIndex, int amount){
		increaseCapacity(amount);
		for(int i = 0; i < amount; i++)
			booleans[startIndex + i] = readDirectBoolean();
	}
	
	public void readBooleans(boolean[] booleans){
		readBooleans(booleans, 0, booleans.length);
	}
	
	public boolean[] readBooleanArray(){
		boolean[] booleans = new boolean[readInt()];
		readBooleans(booleans);
		return booleans;
	}
	
	public byte readByte(){
		increaseCapacity(8);
		return readDirectByte();
	}
	
	public byte[] readBytes(int amount){
		byte[] bytes = new byte[amount];
		readBytes(bytes);
		return bytes;
	}
	
	public void readBytes(byte[] bytes, int startIndex, int amount){
		increaseCapacity(amount * 8);
		for(int i = 0; i < amount; i++)
			bytes[startIndex + i] = readDirectByte();
	}
	
	public void readBytes(byte[] bytes){
		readBytes(bytes, 0, bytes.length);
	}
	
	public byte[] readByteArray(){
		byte[] bytes = new byte[readInt()];
		readBytes(bytes);
		return bytes;
	}
	
	public char readChar(){
		increaseCapacity(16);
		return makeChar(readDirectByte(), readDirectByte());
	}
	
	public char[] readChars(int amount){
		char[] chars = new char[amount];
		readChars(chars);
		return chars;
	}
	
	public void readChars(char[] chars, int startIndex, int length){
		increaseCapacity(length * 16);
		for(int i = 0; i < length; i++)
			chars[startIndex + i] = readDirectChar();
	}
	
	public void readChars(char[] chars){
		readChars(chars, 0, chars.length);
	}
	
	public char[] readCharArray(){
		char[] chars = new char[readInt()];
		readChars(chars);
		return chars;
	}
	
	public short readShort(){
		increaseCapacity(16);
		return makeShort(readDirectByte(), readDirectByte());
	}
	
	public short[] readShorts(int amount){
		short[] shorts = new short[amount];
		readShorts(shorts);
		return shorts;
	}
	
	public void readShorts(short[] shorts, int startIndex, int length){
		increaseCapacity(16 * length);
		for(int i = 0; i < length; i++)
			shorts[startIndex + i] = readDirectShort();
	}
	
	public void readShorts(short[] shorts){
		readShorts(shorts, 0, shorts.length);
	}
	
	public short[] readShortArray(){
		short[] shorts = new short[readInt()];
		readShorts(shorts);
		return shorts;
	}
	
	public int readInt(){
		increaseCapacity(32);
		return makeInt(readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte());
	}
	
	public int[] readInts(int amount){
		int[] ints = new int[amount];
		readInts(ints);
		return ints;
	}
	
	public void readInts(int[] ints, int startIndex, int length){
		increaseCapacity(32 * length);
		for(int i = 0; i < length; i++)
			ints[startIndex + i] = readDirectInt();
	}
	
	public void readInts(int[] ints){
		readInts(ints, 0, ints.length);
	}
	
	public int[] readIntArray(){
		int[] ints = new int[readInt()];
		readInts(ints);
		return ints;
	}
	
	public float readFloat(){
		return fromInt(readInt());
	}
	
	public float[] readFloats(int amount){
		float[] floats = new float[amount];
		readFloats(floats);
		return floats;
	}
	
	public void readFloats(float[] floats, int startIndex, int length){
		increaseCapacity(32 * length);
		for(int i = 0; i < length; i++)
			floats[startIndex + i] = readDirectFloat();
	}
	
	public void readFloats(float[] floats){
		readFloats(floats, 0, floats.length);
	}
	
	public float[] readFloatArray(){
		float[] floats = new float[readInt()];
		readFloats(floats);
		return floats;
	}
	
	public long readLong(){
		increaseCapacity(64);
		return makeLong(readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte(), readDirectByte());
	}
	
	public long[] readLongs(int amount){
		long[] longs = new long[amount];
		readLongs(longs);
		return longs;
	}
	
	public void readLongs(long[] longs, int startIndex, int length){
		increaseCapacity(64 * length);
		for(int i = 0; i < length; i++)
			longs[startIndex + i] = readDirectLong();
	}
	
	public void readLongs(long[] longs){
		readLongs(longs, 0, longs.length);
	}
	
	public long[] readLongArray(){
		long[] longs = new long[readInt()];
		readLongs(longs);
		return longs;
	}
	
	public double readDouble(){
		return fromLong(readLong());
	}
	
	public double[] readDoubles(int amount){
		double[] doubles = new double[amount];
		readDoubles(doubles);
		return doubles;
	}
	
	public void readDoubles(double[] doubles, int startIndex, int length){
		increaseCapacity(64 * length);
		for(int i = 0; i < length; i++)
			doubles[startIndex + i] = readDirectDouble();
	}
	
	public void readDoubles(double[] doubles){
		readDoubles(doubles, 0, doubles.length);
	}
	
	public double[] readDoubleArray(){
		double[] doubles = new double[readInt()];
		readDoubles(doubles);
		return doubles;
	}
	
	public long readNumber(byte bitCount, boolean allowNegative){
		byte size = bitCount;
		if(allowNegative)
			size++;
		long number = numberFromBinary(readBooleans(size), bitCount, allowNegative);
		return number;
	}
	
	public long readNumber(boolean allowNegative){
		byte bitCount = (byte) readNumber((byte) 6, false);
		return readNumber(bitCount, allowNegative);
	}
	
	public String readJavaString(){
		return new String(readCharArray());
	}
}
