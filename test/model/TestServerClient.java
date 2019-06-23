package model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test class for testing ServerClient model class.
 * @author Oleksandr Kononov
 *
 */
public class TestServerClient extends Thread{

	/** Server side variables */
	private ServerSocket server;
	private ServerClient serverClient;
	
	/** Client side variables */
	private Socket client;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	
	/**
	 * Method to run before every test, establishes a connection between server and client.
	 */
	@Before
	public void setup(){
		try {
			server = new ServerSocket(8000);
			new Thread(this).start();
			serverClient = new ServerClient(server.accept(), 0);
		} catch (IOException e) {
			System.err.println("Could not create server socket!\n" + e.getMessage());
		}
	}
	
	/**
	 * Thread for establishing client connection to server.
	 */
	@Override
	public void run(){
		try {
			client = new Socket("localhost", 8000);
			fromServer = new ObjectInputStream(client.getInputStream());
			toServer = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			System.err.println("Client could not connect to server socket");
		}
	}
	
	/**
	 * Close all connections after all tests.
	 */
	@After
	public void tearDown(){
		try {
			toServer.close();
			fromServer.close();
			client.close();
			serverClient.disconnect();
			server.close();
		} catch (IOException e) {
			System.err.println("Could not close sockets in tearDown");
		}
	}
	
	/**
	 * Tests connect method of ServerClient
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testConnect() throws ClassNotFoundException, IOException {
		assertEquals("Player1", serverClient.getClientName());
		
		/* Connect to client on a separate Thread to simulate server */
		new Thread(new Runnable(){
			@Override
			public void run() {
				serverClient.connect();
			}
		}).start();
		
		/* Verify connection request data from server */
		ServerClientData data = (ServerClientData) fromServer.readObject();
		assertNotNull(data);
		assertEquals("", data.board);
		assertEquals(0, data.fromPlayerIndex);
		assertEquals(ServerClient.CONNECTION_QUERY, data.data);
		assertEquals(true, data.clearText);
		assertEquals(true, data.yourTurn);
		
		toServer.writeObject(new ServerClientData(0, null, "tester", false, false));
		toServer.flush();
		
		/* Allow connection to finish */
		try { Thread.sleep(100); } catch (InterruptedException e) {}
		
		/* Verify connection response data from client */
		assertEquals("tester", serverClient.getClientName());
	}

	/**
	 * Test disconnect method for ServerClient.
	 */
	@Test
	public void testDisconnect(){
		assertEquals(true, serverClient.isConnected());
		serverClient.disconnect();
		assertEquals(false, serverClient.isConnected());

		serverClient.send(null, "test", false, false);
		assertNull(serverClient.receive());
	}
}
