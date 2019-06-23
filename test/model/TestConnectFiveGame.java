package model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.ConnectFiveGame;
import model.interfaces.IGame;

/**
 * JUnit Test class for testing ConnectFiveGame model class.
 * @author Oleksandr Kononov
 *
 */
public class TestConnectFiveGame {

	/** Game variable used for testing */
	private IGame baseGame;
	
	/**
	 * Method to run before every test, initialises the game instance.
	 */
	@Before
	public void setup(){
		baseGame = new ConnectFiveGame();
	}
	
	/**
	 * Testing ConnectFiveGame constructor.
	 * @throws Exception
	 */
	@Test
	public void testConstructor() throws Exception {
		assertEquals(2, baseGame.getPlayers().length);
		assertEquals("Player1", baseGame.getPlayer(0));
		assertEquals("Player2", baseGame.getPlayer(1));
		assertEquals(9, baseGame.getBoard().length);
		assertEquals(6, baseGame.getBoard()[0].length);
		
		baseGame = new ConnectFiveGame(10,15, null);
		assertEquals(2, baseGame.getPlayers().length);
		assertEquals("Player1", baseGame.getPlayer(0));
		assertEquals("Player2", baseGame.getPlayer(1));
		assertEquals(10, baseGame.getBoard().length);
		assertEquals(15, baseGame.getBoard()[0].length);
		
		baseGame = new ConnectFiveGame(5,5, new String[]{"Tester"});
		assertEquals(2, baseGame.getPlayers().length);
		assertEquals("Player1", baseGame.getPlayer(0));
		assertEquals("Player2", baseGame.getPlayer(1));
		assertEquals(5, baseGame.getBoard().length);
		assertEquals(5, baseGame.getBoard()[0].length);
		
		baseGame = new ConnectFiveGame(5,5, new String[]{"Tester1", "Tester2", "Tester3"});
		assertEquals(3, baseGame.getPlayers().length);
		assertEquals("Tester1", baseGame.getPlayer(0));
		assertEquals("Tester2", baseGame.getPlayer(1));
		assertEquals("Tester3", baseGame.getPlayer(2));
		assertEquals(5, baseGame.getBoard().length);
		assertEquals(5, baseGame.getBoard()[0].length);
	}
	
	/**
	 * Testing constructor exception handling for null players array.
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void TestConstructorException() throws Exception{
		baseGame = new ConnectFiveGame(4, 4, null);
	}

	/**
	 * Testing initBoard method.
	 */
	@Test
	public void TestInitBoard(){
		/* Verify initBoard clearing all symbols on the board */
		String initialBoard = baseGame.toString();
		baseGame.doMove(2);
		baseGame.doMove(4);
		baseGame.doMove(1);
		assertNotEquals(initialBoard, baseGame.toString());
		baseGame.initBoard();
		assertEquals(initialBoard, baseGame.toString());
		
		/* Verify currentPlayerIndex reset */
		assertEquals(0, baseGame.getCurrentPlayerIndex());
		baseGame.endPlayerTurn();
		assertNotEquals(0, baseGame.getCurrentPlayerIndex());
	}
	
