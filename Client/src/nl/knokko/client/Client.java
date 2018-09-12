package nl.knokko.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.filechooser.FileSystemView;

import nl.knokko.client.connection.handler.ClientConnectionHandler;
import nl.knokko.client.connection.io.SocketConnectionIO;
import nl.knokko.client.connection.listener.ClientMainConnectionListener;
import nl.knokko.client.connection.speaker.ClientMainConnectionSpeaker;
import nl.knokko.client.connection.state.ClientMainConnectionState;
import nl.knokko.client.gui.menu.MenuLogin;
import nl.knokko.gui.window.GLGuiWindow;
import nl.knokko.gui.window.WindowListener;

public class Client {
	
	private static final PrintWriter BACK_UP_OUTPUT = new PrintWriter(System.out);
	
	private static String mainIP;
	private static int mainPort;
	
	private static File gameFolder;
	private static GLGuiWindow window;
	
	private static ClientConnectionHandler mainHandler;
	private static ClientMainConnectionSpeaker mainSpeaker;
	private static ClientMainConnectionState mainState;
	
	private static PrintWriter mainLogger;
	private static PrintWriter realmLogger;
	private static PrintWriter logger;
	
	private static boolean isStopping;
	
	private static void open(){
		//System.out.println(Shell32Util.getFolderPath(ShlObj.CSIDL_PERSONAL));
		//System.out.println(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
		gameFolder = new File(FileSystemView.getFileSystemView().getDefaultDirectory() + File.separator + "BotBattle");
		gameFolder.mkdirs();
		//Natives.prepare(gameFolder);
		window = new GLGuiWindow();
		window.setWindowListener(new ClientWindowListener());
		window.setMainComponent(MenuLogin.getInstance());
		window.open("Bot Battle", 1200, 700, true);
	}
	
	private static void initialise(String[] args){
		mainIP = args[0];
		mainPort = Integer.parseInt(args[1]);
		try {
			logger = new PrintWriter(gameFolder + File.separator + "log.txt");
			mainLogger = new PrintWriter(gameFolder + File.separator + "main log.txt");
			realmLogger = new PrintWriter(gameFolder + File.separator + "realm log.txt");
		} catch(FileNotFoundException io){
			throw new RuntimeException("Could not open loggers:", io);
		}
		mainState = new ClientMainConnectionState();
		mainSpeaker = new ClientMainConnectionSpeaker(mainState);
		mainHandler = new ClientConnectionHandler(mainLogger, new ClientMainConnectionListener(), mainSpeaker, mainState, new SocketConnectionIO());
	}
	
	private static void finish(){
		mainHandler.stop();
	}

	public static void main(String[] args) {
		open();
		initialise(args);
		window.run(60);
		finish();
	}
	
	public static void openMainConnection(){
		mainLogger.println("Client.openMainConnection() has been called");
		if(!mainState.isConnecting()){
			mainLogger.println("Open main connection with " + mainIP + ":" + mainPort);
			mainHandler.start(mainIP, mainPort);
		}
	}
	
	public static File getGameFolder(){
		return gameFolder;
	}
	
	public static PrintWriter getLogger(){
		return logger != null ? logger : BACK_UP_OUTPUT;
	}
	
	/**
	 * @return The logger for the connection with the main server
	 */
	public static PrintWriter getMainLogger(){
		return mainLogger;
	}
	
	/**
	 * @return The logger for the connection with the realm server
	 */
	public static PrintWriter getRealmLogger(){
		return realmLogger;
	}
	
	public static ClientConnectionHandler getMainConnectionHandler(){
		return mainHandler;
	}
	
	public static ClientMainConnectionSpeaker getMainConnectionSpeaker(){
		return mainSpeaker;
	}
	
	public static ClientMainConnectionState getMainConnectionState(){
		return mainState;
	}
	
	public static GLGuiWindow getWindow(){
		return window;
	}
	
	public static void stop(){
		isStopping = true;
	}
	
	private static class ClientWindowListener implements WindowListener {

		@Override
		public boolean preUpdate() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postUpdate() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean preRender() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postRender() {
			
		}

		@Override
		public boolean preClick(float x, float y, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postClick(float x, float y, int button) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public float preScroll(float amount) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void postScroll(float amount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean preKeyPressed(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postKeyPressed(char character) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean preKeyPressed(int keyCode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postKeyPressed(int keyCode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean preKeyReleased(int keyCode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void postKeyReleased(int keyCode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void preClose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postClose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean preRunLoop() {
			return isStopping;
		}

		@Override
		public void postRunLoop() {
			if(isStopping)
				window.close();
		}
	}
}