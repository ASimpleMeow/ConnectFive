package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import view.GameClientView;

public class ConnectGameClient extends Thread implements ActionListener{
	
	private GameClientView view;
	
	private Socket socket;
	
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	
	public static void main(String[] args){
		new ConnectGameClient();
	}
	
	public ConnectGameClient() {
		try {
			socket = new Socket("localhost", 8000);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
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
				String message = fromServer.readUTF();
				view.appendTextArea(message);
			}
		} catch (IOException e1) {
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

	@Override
	public void actionPerformed(ActionEvent ev) {
		String message = view.getText().trim();
		if (message.isEmpty()) return;
		try {
			toServer.writeUTF(message);
			toServer.flush();
		} catch (IOException e) {
			System.err.println("Could not send message to server!");
		}
	}

}
