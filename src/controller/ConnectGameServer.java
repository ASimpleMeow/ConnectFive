package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.ConnectFiveGame;
import model.ServerClient;
import model.ServerClientData;
import model.interfaces.IGame;
import view.GameServerView;

public class ConnectGameServer{
	
	private GameServerView view;
	
	private ServerSocket serverSocket;
	
	private List<ServerClient> clients;
	
	private IGame game;
	
	/**
	 * Point of entry for the server program.
	 * @param args
	 */
	public static void main(String[] args){
		new ConnectGameServer().run();
	}
	
	/**
	 * Main function to handle the server program.
	 */
	private void run(){
		game = new ConnectFiveGame();
		view = new GameServerView();
		clients = new ArrayList<ServerClient>();
		try{
			serverSocket = new ServerSocket(8000);
			view.setTextArea("Socket Created Successfully\n");
			while(clients.size() < 2){
				Socket socket = serverSocket.accept();
				ServerClient s = new ServerClient(socket, clients.size());
				game.setPlayerName(s.getClientIndex(), s.connect());
				view.appendTextArea("Player Connected");
				if (clients.size() < 1) s.send(null, "Waiting for other player...", true, false);
				clients.add(s);
			}
			announceAll(null, "Game will now begin...", true);
			view.appendTextArea("Game Starting...");
			Thread.sleep(500);
			runGame();
		} catch(IOException e){
			System.err.println("Error with socket");
		} catch (InterruptedException e) {
			System.err.println("Thread sleep encountered an error");
		}
	}
	
	private void runGame() throws InterruptedException{
		int currentPlayer = game.getCurrentPlayerIndex();
		ServerClient currentClient = clients.get(currentPlayer);
		while(!game.isGameOver()){
			announceAll(game.toString(),null, true);
			currentClient.send(null, game.getCurrentPlayer()+", please select column (1-9)", false, true);
			ServerClientData response = currentClient.receive();
			if (response == null || response.fromPlayerIndex != currentPlayer) continue;
			int playerSelection = Integer.parseInt(response.data);
			int row = game.doMove(playerSelection);
			if (row == -1) continue;
			if (game.isGameWon(playerSelection, row)){
				announceAll(game.toString(), game.getCurrentPlayer()+" Has Won!", true);
				Thread.sleep(1000);
				disconnnectAll();
				return;
			}
			currentPlayer = game.endPlayerTurn();
			currentClient = clients.get(currentPlayer);
		}
		view.setTextArea("Game Is Over With No Winners");
		Thread.sleep(1000);
		disconnnectAll();
	}
	
	private void announceAll(String board, String message, boolean clear){
		for (ServerClient s : clients) s.send(board, message, clear, false);
	}
	
	private void disconnnectAll(){
		for (ServerClient s : clients) s.disconnect();
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
