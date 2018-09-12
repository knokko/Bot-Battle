package nl.knokko.server.realm;

import java.io.File;

import nl.knokko.server.connection.manager.ServerConnectionManager;
import nl.knokko.server.connection.manager.SocketConnectionManager;
import nl.knokko.server.connection.protocol.factory.RealmProtocolFactory;
import nl.knokko.util.files.FileManager;
import nl.knokko.util.files.FolderFileManager;

public class RealmServer {
	
	private static RealmConsole console;
	private static ServerConnectionManager connectionManager;
	private static FileManager fileManager;
	
	private static boolean stopping;

	public static void main(String[] args) {
		fileManager = new FolderFileManager(new File("realm server data"));
		console = new RealmConsole();
		console.start();
		connectionManager = new SocketConnectionManager(console.getOutput());
	}
	
	public static RealmConsole getConsole(){
		return console;
	}
	
	public static ServerConnectionManager getConnectionManager(){
		return connectionManager;
	}
	
	public static FileManager getFileManager(){
		return fileManager;
	}
	
	public static void startConnection(int port) throws Exception {
		connectionManager.start(port, new RealmProtocolFactory());
	}
	
	public static void stop(){
		if(!stopping){
			stopping = true;
			if(connectionManager != null)
				connectionManager.stop();
			console.stop();
		}
		else
			console.println("Realm server is being closed twice.");
	}
	
	public static boolean isStopping(){
		return stopping;
	}
}