	/**
	 * Testing setting player name.
	 */
	@Test
	public void TestSetPlayerName(){
		assertEquals("Player1", baseGame.getPlayer(0));
		assertEquals("Player2", baseGame.getPlayer(1));
		baseGame.setPlayerName(0, "Tester1");
		baseGame.setPlayerName(1, "Tester2");
		assertEquals("Tester1", baseGame.getPlayer(0));
		assertEquals("Tester2", baseGame.getPlayer(1));
		
		/* Testing Invalid Names */
		assertEquals("Tester1", baseGame.getPlayer(0));
		baseGame.setPlayerName(0, null);
		assertEquals("Tester1", baseGame.getPlayer(0));
		baseGame.setPlayerName(0, "");
		assertEquals("Tester1", baseGame.getPlayer(0));
		baseGame.setPlayerName(0, " ");
		assertEquals("Tester1", baseGame.getPlayer(0));
		
		try{
			baseGame.setPlayerName(-1, "TestException");
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
		
		try{
			baseGame.setPlayerName(2, "TestException");
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
	}
	
	/**
	 * Testing doMove method.
	 * @throws Exception
	 */
	@Test
	public void TestDoMove() throws Exception{
		/* Make move for all spaces on the board */
		for (int i=0; i<9; ++i)
			for (int j=0; j<6; ++j)
				assertNotEquals(-1, baseGame.doMove(i));
		baseGame.initBoard();
		
		/* Verify doMove proper position of symbols on the board */
		assertEquals('X', baseGame.getCurrentPlayerSymbol());
		
		assertEquals(5, baseGame.doMove(0));
		assertEquals(baseGame.getCurrentPlayerSymbol(), baseGame.getBoard()[0][5]);
		assertEquals(4, baseGame.doMove(0));
		assertEquals(baseGame.getCurrentPlayerSymbol(), baseGame.getBoard()[0][4]);
		assertEquals(5, baseGame.doMove(1));
		assertEquals(baseGame.getCurrentPlayerSymbol(), baseGame.getBoard()[1][5]);
		
		baseGame.endPlayerTurn();
		assertEquals('O', baseGame.getCurrentPlayerSymbol());
		
		assertEquals(3, baseGame.doMove(0));
		assertEquals(baseGame.getCurrentPlayerSymbol(), baseGame.getBoard()[0][3]);
		assertEquals(2, baseGame.doMove(0));
		assertEquals(baseGame.getCurrentPlayerSymbol(), baseGame.getBoard()[0][2]);
		
		/* Testing Invalid Moves */
		String preMoveBoard = baseGame.toString();
		assertEquals(-1, baseGame.doMove(-1));
		assertEquals(preMoveBoard, baseGame.toString());
		
		preMoveBoard = baseGame.toString();
		assertEquals(-1, baseGame.doMove(9));
		assertEquals(preMoveBoard, baseGame.toString());
	}
	
	/**
	 * Testing gameOver method.
	 * @throws Exception
	 */
	@Test
	public void TestGameOver() throws Exception{
		/* Game cannot be over when just initialised */
		baseGame = new ConnectFiveGame(5, 5, null);
		assertEquals(false, baseGame.isGameOver());
		
		/* Fill all spaces except last column */
		for (int i=0; i<4; ++i)
			for (int j=0; j<5; ++j){
				assertNotEquals(-1, baseGame.doMove(i));
				assertEquals(false, baseGame.isGameOver());
				baseGame.endPlayerTurn();
			}
		
		/* Alternate between players and fill last column */
		assertEquals(false, baseGame.isGameOver());
		assertNotEquals(-1, baseGame.doMove(4));
		baseGame.endPlayerTurn();
		assertEquals(false, baseGame.isGameOver());
		assertNotEquals(-1, baseGame.doMove(4));
		baseGame.endPlayerTurn();
		assertEquals(false, baseGame.isGameOver());
		assertNotEquals(-1, baseGame.doMove(4));
		baseGame.endPlayerTurn();
		assertEquals(false, baseGame.isGameOver());
		assertNotEquals(-1, baseGame.doMove(4));
		baseGame.endPlayerTurn();
		assertEquals(false, baseGame.isGameOver());
		assertNotEquals(-1, baseGame.doMove(4));

		assertEquals(true, baseGame.isGameOver());
	}
	
	/**
	 * Test horizontal victory checking.
	 */
	@Test
	public void TestHorizontalWin(){
		/* Horizontal line of the same players symbol - win */
		int row = 0;
		for (int column=0; column<4; ++column){
			row = baseGame.doMove(column);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(column, row));
		}
		row = baseGame.doMove(4);
		assertNotEquals(-1, row);
		assertEquals(true, baseGame.isGameWon(4, row));
		
		/* Horizontal line of symbols but alternating players - no win */
		baseGame.initBoard();
		row = 0;
		for (int column=0; column<5; ++column){
			row = baseGame.doMove(column);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(column, row));
			baseGame.endPlayerTurn();
		}
		assertEquals(false, baseGame.isGameWon(4, row));
		baseGame.endPlayerTurn();
	}
	
