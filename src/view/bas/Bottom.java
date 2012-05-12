package view.bas;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import model.PlayModel;
import controller.PlayController;

public class Bottom implements Observer {

	private JFrame principalFrame;
	private PlayController controller;
	private PlayModel model;
	private JPanel panelBottom;
	private JButton previous;
	private JButton playPause;
	private JButton next;
	private JSlider sliderTime;
	
	public Bottom(JFrame principalFrame, PlayModel playModel, PlayController playController){
		this.principalFrame = principalFrame;
		this.controller = playController;
		this.model = playModel;
		createBottom();
	}
	
	private void createBottom(){
		this.panelBottom = new JPanel();
		this.panelBottom.setPreferredSize(new Dimension(800,70));
		this.panelBottom.setMinimumSize(new Dimension(800,70));
		this.panelBottom.setMaximumSize(new Dimension(800,70));
		this.panelBottom.setLayout(new BoxLayout(this.panelBottom, BoxLayout.X_AXIS));
		
		// Color
		this.panelBottom.setBackground(Color.BLUE);
		
		previous = new JButton("Previous");
		
		playPause = new JButton("Play / Pause");
		
		next = new JButton("Next");
		
		sliderTime = new JSlider(0,100,0);
		sliderTime.setPreferredSize(new Dimension(200,30));
		sliderTime.setMinimumSize(new Dimension(200,30));
		sliderTime.setMaximumSize(new Dimension(200,30));
		sliderTime.setEnabled(false);
		
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(previous);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(playPause);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(next);
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(sliderTime);
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		
		this.model.addObserver(this);
		
		this.principalFrame.getContentPane().add(this.panelBottom);	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
