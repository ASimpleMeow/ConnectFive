package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.ServerClientData;
import view.GameClientView;

/**
 * Controller class for the game client.
 * 
 * @author Oleksandr Kononov
 *
 */
public class ConnectGameClient extends Thread implements ActionListener{
	
	/** The view component for the client controller */
	private GameClientView view;
	
	/** Client socket and object input/output streams  for data transfer */
	private Socket socket;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	
	/** Player index for this client */
	private int playerIndex;
	
	/**
	 * Entry point for the client program.
	 * @param args
	 */
	public static void main(String[] args){
		new ConnectGameClient();
	}
	
	/**
	 * Constructor for the game client.
	 * Initialises variables, connects to server and starts the polling thread.
	 */
	public ConnectGameClient() {
		view = new GameClientView(this);
		view.setTextArea("Please wait will connection to server is established...");
		
		// Try to connect to the server.
		try {
			socket = new Socket("localhost", ConnectGameServer.PORT);
			fromServer = new ObjectInputStream(socket.getInputStream());
			toServer = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			System.err.println("Could not connect to server!");
			return;
		}
		
		// Start the polling thread
		this.start();
	}
	
	/**
	 * Method running on a separate thread for polling messages from the server.
	 */
	@Override
	public void run(){
		// Poll for any ServerClientData sent from the server and process it
		try {
			while(socket.getInetAddress().isReachable(100)){
				processServerData((ServerClientData)fromServer.readObject());
			}
		} catch (IOException | ClassNotFoundException e1) {
			System.err.println("Could not reach server!");
		}
		
		// If an exception has occurred (could not reach server, close the connection and exit
		try{
			fromServer.close();
			toServer.close();
			socket.close();
			view.setVisible(false);
			view.dispose();
		}catch (IOException e){
			System.err.println("Could not close connection from a disconnected server!");
		}
	}
	
	/**
	 * Process data received from server.
	 * @param data ServerClientData object from the server
	 */
	private void processServerData(ServerClientData data){
		if (data == null) return;
		playerIndex = data.fromPlayerIndex;
		if (data.clearText) view.clearTextArea();
		if (!data.board.isEmpty()){
			view.appendTextArea(data.board);
			System.out.println(data.board);
		}
		if (!data.data.isEmpty()) view.appendTextArea(data.data);
		view.enableTextField(data.yourTurn);
	}

	/**
	 * Handles action events for the view (when enter was pressed for the text field).
	 * Sends the contents of the text field in the view to the server.
	 * @param ev Action event that was triggered
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {
		String message = view.getText().trim();
		if (message.isEmpty()) return;
		
		// Send ServerClientData to server from this playerIndex
		try {
			toServer.writeObject(new ServerClientData(playerIndex, "", message, false, false));
			toServer.flush();
		} catch (IOException e) {
			System.err.println("Could not send message to server!");
		}
		
		// Clear view text field and disable it for editing
		view.clearTextField();
		view.enableTextField(false);
	}

}
