package model;

import java.io.Serializable;

/**
 * A class that acts as a structure of data that will be transfered
 * between clients and server.
 * @author Oleksandr Kononov
 *
 */
public class ServerClientData implements Serializable{
	private static final long serialVersionUID = -7257704834602472132L;
	public int fromPlayerIndex;
	public String board;
	public String data;
	public boolean clearText;
	public boolean yourTurn;
	
	public ServerClientData(){
		this.fromPlayerIndex = -1;
		this.board = "";
		this.data = "";
		this.clearText = true;
		this.yourTurn = false;
	}
	
	public ServerClientData(int from, String board, String data, boolean clearText, boolean yourTurn){
		this.fromPlayerIndex = from;
		this.board = board == null ? "" : board;
		this.data = data == null ? "" : data;
		this.clearText = clearText;
		this.yourTurn = yourTurn;
	}
}
