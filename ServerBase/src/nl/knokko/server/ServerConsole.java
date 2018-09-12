package nl.knokko.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public abstract class ServerConsole implements Runnable {
	
	protected BufferedReader in;
	protected PrintWriter out;
	
	protected boolean stopped;
	
	protected ServerConsole(){}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void stop(){
		stopped = true;
		try {
			in.close();
		} catch (IOException e) {
			println("Couldn't close console input stream: " + e.getMessage());
		}
	}
	
	@Override
	public void run() {
		in = new BufferedReader(new InputStreamReader(createInputStream()));
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(createOutputStream(), "UTF-8")), true);
		} catch (UnsupportedEncodingException e1) {
			stopServer();
			return;
		}
		println("Started server console.");
		onStart();
		if(stopped)
			return;
		println("execute commands...");
		while(!stopped && !isServerStopping()){
			try {
				executeCommand(in.readLine());
			} catch(IOException e){
				if(!isServerStopping()){
					println("An Error occured while reading a command: " + e.getMessage());
					println("Stopping server...");
					stopServer();
				}
				else
					println("Stopped reading commands because server is stopping and this error occured: " + e.getMessage());
			}
		}
		println("Stopped reading commands.");
	}
	
	public PrintWriter getOutput(){
		return out;
	}
	
	protected abstract void onStart();
	
	protected abstract InputStream createInputStream();
	
	protected abstract OutputStream createOutputStream();
	
	protected abstract void stopServer();
	
	protected abstract boolean isServerStopping();
	
	protected abstract void startServerConnection(int port) throws Exception;
	
	public void println(String message){
		out.println(message);
	}
	
	public void executeCommand(String command){
		try {
			executeCommand(command.split(" "));
		} catch(Throwable t){
			t.printStackTrace(out);
			stopServer();
		}
	}
	
	public abstract void executeCommand(String... args);
}
