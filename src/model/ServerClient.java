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
	
	/** Socket for establishing connection with the client */
	private Socket socket;
	
	/** Object Input/Output streams for transfer of data */
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	
	/** Boolean on whether the client is connected */
	private boolean isConnected;
	
	/** Client/Player index for the connected player */
	private int clientIndex;
	
	/** Client/Player name for the connected player */
	private String clientName;
	
	/**
	 * Constructor for the ServerClient class, initialise all variables and connect to client.
	 * @param socket
	 * @param clientIndex
	 * @throws IOException
	 */
	public ServerClient(Socket socket, int clientIndex) throws IOException{
		this.socket = socket;
		this.clientIndex = clientIndex;
		
		this.toClient = new ObjectOutputStream(socket.getOutputStream());
		this.fromClient = new ObjectInputStream(socket.getInputStream());
		
		this.isConnected = true;
		
		// Send initial query for player name to initialise clientName variable
		send(new ServerClientData(clientIndex, null, "Please enter your name...", true, true));
		this.clientName = receive().data;
	}
	
	/**
	 * @return clientIndex Integer
	 */
	public int getClientIndex(){
		return clientIndex;
	}
	
	/**
	 * @return clientName String
	 */
	public String getClientName(){
		return clientName;
	}
	
	/**
	 * Send data to the client.
	 * @param board String of the current game board
	 * @param data Additional data/message for the client
	 * @param clear Boolean flag to clear (refresh) client's text area
	 * @param yourTurn Boolean flag enabling clients text field editing
	 */
	public void send(String board, String data, boolean clear, boolean yourTurn){
		send(new ServerClientData(clientIndex, board, data, clear, yourTurn));
	}

	/**
	 * Send data to the client.
	 * @param data ServerClientData object to be sent
	 */
	public void send(ServerClientData data){
		try {
			toClient.writeObject(data);
			toClient.flush();
		} catch (IOException e) {
			isConnected = false;
			System.err.println("Server could not send message to client!");
		}
	}
	
	/**
	 * Receive data from client.
	 * @return ServerClientData object to be received, if unsuccessful the null
	 */
	public ServerClientData receive(){
		try{
			ServerClientData data = (ServerClientData) fromClient.readObject();
			return data;
		} catch (IOException e) {
			isConnected = false;
			System.err.println("Server could not recieve message from client!");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not cast to ServerClientData class");
		}
		return null;
	}
	
	/**
	 * Checks whether the client is still connected
	 * @return Boolean
	 * @throws IOException
	 */
	public boolean isConnected(){
		return isConnected;
	}
	
	/**
	 * Disconnects the client from the server.
	 */
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
