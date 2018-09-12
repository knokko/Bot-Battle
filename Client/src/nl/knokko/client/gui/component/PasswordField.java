package nl.knokko.client.gui.component;

import java.util.Arrays;

import nl.knokko.gui.component.text.TextEditField;
import nl.knokko.gui.util.TextBuilder;
import nl.knokko.gui.util.TextBuilder.Properties;

public class PasswordField extends TextEditField {
	
	public static String hide(String password){
		char[] chars = new char[password.length()];
		Arrays.fill(chars, '*');
		return new String(chars);
	}

	public PasswordField(String text, Properties passiveProperties, Properties activeProperties) {
		super(text, passiveProperties, activeProperties);
	}
	
	@Override
	protected void updatePassiveTexture(){
		texture = state.getWindow().getTextureLoader().loadTexture(TextBuilder.createTexture(hide(text), properties, IMAGE_WIDTH, IMAGE_HEIGHT));
	}
	
	@Override
	protected void updateActiveTexture(){
		activeTexture = state.getWindow().getTextureLoader().loadTexture(TextBuilder.createTexture(hide(text), activeProperties, IMAGE_WIDTH, IMAGE_HEIGHT));
	}
}