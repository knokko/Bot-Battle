package nl.knokko.connection.realm;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class RealmName {
	
	private static final byte SIZE_BITCOUNT = 4;
	private static final byte CHAR_BITCOUNT = 6;
	
	private static final byte MIN_LENGTH = 4;
	private static final byte MAX_LENGTH = 19;
	
	private static final char[] WHITE_LIST = {' '};
	
	private static void verify(String name){
		name = name.trim();
		if(name.length() < MIN_LENGTH)
			throw new IllegalArgumentException("Realm name must be at least " + MIN_LENGTH + " characters long! (this length is " + name.length() + ")");
		if(name.length() > MAX_LENGTH)
			throw new IllegalArgumentException("Realm name can't be longer than " + MAX_LENGTH + " characters! (this length is " + name.length() + ")");
		for(int i = 0; i < name.length(); i++)
			verify(name.charAt(i));
	}
	
	private static void verify(char character){
		if(character >= 'a' && character <= 'z')
			return;
		if(character >= 'A' && character <= 'Z')
			return;
		for(char good : WHITE_LIST)
			if(good == character)
				return;
		throw new IllegalArgumentException("Forbidden character: " + character);
	}
	
	private final String name;

	public RealmName(String name) {
		verify(name);
		this.name = name;
	}
	
	public RealmName(BitInput input){
		char[] chars = new char[(int) input.readNumber(SIZE_BITCOUNT, false) + MIN_LENGTH];
		for(int i = 0; i < chars.length; i++){
			byte next = (byte) input.readNumber(CHAR_BITCOUNT, false);
			if(next < 26)
				chars[i] = (char) ('a' + next);
			else if(next < 52)
				chars[i] = (char) ('A' + next - 26);
			else
				chars[i] = ' ';
		}
		this.name = new String(chars);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof RealmName)
			return name.equals(((RealmName) other).name);
		return false;
	}
	
	public void save(BitOutput output){
		output.addNumber(name.length() - MIN_LENGTH, SIZE_BITCOUNT, false);
		for(int i = 0; i < name.length(); i++){
			char c = name.charAt(i);
			int number;
			if(c >= 'a' && c <= 'z')
				number = c - 'a';
			else if(c >= 'A' && c <= 'Z')
				number = 26 + c - 'A';
			else
				number = 52;
			output.addNumber(number, CHAR_BITCOUNT, false);
		}
	}
}