	/**
	 * Test vertical victory checking.
	 */
	@Test
	public void TestVerticalWin(){
		/* Vertical line of the same players symbol - win */
		int row = 0;
		int column = 0;
		for (int i=0; i<4; ++i){
			row = baseGame.doMove(column);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(column, row));
		}
		row = baseGame.doMove(column);
		assertNotEquals(-1, row);
		assertEquals(true, baseGame.isGameWon(column, row));
		
		/* Vertical line of symbols but alternating players - no win */
		baseGame.initBoard();
		row = 0;
		for (int i=0; i<5; ++i){
			row = baseGame.doMove(column);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(column, row));
			baseGame.endPlayerTurn();
		}
		assertEquals(false, baseGame.isGameWon(column, row));
		baseGame.endPlayerTurn();
	}
	
	/**
	 * Check diagonal (forward slash) victory checking.
	 */
	@Test
	public void TestDiagonalWinForward(){
		/* Create "steps" for Player1 to win in a forward slash diagonal */
		baseGame.endPlayerTurn();
		for(int i=1; i<6; ++i)
			for(int j=i; j>0; --j)
				assertNotEquals(-1, baseGame.doMove(i));
		baseGame.endPlayerTurn();
		
		int row = 0;
		for (int i=0; i<4; ++i){
			row = baseGame.doMove(i);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(i, row));
		}
		row = baseGame.doMove(4);
		assertNotEquals(-1, row);
		assertEquals(true, baseGame.isGameWon(4, row));
		
		/* Interrupted diagonal (by other player) - no win */
		baseGame.initBoard();
		baseGame.endPlayerTurn();
		for(int i=1; i<6; ++i)
			for(int j=i; j>0; --j)
				assertNotEquals(-1, baseGame.doMove(i));
		
		assertNotEquals(-1, baseGame.doMove(2)); //Broken diagonal for Player1 by Player2
		baseGame.endPlayerTurn();
		
		row = 0;
		for (int i=0; i<4; ++i){
			row = baseGame.doMove(i);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(i, row));
		}
		row = baseGame.doMove(4);
		assertNotEquals(-1, row);
		assertEquals(false, baseGame.isGameWon(4, row));
	}
	
	/**
	 * Check diagonal (backward slash) victory checking.
	 */
	@Test
	public void TestDiagonalWinBackward(){
		/* Create "steps" for Player1 to win in a backward slash diagonal */
		baseGame.endPlayerTurn();
		for(int i=0; i<6; ++i)
			for(int j=6-i; j>0; --j)
				assertNotEquals(-1, baseGame.doMove(i));
		baseGame.endPlayerTurn();
		
		int row = 0;
		for (int i=1; i<5; ++i){
			row = baseGame.doMove(i);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(i, row));
		}
		row = baseGame.doMove(5);
		assertNotEquals(-1, row);
		assertEquals(true, baseGame.isGameWon(5, row));
		
		/* Interrupted diagonal (by other player) - no win */
		baseGame.initBoard();
		baseGame.endPlayerTurn();
		for(int i=0; i<6; ++i)
			for(int j=6-i; j>0; --j)
				assertNotEquals(-1, baseGame.doMove(i));
		
		assertNotEquals(-1, baseGame.doMove(3)); //Broken diagonal for Player1 by Player2
		baseGame.endPlayerTurn();

		row = 0;
		for (int i=1; i<5; ++i){
			row = baseGame.doMove(i);
			assertNotEquals(-1, row);
			assertEquals(false, baseGame.isGameWon(i, row));
		}
		row = baseGame.doMove(5);
		assertNotEquals(-1, row);
		assertEquals(false, baseGame.isGameWon(5, row));
	}
	
	/**
	 * Check invalid moves for gameWon method.
	 */
	@Test
	public void TestGameWonInvalidParameters(){
		try{
			baseGame.isGameWon(-1, 0);
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
		
		try{
			baseGame.isGameWon(0, -1);
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
		
		try{
			baseGame.isGameWon(9, 0);
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
		
		try{
			baseGame.isGameWon(0, 0, -1);
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
		
		try{
			baseGame.isGameWon(1, 0, 2);
			fail("Index out of bounds");
		} catch(IndexOutOfBoundsException e){}
	}
}
