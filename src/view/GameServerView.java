package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * 
 * The View class for the game server.
 * @author Oleksandr Kononov
 *
 */
public class GameServerView extends JFrame{

	private static final long serialVersionUID = -685456591411784579L;
	
	/** Server text area for server internal information */
	private JTextArea textArea;
	
	public GameServerView(){
		setTitle("Connect Game Server");
		setSize(300, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		/* Setup text area */
		textArea = new JTextArea();
		textArea.setEditable(false);
		getContentPane().add(textArea, BorderLayout.CENTER);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Clears the text area.
	 */
	public void clearTextArea(){
		textArea.setText("");
	}
	
	/**
	 * Set the text in the text area to a given String.
	 * @param text
	 */
	public void setTextArea(String text){
		textArea.setText(text);
	}
	
	/**
	 * Appends a given String to the text area.
	 * @param text
	 */
	public void appendTextArea(String text){
		textArea.append(text+"\n");
	}
}
