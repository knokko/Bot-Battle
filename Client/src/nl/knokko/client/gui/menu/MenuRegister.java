package nl.knokko.client.gui.menu;

import nl.knokko.gui.component.menu.GuiMenu;

public class MenuRegister extends GuiMenu {
	
	private static MenuRegister instance;
	
	public static MenuRegister getInstance(){
		if(instance == null)
			instance = new MenuRegister();
		return instance;
	}

	private MenuRegister() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addComponents() {
		// TODO Auto-generated method stub

	}
}