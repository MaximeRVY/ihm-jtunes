package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class CreatePlaylist extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextField textField;
	private JButton ok;
	
	public CreatePlaylist(){
		super(new JFrame(), "Create a playlist", true);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		
		textField = new JTextField();
		textField.setMaximumSize(new Dimension(200, 30));
		textField.addActionListener(this);
		ok = new JButton("OK");
		ok.addActionListener(this);
		
		this.getContentPane().add(textField);
		this.getContentPane().add(ok);
		
		this.pack();
		this.setSize(new Dimension(250, 75));
		this.setLocationRelativeTo(null);
	}
	
	public void startDialog(){
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
	}
	
	public String getTextField(){
		return textField.getText();
	}
	
}
