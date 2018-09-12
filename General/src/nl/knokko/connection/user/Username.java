package nl.knokko.connection.user;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class Username {
	
	private static final char[] WHITE_LIST = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '_'};
	
	public static final int MIN_LENGTH = 4;
	public static final int MAX_LENGTH = 19;
	
	public static void verify(String name) throws IllegalArgumentException {
		if(name.length() < MIN_LENGTH)
			throw new IllegalArgumentException("The length (" + name.length() + ") requires at least " + MIN_LENGTH + " characters.");
		if(name.length() > MAX_LENGTH)
			throw new IllegalArgumentException("The length (" + name.length() + ") can not exceed the " + MAX_LENGTH + " characters.");
		for(int i = 0; i < name.length(); i++)
			verify(name.charAt(i));
	}
	
	public static void verify(char character){
		if(Character.isLetter(character))
			return;
		for(char good : WHITE_LIST)
			if(good == character)
				return;
		throw new IllegalArgumentException("Forbidden character: '" + character + "'");
	}
	
	private String name;

	public Username(String name) {
		verify(name);
		this.name = name;
	}
	
	public Username(BitInput input){
		this.name = input.readJavaString();
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public void save(BitOutput output){
		output.addJavaString(name);
	}
}
