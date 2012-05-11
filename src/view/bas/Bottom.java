package view.bas;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.PlayModel;
import controller.PlayController;

public class Bottom implements Observer {

	private JFrame principalFrame;
	private PlayController controller;
	private PlayModel model;
	private JPanel panelBottom;
	
	public Bottom(JFrame principalFrame, PlayModel playModel, PlayController playController){
		this.principalFrame = principalFrame;
		this.controller = playController;
		this.model = playModel;
		createBottom();
	}
	
	private void createBottom(){
		this.panelBottom = new JPanel();
		this.panelBottom.setPreferredSize(new Dimension(800,100));
		this.panelBottom.setMinimumSize(new Dimension(800,100));
		this.panelBottom.setMaximumSize(new Dimension(800,100));
		this.panelBottom.setLayout(new BoxLayout(this.panelBottom, BoxLayout.X_AXIS));
		
		// Color
		this.panelBottom.setBackground(Color.BLUE);
		
		this.model.addObserver(this);
		
		this.principalFrame.getContentPane().add(this.panelBottom);	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
