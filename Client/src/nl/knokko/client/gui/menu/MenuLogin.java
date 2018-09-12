package nl.knokko.client.gui.menu;

import java.awt.Color;
import java.awt.Font;

import nl.knokko.client.Client;
import nl.knokko.client.gui.component.PasswordField;
import nl.knokko.gui.color.GuiColor;
import nl.knokko.gui.color.SimpleGuiColor;
import nl.knokko.gui.component.menu.GuiMenu;
import nl.knokko.gui.component.text.TextButton;
import nl.knokko.gui.component.text.TextComponent;
import nl.knokko.gui.component.text.TextEditField;
import nl.knokko.gui.util.TextBuilder.HorAlignment;
import nl.knokko.gui.util.TextBuilder.Properties;
import nl.knokko.gui.util.TextBuilder.VerAlignment;

public class MenuLogin extends GuiMenu {
	
	public static final Color TRANSPARENT = new Color(0,0,0,0);
	
	public static final GuiColor BACKGROUND = new SimpleGuiColor(200, 0, 250);
	
	public static final Properties TEXT_PROPERTIES = new Properties(new Font("TimesRoman", 1, 20), Color.BLACK, TRANSPARENT, TRANSPARENT, HorAlignment.LEFT, VerAlignment.DOWN, 0, 0, 0, 0);
	public static final Properties ERROR_PROPERTIES = new Properties(new Font("TimesRoman", 1, 20), Color.RED, TRANSPARENT, TRANSPARENT, HorAlignment.LEFT, VerAlignment.DOWN, 0, 0, 0, 0);
	
	public static final Properties CREATE_PROPERTIES = new Properties(new Font("TimesRoman", 1, 20), Color.BLUE, TRANSPARENT, TRANSPARENT, HorAlignment.LEFT, VerAlignment.MIDDLE, 0, 0, 0, 0);
	public static final Properties HOVER_CREATE_PROPERTIES = new Properties(new Font("TimesRoman", 1, 20), Color.YELLOW, TRANSPARENT, TRANSPARENT, HorAlignment.LEFT, VerAlignment.MIDDLE, 0, 0, 0, 0);
	
	public static final Properties EDIT_PROPERTIES = new Properties(new Font("", 0, 20), Color.BLACK, new Color(0, 0, 200), Color.BLACK, HorAlignment.LEFT, VerAlignment.MIDDLE, 0.025f, 0.05f, 0.05f, 0.05f);
	public static final Properties ACTIVE_EDIT_PROPERTIES = new Properties(new Font("", 0, 20), Color.BLACK, new Color(0, 0, 255), Color.YELLOW, HorAlignment.LEFT, VerAlignment.MIDDLE, 0.025f, 0.05f, 0.05f, 0.05f);
	
	public static final Properties QUIT_PROPERTIES = new Properties(new Font("", 0, 20), Color.BLACK, new Color(200, 100, 0), new Color(100, 50, 0), HorAlignment.MIDDLE, VerAlignment.MIDDLE, 0.05f, 0.1f, 0.05f, 0.1f);
	public static final Properties HOVER_QUIT_PROPERTIES = new Properties(new Font("", 0, 20), Color.BLACK, new Color(255, 150, 0), new Color(150, 80, 0), HorAlignment.MIDDLE, VerAlignment.MIDDLE, 0.05f, 0.1f, 0.05f, 0.1f);
	
	public static final Properties LOGIN_PROPERTIES = new Properties(new Font("", 0, 20), Color.BLACK, new Color(0, 200, 50), new Color(0, 100, 0), HorAlignment.MIDDLE, VerAlignment.MIDDLE, 0.05f, 0.1f, 0.05f, 0.1f);
	public static final Properties HOVER_LOGIN_PROPERTIES = new Properties(new Font("", 0, 20), Color.BLACK, new Color(0, 255, 100), new Color(0, 150, 50), HorAlignment.MIDDLE, VerAlignment.MIDDLE, 0.05f, 0.1f, 0.05f, 0.1f);
	
	private static MenuLogin instance;
	
	public static MenuLogin getInstance(){
		if(instance == null)
			instance = new MenuLogin();
		return instance;
	}
	
	private final TextEditField username;
	private final PasswordField password;
	
	private final TextComponent loginError;

	private MenuLogin() {
		username = new TextEditField("", EDIT_PROPERTIES, ACTIVE_EDIT_PROPERTIES);
		password = new PasswordField("", EDIT_PROPERTIES, ACTIVE_EDIT_PROPERTIES);
		loginError = new TextComponent("", ERROR_PROPERTIES);
	}
	
	public void setError(String error){
		loginError.setText(error);
	}

	@Override
	protected void addComponents() {
		addComponent(new TextComponent("Username", TEXT_PROPERTIES), 0.75f, 0.925f, 0.90f, 0.975f);
		addComponent(username, 0.75f, 0.825f, 0.95f, 0.9f);
		addComponent(new TextComponent("Password", TEXT_PROPERTIES), 0.75f, 0.725f, 0.90f, 0.775f);
		addComponent(password, 0.75f, 0.625f, 0.95f, 0.7f);
		addComponent(new TextButton("Log in", LOGIN_PROPERTIES, HOVER_LOGIN_PROPERTIES, new Runnable(){

			@Override
			public void run() {
				Client.openMainConnection();
				Client.getMainConnectionSpeaker().auth().startLoginProcess(username.getText(), password.getText());
				password.setText("");//don't keep the password longer in memory than necessary
			}
		}), 0.75f, 0.45f, 0.95f, 0.55f);
		addComponent(new TextButton("Create account", CREATE_PROPERTIES, HOVER_CREATE_PROPERTIES, new Runnable(){

			@Override
			public void run() {
				Client.getWindow().setMainComponent(MenuRegister.getInstance());
			}
		}), 0.75f, 0.325f, 0.95f, 0.4f);
		addComponent(loginError, 0.75f, 0.25f, 0.95f, 0.3f);
		addComponent(new TextButton("Quit game", QUIT_PROPERTIES, HOVER_QUIT_PROPERTIES, new Runnable(){

			@Override
			public void run() {
				Client.stop();
			}
		}), 0.75f, 0.05f, 0.95f, 0.15f);
	}
	
	@Override
	public GuiColor getBackgroundColor(){
		return BACKGROUND;
	}
}