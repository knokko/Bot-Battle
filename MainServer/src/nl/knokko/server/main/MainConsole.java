package nl.knokko.server.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.knokko.server.ServerConsole;
import nl.knokko.server.main.account.ServerPassword;

public class MainConsole extends ServerConsole {
	
	MainConsole(){}
	
	@Override
	protected InputStream createInputStream(){
		return System.in;
	}
	
	@Override
	protected OutputStream createOutputStream(){
		return System.out;
	}
	
	@Override
	public void executeCommand(String... command) {
		if(command.length == 0){
			println("empty command?");
			return;
		}
		if(command[0].equals("stop")){
			println("Stopping server...");
			MainServer.stop();
			return;
		}
		if(command[0].equals("hash")){
			if(command.length != 6){
				println("Use hash [code1] [code2] [code3] [code4] [code5]");
				return;
			}
			try {
				long startTime = System.nanoTime();
				ServerPassword sp = new ServerPassword(Long.parseLong(command[1]), Long.parseLong(command[2]), Long.parseLong(command[3]), Long.parseLong(command[4]), Long.parseLong(command[5]));
				long endTime = System.nanoTime();
				println("Result is " + sp + " and took " + (endTime - startTime) / 1000 + " microseconds.");
			} catch(NumberFormatException ex){
				println("All arguments after 'hash' need to be numbers.");
			}
			return;
		}
		if(command[0].equals("realm")){
			if(command.length != 3){
				sendRealmCommandUseage();
				return;
			}
			if(command[1].equals("create")){
				if(command[2].equals("approve")){
					println(MainServer.getRealmManager().approveCreation());
					return;
				}
				if(command[2].equals("deny")){
					println(MainServer.getRealmManager().denyCreation());
					return;
				}
				println("Use 'realm create approve' or 'realm create deny'");
				return;
			}
			if(command[1].equals("move")){
				if(command[2].equals("approve")){
					println(MainServer.getRealmManager().approveMove());
					return;
				}
				if(command[2].equals("deny")){
					println(MainServer.getRealmManager().denyMove());
					return;
				}
				println("Use 'realm move approve' or 'realm move deny'");
				return;
			}
			if(command[1].equals("autodeny")){
				if(command[2].equals("move")){
					MainServer.getRealmManager().autoDenyMove();
					println("The server will automatically deny realm move requests.");
					return;
				}
				if(command[2].equals("create")){
					MainServer.getRealmManager().autoDenyNew();
					println("The server will automatically deny realm creation requests.");
					return;
				}
				println("Use 'realm autodeny move' or 'realm autodeny create'");
				return;
			}
			if(command[1].equals("manualdeny")){
				if(command[2].equals("move")){
					MainServer.getRealmManager().manualDenyMove();
					println("The server will let you deny or approve realm move requests.");
					return;
				}
				if(command[2].equals("create")){
					MainServer.getRealmManager().manualDenyNew();
					println("The server will let you deny or approve realm creation requests.");
					return;
				}
				println("Use 'realm manualdeny move' or 'realm manualdeny create'");
				return;
			}
			sendRealmCommandUseage();
			return;
		}
		println("Unknown command '" + command[0] + "'");
		println("Commands are: [stop,hash,realm]");
	}

	@Override
	protected void stopServer() {
		MainServer.stop();
	}

	@Override
	protected boolean isServerStopping() {
		return MainServer.isStopping();
	}

	@Override
	protected void startServerConnection(int port) throws Exception {
		MainServer.startConnection(port);
	}

	@Override
	protected void onStart() {
		println("Enter the requested port number:");
		while(true){
			if(stopped)
				return;
			try {
				int port = Integer.parseInt(in.readLine());
				try {
					println("Starting connection on port " + port + "...");
					startServerConnection(port);
					println("start connection method returned; breaking loop...");
					break;
				} catch(Exception ex){
					println("Failed to start connection with port " + port + ": " + ex.getMessage());
				}
			} catch(NumberFormatException e){
				println("Please enter a valid integer.");
			} catch (IOException e) {
				if(!isServerStopping())
					println("An IO Error occured while asking for the port number: " + e.getMessage());
				else {
					println("Stopped reading port number because server is stopping and this error occured: " + e.getMessage());
					return;
				}
			}
		}
		println("Port has been selected");
	}
	
	private void sendRealmCommandUseage(){
		println("Use one of the following commands:");
		println("realm create approve/deny");
		println("realm move approve/deny");
		println("realm autodeny move/create");
		println("realm manualdeny move/create");
	}
}
