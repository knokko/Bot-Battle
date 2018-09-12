package nl.knokko.server.main;

import java.io.File;

import nl.knokko.server.connection.listener.factory.MainListenerFactory;
import nl.knokko.server.connection.manager.ServerConnectionManager;
import nl.knokko.server.connection.manager.SocketConnectionManager;
import nl.knokko.server.main.account.AccountManager;
import nl.knokko.server.main.realm.RealmManager;
import nl.knokko.util.files.FileManager;
import nl.knokko.util.files.FolderFileManager;

public class MainServer {
	
	private static ServerConnectionManager connectionManager;
	private static MainConsole console;
	private static FileManager fileManager;
	private static AccountManager accountManager;
	private static RealmManager realmManager;
	
	private static boolean isStopping;

	public static void main(String[] args) {
		console = new MainConsole();
		console.start();
		fileManager = new FolderFileManager(new File("main server data"));
		accountManager = new AccountManager();
		realmManager = new RealmManager();
	}
	
	public static void stop(){
		if(!isStopping){
			isStopping = true;
			connectionManager.stop();
			accountManager.onEnd();
			console.stop();
		}
		else
			console.println("Server is being closed twice.");
	}
	
	public static void startConnection(int port) throws Exception {
		accountManager.onStart();
		connectionManager = new SocketConnectionManager(console.getOutput());
		connectionManager.start(port, new MainListenerFactory());
	}
	
	public static ServerConnectionManager getConnectionManager(){
		if(connectionManager == null)
			throw new IllegalStateException("The server connection has not yet been started!");
		return connectionManager;
	}
	
	public static FileManager getFileManager(){
		return fileManager;
	}
	
	public static AccountManager getAccountManager(){
		return accountManager;
	}
	
	public static RealmManager getRealmManager(){
		return realmManager;
	}
	
	public static MainConsole getConsole(){
		return console;
	}
	
	public static boolean isStopping(){
		return isStopping;
	}
}
