package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Client model class for the game server to keep track of.
 * @author Oleksandr Kononov
 *
 */
public class ServerClient {
	
	private Socket socket;
	
	private int clientIndex;
	
	private DataOutputStream toClient;
	private DataInputStream fromClient;
	
	public ServerClient(Socket socket, int clientIndex) throws IOException{
		this.socket = socket;
		this.clientIndex = clientIndex;
		
		this.fromClient = new DataInputStream(socket.getInputStream());
		this.toClient = new DataOutputStream(socket.getOutputStream());
	}
	
	public int getClientIndex(){
		return clientIndex;
	}
	
	public String connect(){
		send("Please enter your name...");
		String name = receive();
		send("You are now registered into the game");
		return name;
	}

	public void send(String message){
		try {
			toClient.writeUTF(message+"\n");
			toClient.flush();
		} catch (IOException e) {
			System.err.println("Server could not send message to client!");
		}
	}
	
	public String receive(){
		try{
			return fromClient.readUTF();
		} catch (IOException e) {
			System.err.println("Server could not recieve message from client!");
		}
		return null;
	}
	
	public void disconnect(){
		try {
			toClient.close();
			fromClient.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Count not close socket!");
		}
	}
}
