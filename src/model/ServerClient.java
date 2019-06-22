package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client model class for the game server to keep track of.
 * @author Oleksandr Kononov
 *
 */
public class ServerClient {
	
	private Socket socket;
	
	private int clientIndex;
	
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	
	public ServerClient(Socket socket, int clientIndex) throws IOException{
		this.socket = socket;
		this.clientIndex = clientIndex;
		
		this.toClient = new ObjectOutputStream(socket.getOutputStream());
		this.fromClient = new ObjectInputStream(socket.getInputStream());
	}
	
	public int getClientIndex(){
		return clientIndex;
	}
	
	public String connect(){
		ServerClientData data = new ServerClientData(clientIndex, null, "Please enter your name...", true, true);
		send(data);
		return receive().data;
	}
	
	public void send(String board, String data, boolean clear, boolean yourTurn){
		send(new ServerClientData(clientIndex, board, data, clear, yourTurn));
	}

	public void send(ServerClientData data){
		try {
			toClient.writeObject(data);
			toClient.flush();
		} catch (IOException e) {
			System.err.println("Server could not send message to client!");
		}
	}
	
	public ServerClientData receive(){
		try{
			ServerClientData data = (ServerClientData) fromClient.readObject();
			return data;
		} catch (IOException e) {
			System.err.println("Server could not recieve message from client!");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not cast to ServerClientData class");
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
