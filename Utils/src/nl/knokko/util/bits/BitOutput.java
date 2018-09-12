package nl.knokko.util.bits;

public abstract class BitOutput {
	
	static byte getRequiredBits(long number){
		if(number < 0)
			number = -(number + 1);
		long l = 1;
		byte b = 0;
		while(l < number){
			l *= 2;
			b++;
		}
		return b;
	}
	
	static void checkBitCount(byte bits){
		if(bits < 0)
			throw new IllegalArgumentException("Number of bits ( + " + bits + ") can't be negative!");
		if(bits >= 64)
			throw new IllegalArgumentException("Number of bits ( + " + bits + ") can't be greater than 63!");
	}
	
	static void checkOverflow(long number, byte bits){
		if(get2Power(bits) <= number || get2Power(bits) < -number)
			throw new IllegalArgumentException("You need more than " + bits + " bits to store the number " + number + "!");
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
	
	static boolean[] numberToBinary(long number, byte bits, boolean allowNegative){
		checkBitCount(bits);
		checkOverflow(number, bits);
		byte neg = (byte) (allowNegative ? 1 : 0);
		boolean[] bools = new boolean[bits + neg];
		if(allowNegative){
			if(number >= 0)
				bools[0] = true;
			else {
				//bools[0] will stay false
				number++;
				number = -number;
			}
		}
		for(byte b = 0; b < bits; b++){
			if(number >= get2Power((byte) (bits - b - 1))){
				number -= get2Power((byte) (bits - b - 1));
				bools[b + neg] = true;
			}
		}
		return bools;
	}
	
	static byte char1(char x) { return (byte)(x >> 8); }
    static byte char0(char x) { return (byte)(x     ); }
    
    static byte short1(short x) { return (byte)(x >> 8); }
    static byte short0(short x) { return (byte)(x     ); }
    
    static byte int3(int x) { return (byte)(x >> 24); }
    static byte int2(int x) { return (byte)(x >> 16); }
    static byte int1(int x) { return (byte)(x >>  8); }
    static byte int0(int x) { return (byte)(x      ); }
    
    static byte long7(long x) { return (byte)(x >> 56); }
    static byte long6(long x) { return (byte)(x >> 48); }
    static byte long5(long x) { return (byte)(x >> 40); }
    static byte long4(long x) { return (byte)(x >> 32); }
    static byte long3(long x) { return (byte)(x >> 24); }
    static byte long2(long x) { return (byte)(x >> 16); }
    static byte long1(long x) { return (byte)(x >>  8); }
    static byte long0(long x) { return (byte)(x      ); }
    
    static int fromFloat(float f){
    	return Float.floatToRawIntBits(f);
    }
    
    static long fromDouble(double d){
    	return Double.doubleToRawLongBits(d);
    }
	
	protected abstract void addDirectBoolean(boolean value);
	
	protected abstract void addDirectByte(byte value);
	
	public abstract void increaseCapacity(int booleans);
	
	public abstract void terminate();
	
	protected void addDirectBooleans(boolean... bools){
		for(boolean bool : bools)
			addBoolean(bool);
	}
	
	protected void addDirectBytes(byte... bytes){
		for(byte b : bytes)
			addByte(b);
	}
	
	protected void addDirectChar(char value){
		addDirectBytes(char0(value), char1(value));
	}
	
	protected void addDirectShort(short value){
		addDirectBytes(short0(value), short1(value));
	}
	
	protected void addDirectInt(int value){
		addDirectBytes(int0(value), int1(value), int2(value), int3(value));
	}
	
	protected void addDirectFloat(float value){
		addDirectInt(fromFloat(value));
	}
	
	protected void addDirectLong(long value){
		addDirectBytes(long0(value), long1(value), long2(value), long3(value), long4(value), long5(value), long6(value), long7(value));
	}
	
	protected void addDirectDouble(double value){
		addDirectLong(fromDouble(value));
	}
	
	public void addBoolean(boolean value){
		increaseCapacity(1);
		addDirectBoolean(value);
	}
	
	public void addBooleans(boolean... bools){
		increaseCapacity(bools.length);
		for(boolean bool : bools)
			addDirectBoolean(bool);
	}
	
	public void addBooleanArray(boolean[] value){
		increaseCapacity(32 + value.length);
		addDirectInt(value.length);
		addDirectBooleans(value);
	}
	
	public void addByte(byte value){
		increaseCapacity(8);
		addDirectByte(value);
	}
	
	public void addBytes(byte... bytes){
		increaseCapacity(bytes.length * 8);
		for(byte b : bytes)
			addDirectByte(b);
	}
	
	public void addBytes(byte[] bytes, int startIndex, int amount){
		increaseCapacity(amount * 8);
		for(int i = 0; i < amount; i++)
			addDirectByte(bytes[startIndex + i]);
	}
	
	public void addByteArray(byte[] value){
		increaseCapacity(32 + value.length * 8);
		addDirectInt(value.length);
		addDirectBytes(value);
	}
	
	public void addShort(short value){
		addBytes(short0(value), short1(value));
	}
	
	public void addShorts(short... shorts){
		increaseCapacity(shorts.length * 16);
		for(short s : shorts)
			addDirectShort(s);
	}
	
	public void addShorts(short[] shorts, int startIndex, int amount){
		increaseCapacity(16 * amount);
		for(int i = 0; i < amount; i++)
			addDirectShort(shorts[startIndex + i]);
	}
	
	public void addShortArray(short[] value){
		increaseCapacity(32 + value.length * 16);
		addDirectInt(value.length);
		for(short s : value)
			addDirectShort(s);
	}
	
	public void addChar(char value){
		addBytes(char0(value), char1(value));
	}
	
	public void addChars(char... chars){
		increaseCapacity(chars.length * 16);
		for(char c : chars)
			addDirectChar(c);
	}
	
	public void addChars(char[] chars, int startIndex, int amount){
		increaseCapacity(amount * 16);
		for(int i = 0; i < amount; i++)
			addDirectChar(chars[startIndex + i]);
	}
	
	public void addCharArray(char[] value){
		increaseCapacity(32 + value.length * 16);
		addDirectInt(value.length);
		for(char c : value)
			addDirectChar(c);
	}
	
	public void addInt(int value){
		addBytes(int0(value), int1(value), int2(value), int3(value));
	}
	
	public void addInts(int... ints){
		increaseCapacity(ints.length * 32);
		for(int i : ints)
			addDirectInt(i);
	}
	
	public void addInts(int[] ints, int startIndex, int amount){
		increaseCapacity(amount * 32);
		for(int i = 0; i < amount; i++)
			addDirectInt(ints[startIndex + i]);
	}
	
	public void addIntArray(int[] value){
		increaseCapacity(32 + value.length * 32);
		addDirectInt(value.length);
		for(int i : value)
			addDirectInt(i);
	}
	
	public void addFloat(float value){
		addInt(fromFloat(value));
	}
	
	public void addFloats(float... floats){
		increaseCapacity(floats.length * 32);
		for(float f : floats)
			addDirectFloat(f);
	}
	
	public void addFloats(float[] floats, int startIndex, int amount){
		increaseCapacity(32 * amount);
		for(int i = 0; i < amount; i++)
			addDirectFloat(floats[startIndex + i]);
	}
	
	public void addFloatArray(float[] value){
		increaseCapacity(32 + value.length * 32);
		addDirectInt(value.length);
		for(float f : value)
			addDirectFloat(f);
	}
	
	public void addLong(long value){
		addBytes(long0(value), long1(value), long2(value), long3(value), long4(value), long5(value), long6(value), long7(value));
	}
	
	public void addLongs(long... longs){
		increaseCapacity(64 * longs.length);
		for(long l : longs)
			addDirectLong(l);
	}
	
	public void addLongs(long[] longs, int startIndex, int amount){
		increaseCapacity(64 * amount);
		for(int i = 0; i < amount; i++)
			addDirectLong(longs[startIndex + 1]);
	}
	
	public void addLongArray(long[] value){
		increaseCapacity(32 + value.length * 64);
		addDirectInt(value.length);
		for(long l : value)
			addDirectLong(l);
	}
	
	public void addDouble(double value){
		addLong(fromDouble(value));
	}
	
	public void addDoubles(double... doubles){
		increaseCapacity(64 * doubles.length);
		for(double d : doubles)
			addDirectDouble(d);
	}
	
	public void addDoubles(double[] doubles, int startIndex, int amount){
		increaseCapacity(64 * amount);
		for(int i = 0; i < amount; i++)
			addDirectDouble(doubles[startIndex + i]);
	}
	
	public void addDoubleArray(double[] value){
		increaseCapacity(32 + value.length * 64);
		addDirectInt(value.length);
		for(double d : value)
			addDirectDouble(d);
	}
	
	public void addNumber(long number, byte bitCount, boolean allowNegative){
		addBooleans(numberToBinary(number, bitCount, allowNegative));
	}
	
	public void addNumber(long number, boolean allowNegative){
		if(!allowNegative && number < 0)
			throw new IllegalArgumentException("Number (" + number + ") can't be negative!");
		byte bitCount = getRequiredBits(number);
		if(allowNegative)
			bitCount++;
		increaseCapacity(6 + bitCount);
		addDirectBooleans(numberToBinary(bitCount, (byte) 6, false));
		addDirectBooleans(numberToBinary(number, bitCount, allowNegative));
	}
	
	public void addJavaString(String value){
		increaseCapacity(32 + value.length() * 16);
		addDirectInt(value.length());
		for(int i = 0; i < value.length(); i++)
			addDirectChar(value.charAt(i));
	}
}
