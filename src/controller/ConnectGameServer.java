package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import model.ConnectFiveGame;
import model.ServerClient;
import model.ServerClientData;
import model.interfaces.IGame;
import view.GameServerView;

/**
 * Controller class for the game server.
 * 
 * @author Oleksandr Kononov
 *
 */
public class ConnectGameServer extends Thread{
	
	/** Port number constant for connections */
	public static final int PORT = 8000;
	
	/** The view component for the client controller */
	private GameServerView view;
	
	/** Server socket for establishing client connections */
	private ServerSocket serverSocket;
	
	/** List of ServerClient object to keep track of client related data */
	private List<ServerClient> clients;
	
	/** The Connect type game */
	private IGame game;
	
	/**
	 * Point of entry for the server program.
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException{
		new ConnectGameServer();
	}
	
	/**
	 * Constructor for the game server, initialises values and starts the game.
	 * @throws InterruptedException
	 */
	public ConnectGameServer() throws InterruptedException{
		game = new ConnectFiveGame();
		view = new GameServerView();
		clients = new ArrayList<ServerClient>();
		
		try {
			serverSocket = new ServerSocket(PORT);
			view.setTextArea("Socket Created Successfully\n");
			connectClients();
			runGame();
		} catch (IOException e) {
			System.err.println("Could not create server socket!\n" + e.getMessage());
		}
	}
	
	/**
	 * Connects to all clients (player) as specified by the PLAYERS_AMOUNT constant.
	 */
	private void connectClients(){
		try{
			while(clients.size() < ConnectFiveGame.PLAYERS_AMOUNT){
				ServerClient sc = new ServerClient(serverSocket.accept(), clients.size());
				sc.connect();
				game.setPlayerName(sc.getClientIndex(), sc.getClientName());
				view.appendTextArea("Player "+sc.getClientName()+" Connected");
				clients.add(sc);
				if (clients.size() < ConnectFiveGame.PLAYERS_AMOUNT)
					sc.send(null, "Waiting for other player(s)...", true, false);
			}
		} catch(IOException e){
			System.err.println("Exception creating and connecting to client\n"+e.getMessage());
		}
	}
	
	/**
	 * Method for the game loop of the server.
	 * @throws InterruptedException
	 */
	private void runGame() throws InterruptedException{
		// Announce the beginning of the game
		announceAll(null, "Game will now begin...", true);
		for (ServerClient sc : clients) 
			sc.send(null, "Your symbol is: "+game.getPlayerSymbol(sc.getClientIndex()), false, false);
		view.appendTextArea("Game Starting...");
		Thread.sleep(1500);
		
		ServerClient currentClient = clients.get(game.getCurrentPlayerIndex());
		String playerQuery = ": It's your turn, please enter a column (1-9)";
		String othersMessage = "Waiting for player: ";
		
		// Main loop of the game
		while(!game.isGameOver() && allClientsConnected()){
			// Announce the current board to all players
			announceAllBut(currentClient, game.toString(),othersMessage+currentClient.getClientName(), true);
			
			// Send query to the current player
			currentClient.send(game.toString(), currentClient.getClientName()+playerQuery, true, true);
			ServerClientData response = currentClient.receive();
			if (response == null) continue;
			
			// Attempt to perform the move on the game board
			int column = -1;
			try { column = Integer.parseInt(response.data) - 1; }
			catch (NumberFormatException e) { continue; }
			int row = game.doMove(column);
			if (row == -1) continue;
			
			// If game is won by the previous move, the current player won
			if (game.isGameWon(column, row)){
				announceAll(game.toString(), game.getCurrentPlayer()+" Has Won!", true);
				Thread.sleep(2000);
				disconnnectAll();
				return;
			}
			
			// Switch to next player
			currentClient = clients.get(game.endPlayerTurn());
		}
		
		// No further moves can be made or a player disconnected
		view.setTextArea("Game Is Over With No Winners");
		announceAll(null, "Game Is Over With No Winners", false);
		Thread.sleep(2000);
		disconnnectAll();
	}
	
	/**
	 * Send data such as the state of the game board, some message and a clear text
	 * flag to all connected clients (players).
	 * @param board String representing the current state of the game board
	 * @param message String to send to the player (usually a query)
	 * @param clear Boolean flag on whether to clear the text area (refresh) for the clients
	 */
	private void announceAll(String board, String message, boolean clear){
		for (ServerClient sc : clients) sc.send(board, message, clear, false);
	}
	
	/**
	 * Send data such as the state of the game board, some message and a clear text
	 * flag to all connected clients (players) but s.
	 * @param s ServerClient to exclude from the announcement 
	 * @param board String representing the current state of the game board
	 * @param message String to send to the player (usually a query)
	 * @param clear Boolean flag on whether to clear the text area (refresh) for the clients
	 */
	private void announceAllBut(ServerClient s, String board, String message, boolean clear){
		for (ServerClient sc: clients) {
			if (sc.equals(s)) continue;
			sc.send(board, message, clear, false);
		}
	}
	
	/**
	 * Checks all clients in the list if they are still connected.
	 * @return true if all clients are connected, false otherwise
	 */
	private boolean allClientsConnected(){
		for(ServerClient sc : clients) if (!sc.isConnected()) return false;
		return true;
	}
	
	/**
	 * Disconnects all clients from server.
	 * If all clients are disconnected the server doesn't need to exist
	 * as there is no more to be played, so the view is disposed as well.
	 */
	private void disconnnectAll(){
		for (ServerClient sc : clients) sc.disconnect();
		clients.clear();
		
		try {
			serverSocket.close();
			view.setVisible(false);
			view.dispose();
		} catch (IOException e) {
			System.err.println("Could not close server socket!");
		}
	}
}
