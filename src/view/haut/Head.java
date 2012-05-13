package view.haut;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.LibraryModel;
import controller.LibraryController;

public class Head implements Observer{
	private JFrame principalFrame;
	private LibraryModel model;
	private LibraryController controller;
	private JTextField searchText;
	private JPanel headPanel;
	
	public Head(LibraryModel model, LibraryController controller, JFrame frame) {
		this.principalFrame = frame;
		this.controller = controller;
		this.model = model;
		this.searchText = new JTextField();
		add_search();
	}
	
	public void add_search(){
		this.headPanel = new JPanel();
		this.headPanel.setPreferredSize(new Dimension(800,50));
		this.headPanel.setMinimumSize(new Dimension(800,50));
		this.headPanel.setMaximumSize(new Dimension(800,50));
		this.headPanel.setLayout(new BoxLayout(this.headPanel, BoxLayout.X_AXIS));
		
		this.headPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		
		this.headPanel.add(new JLabel("Search :"));
		
		this.headPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		this.searchText.setToolTipText("Search");
		this.searchText.setMinimumSize(new Dimension(140, 25));
		this.searchText.setMinimumSize(new Dimension(140, 25));
		this.searchText.setMaximumSize(new Dimension(140, 25));
		// Ajout d'un listener sur searchText
		this.searchText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				model.editJTable(searchText.getText());
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.headPanel.add(this.searchText);
		
		this.principalFrame.getContentPane().add(this.headPanel);
		
		this.model.addObserver(this);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
