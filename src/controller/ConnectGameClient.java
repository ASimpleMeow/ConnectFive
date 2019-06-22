package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.ServerClientData;
import view.GameClientView;

public class ConnectGameClient extends Thread implements ActionListener{
	
	private GameClientView view;
	
	private Socket socket;
	
	private int playerIndex;
	
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	
	public static void main(String[] args){
		new ConnectGameClient();
	}
	
	public ConnectGameClient() {
		try {
			socket = new Socket("localhost", 8000);
			fromServer = new ObjectInputStream(socket.getInputStream());
			toServer = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			System.err.println("Could not connect to server!");
			return;
			
		}
		view = new GameClientView(this);
		this.start();
	}
	
	@Override
	public void run(){
		try {
			while(socket.getInetAddress().isReachable(100)){
				parseServerData((ServerClientData)fromServer.readObject());
			}
		} catch (IOException | ClassNotFoundException e1) {
			System.err.println("Could not reach server!");
		}
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
	
	private void parseServerData(ServerClientData data){
		if (data == null) return;
		playerIndex = data.fromPlayerIndex;
		if (data.clearText) view.clearTextArea();
		if (!data.board.isEmpty())view.appendTextArea(data.board);
		if (!data.data.isEmpty()) view.appendTextArea(data.data);
		view.enableTextField(data.yourTurn);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String message = view.getText().trim();
		if (message.isEmpty()) return;
		try {
			toServer.writeObject(new ServerClientData(playerIndex, "", message, false, false));
			toServer.flush();
			view.clearTextField();
			view.enableTextField(false);
		} catch (IOException e) {
			System.err.println("Could not send message to server!");
		}
	}

}
