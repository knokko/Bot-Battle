package nl.knokko.util.bits;

import java.util.Arrays;

public final class BitHelper {
	
	private static final long[] POWERS = new long[63];
	
	private static final boolean[][] BOOLEANS = new boolean[256][8];
	
	private static final short[] BYTES = new short[]{128,64,32,16,8,4,2,1};
	
	static {
		setPowers();
		setBooleans();
	}
	
	private static void setPowers(){
		long l = 1;
		for(byte index = 0; index < POWERS.length; index++){
			POWERS[index] = l;
			l *= 2;
		}
	}
	
	private static void setBooleans(){
		for(short b = 0; b < 256; b++){
			short c = b;
			for(byte t = 0; t < 8; t++){
				if(c >= BYTES[t]){
					BOOLEANS[b][t] = true;
					c -= BYTES[t];
				}
			}
		}
	}
	
	private static void checkBitCount(byte bits){
		if(bits < 0)
			throw new IllegalArgumentException("Number of bits ( + " + bits + ") can't be negative!");
		if(bits >= 64)
			throw new IllegalArgumentException("Number of bits ( + " + bits + ") can't be greater than 63!");
	}
	
	private static void checkOverflow(long number, byte bits){
		if(get2Power(bits) <= number || get2Power(bits) < -number)
			throw new IllegalArgumentException("You need more than " + bits + " bits to store the number " + number + "!");
	}
	
	public static boolean[] byteToBinary(byte number){
		return Arrays.copyOf(BOOLEANS[number & 0xFF], 8);
	}
	
	public static void byteToBinary(byte number, boolean[] dest, int startIndex){
		int index = number & 0xFF;
		dest[startIndex++] = BOOLEANS[index][0];
		dest[startIndex++] = BOOLEANS[index][1];
		dest[startIndex++] = BOOLEANS[index][2];
		dest[startIndex++] = BOOLEANS[index][3];
		dest[startIndex++] = BOOLEANS[index][4];
		dest[startIndex++] = BOOLEANS[index][5];
		dest[startIndex++] = BOOLEANS[index][6];
		dest[startIndex++] = BOOLEANS[index][7];
	}
	
	public static boolean[] numberToBinary(long number, byte bits, boolean allowNegative){
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
	
	public static byte char1(char x) { return (byte)(x >> 8); }
    public static byte char0(char x) { return (byte)(x     ); }
    
    public static byte short1(short x) { return (byte)(x >> 8); }
    public static byte short0(short x) { return (byte)(x     ); }
    
    public static byte int3(int x) { return (byte)(x >> 24); }
    public static byte int2(int x) { return (byte)(x >> 16); }
    public static byte int1(int x) { return (byte)(x >>  8); }
    public static byte int0(int x) { return (byte)(x      ); }
    
    public static byte long7(long x) { return (byte)(x >> 56); }
    public static byte long6(long x) { return (byte)(x >> 48); }
    public static byte long5(long x) { return (byte)(x >> 40); }
    public static byte long4(long x) { return (byte)(x >> 32); }
    public static byte long3(long x) { return (byte)(x >> 24); }
    public static byte long2(long x) { return (byte)(x >> 16); }
    public static byte long1(long x) { return (byte)(x >>  8); }
    public static byte long0(long x) { return (byte)(x      ); }
    
    public static byte byteFromBinary(boolean[] source){
    	short number = 0;
    	if(source[0]) number += 128;
    	if(source[1]) number += 64;
    	if(source[2]) number += 32;
    	if(source[3]) number += 16;
    	if(source[4]) number += 8;
    	if(source[5]) number += 4;
    	if(source[6]) number += 2;
    	if(source[7]) number++;
    	return (byte) number;
    }
    
    public static byte byteFromBinary(boolean[] source, int startIndex){
    	short number = 0;
    	if(source[startIndex++]) number += 128;
    	if(source[startIndex++]) number += 64;
    	if(source[startIndex++]) number += 32;
    	if(source[startIndex++]) number += 16;
    	if(source[startIndex++]) number += 8;
    	if(source[startIndex++]) number += 4;
    	if(source[startIndex++]) number += 2;
    	if(source[startIndex++]) number++;
    	return (byte) number;
    }
	
	public static long numberFromBinary(boolean[] bools, byte bits, boolean allowNegative){
		checkBitCount(bits);
		long number = 0;
		byte neg = (byte) (allowNegative ? 1 : 0);
		for(byte b = 0; b < bits; b++)
			if(bools[b + neg])
				number += get2Power(bits - b - 1);
		if(allowNegative){
			if(!bools[0]){
				number = -number;
				number--;
			}
		}
		return number;
	}
	
	public static long get2Power(int index){
		return POWERS[index];
	}
	
	public static char makeChar(byte b0, byte b1) {
        return (char)((b1 << 8) | (b0 & 0xff));
    }
	
	public static short makeShort(byte b0, byte b1) {
        return (short)((b1 << 8) | (b0 & 0xff));
    }
	
	public static int makeInt(byte b0, byte b1, byte b2, byte b3) {
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }
	
	public static long makeLong(byte b0, byte b1, byte b2, byte b3,byte b4, byte b5, byte b6, byte b7){
		return ((((long)b7       ) << 56) |
				(((long)b6 & 0xff) << 48) |
				(((long)b5 & 0xff) << 40) |
				(((long)b4 & 0xff) << 32) |
				(((long)b3 & 0xff) << 24) |
				(((long)b2 & 0xff) << 16) |
			(((long)b1 & 0xff) <<  8) |
			(((long)b0 & 0xff)      ));
	}
}