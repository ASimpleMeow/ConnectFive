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
	
	/** Index of the player/client from/to which data will be transfered to/from */
	public int fromPlayerIndex;
	
	/** String value representing the game board */
	public String board;
	
	/** Additional data for transfer between client/server */
	public String data;
	
	/** Flag for clearing (refreshing) text area of the client window */
	public boolean clearText;
	
	/** Flag for enabling/disabling ability to edit text field for client window */
	public boolean yourTurn;
	
	/** Default constructor and values for the data */
	public ServerClientData(){
		this.fromPlayerIndex = -1;
		this.board = "";
		this.data = "";
		this.clearText = true;
		this.yourTurn = false;
	}
	
	/** Specific constructor for the data */
	public ServerClientData(int from, String board, String data, boolean clearText, boolean yourTurn){
		this.fromPlayerIndex = from;
		this.board = board == null ? "" : board;
		this.data = data == null ? "" : data;
		this.clearText = clearText;
		this.yourTurn = yourTurn;
	}
}
