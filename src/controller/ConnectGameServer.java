package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.ConnectFiveGame;
import model.ServerClient;
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
				clients.add(s);
			}
			for(ServerClient s : clients) s.send("Game will now begin...\n\n"+game);
			view.appendTextArea("Game Starting");
			runGame();
		} catch(IOException e){
			System.err.println(e);
		}
	}
	
	private void runGame(){
		int currentClient = 0;
		disconnnectAll();
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
	
	public IGame getGame(){
		return game;
	}
}
