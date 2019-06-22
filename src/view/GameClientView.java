package view;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JTextArea;
/**
 * The View class for the game client.
 * @author Oleksandr Kononov
 *
 */
public class GameClientView extends JFrame{
	
	private static final long serialVersionUID = -8122356930424875600L;
	private JTextField textField;
	private JTextArea textArea;
	
	/**
	 * Constructor for the game client view class - GUI
	 * @param listener which will listen for input by user
	 */
	public GameClientView(ActionListener listener) {
		setTitle("Connect Game Client");
		setSize(600, 400);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		/* Setup text field */
		textField = new JTextField();
		textField.setColumns(10);
		textField.addActionListener(listener);
		getContentPane().add(textField, BorderLayout.SOUTH);
		
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
	 * Clears the text field.
	 */
	public void clearTextField(){
		textField.setText("");
	}
	
	/**
	 * Set the text in the text area to a given String.
	 * @param text
	 */
	public void setTextArea(String text){
		textArea.setText(text);
	}
	
	/**
	 * Gets the text in the textfield.
	 * @return
	 */
	public String getText(){
		return textField.getText();
	}
	
	/**
	 * Enables or disables text field
	 * @param enable
	 */
	public void enableTextField(boolean enable){
		textField.setEditable(enable);
	}
	
	/**
	 * Appends a given String to the text area.
	 * @param text
	 */
	public void appendTextArea(String text){
		textArea.append(text+"\n");
	}
}
