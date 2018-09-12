package nl.knokko.server.realm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.knokko.client.connection.io.ClientConnectionIO;
import nl.knokko.connection.ConnectionCode;
import nl.knokko.connection.realm.RealmLocation;
import nl.knokko.connection.realm.RealmName;
import nl.knokko.server.ServerConsole;
import nl.knokko.server.connection.code.RealmConnectionCode;
import nl.knokko.server.connection.code.RealmPassword;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;
import nl.knokko.util.bits.BooleanArrayBitOutput;

public class RealmConsole extends ServerConsole {
	
	private RealmName name;
	private RealmPassword password;
	
	private boolean registered;

	RealmConsole() {}

	@Override
	protected InputStream createInputStream() {
		return System.in;
	}

	@Override
	protected OutputStream createOutputStream() {
		return System.out;
	}

	@Override
	protected void stopServer() {
		RealmServer.stop();
	}

	@Override
	protected boolean isServerStopping() {
		return RealmServer.isStopping();
	}

	@Override
	protected void startServerConnection(int port) throws Exception {
		RealmServer.startConnection(port);
	}

	@Override
	public void executeCommand(String... command) {
		if(command[0].equals("stop")){
			println("Stop command has been used.");
			stopServer();
			return;
		}
		if(command[0].equals("connect")){
			new Thread(new Connector(RealmServer.getConnectionManager().createClientConnection(new RealmLocation(ConnectionCode.MAIN_HOST, ConnectionCode.MAIN_PORT)))).start();
			println("Started thread for connection...");
			return;
		}
		if(command[0].equals("name")){
			println("The realm name is " + name);
			return;
		}
		if(command[0].equals("password")){
			println("The realm password is " + password);
			return;
		}
		println("The available commands are (stop)");
	}

	@Override
	protected void onStart() {
		try {
			println("Loading general realm data...");
			BitInput general = RealmServer.getFileManager().openInput("general.data");
			name = new RealmName(general);
			password = new RealmPassword(general);
			registered = general.readBoolean();
			general.terminate();
			println("Loaded general realm data");
		} catch(IOException ioex){
			try {
				println("Failed to load the general realm data; assuming this is the first time this realm server is running.");
				while(true){
					println("Enter '%stop' (without the quotes) to stop the server or enter anything else to choose the name of the realm.");
					String name = in.readLine();
					if(name.equals("%stop")){
						println("Stopping realm server...");
						stopServer();
						return;
					}
					try {
						this.name = new RealmName(name);
						break;
					} catch(IllegalArgumentException iae){
						println("Invalid realm name!");
					}
				}
				password = new RealmPassword();
				println("The password is " + password);
				println("Saving realm data...");
				BitOutput general = RealmServer.getFileManager().openOutput("general.data");
				this.name.save(general);
				password.save(general);
				general.addBoolean(false);
				general.terminate();
				println("The general realm data has been saved.");
			} catch(IOException ioex2){
				println("Failed to start the realm:");
				ioex2.printStackTrace(getOutput());
				println("Stopping realm server...");
				stopServer();
				return;
			}
		}
	}
	
	@Override
	public void stop(){
		if(name != null)
			save();
		super.stop();
	}
	
	private void save(){
		try {
			BitOutput output = RealmServer.getFileManager().openOutput("general.data");
			name.save(output);
			password.save(output);
			output.addBoolean(registered);
			output.terminate();
		} catch(IOException ioex){
			println("Failed to save the general data: " + ioex.getLocalizedMessage());
		}
	}
	
	private class Connector implements Runnable {
		
		private final ClientConnectionIO io;
		
		private Connector(ClientConnectionIO io){
			this.io = io;
		}

		@Override
		public void run() {
			try {
				io.connect(ConnectionCode.MAIN_HOST, ConnectionCode.MAIN_PORT);
				BooleanArrayBitOutput output = new BooleanArrayBitOutput();
				output.addByte(registered ? RealmConnectionCode.MESSAGE_ENABLE_REALM : RealmConnectionCode.MESSAGE_CREATE_REALM);
				name.save(output);
				password.save(output);
				io.sendToServer(output.getBytes());
			} catch(IOException ioex){
				println("Coulnd't connect to the main server: " + ioex.getLocalizedMessage());
			}
		}
	}
}
