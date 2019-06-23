package model.interfaces;

/***
 * Interface for a connect N type game model
 * 
 * @author Oleksandr Kononov
 *
 */
public interface IGame {
	
	/**
	 * Initialises the playing board for the start of the game.
	 */
	public void initBoard();
	
	/**
	 * Checks the last valid move played of the current player
	 * given by column and row arguments to see if the current player has won.
	 * @param column
	 * @param row
	 * @return boolean on whether the game is won current player
	 */
	public boolean isGameWon(int column, int row);
	
	/**
	 * Checks the moved played given by column and row arguments to see if
	 * player of playerIndex has won.
	 * @param column
	 * @param row
	 * @param playerIndex
	 * @return boolean on whether the game is won by player of playerIndex
	 */
	public boolean isGameWon(int column, int row, int playerIndex);
	
	/**
	 * Checks if the game is over by having no more valid moves.
	 * @return boolean is there are no more moves to be made
	 */
	public boolean isGameOver();
	
	/**
	 * Attempts to make a move on the board on behalf of the current player.
	 * @param column of the board to use
	 * @return row that was used
	 */
	public int doMove(int column);
	
	/**
	 * Set the current player to the next player.
	 * @return next players index
	 */
	public int endPlayerTurn();
	
	/**
	 * Sets player name.
	 * @param playerIndex
	 * @param name
	 */
	public void setPlayerName(int playerIndex, String name);
	
	/**
	 * @return String name of the current player
	 */
	public String getCurrentPlayer();
	
	/**
	 * @return Integer index of the current player
	 */
	public int getCurrentPlayerIndex();
	
	/**
	 * @return Character symbol for the current player
	 */
	public char getCurrentPlayerSymbol();
	
	/**
	 * @param playerIndex
	 * @return String name of the player at playerIndex
	 */
	public String getPlayer(int playerIndex);
	
	/**
	 * @param playerIndex
	 * @return Character symbol for the given player
	 */
	public char getPlayerSymbol(int playerIndex);
	
	/**
	 * @return Primitive String array of all player names
	 */
	public String[] getPlayers();
	
	/**
	 * @return 2 dimensional primitive array of characters the game board
	 */
	public char[][] getBoard();
	
}
