package view.bas;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JLabel labelInTime;
	private JSlider sliderTime;
	private JLabel labelEndTime;
	private JSlider sliderVolume;
	
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
		
		previous = new JButton("Previous");
		
		playPause = new JButton("Play / Pause");
		
		next = new JButton("Next");
		
		labelInTime = new JLabel("--:--");
		
		sliderTime = new JSlider(0,100,0);
		sliderTime.setPreferredSize(new Dimension(200,20));
		sliderTime.setMinimumSize(new Dimension(200,20));
		sliderTime.setMaximumSize(new Dimension(200,20));
		sliderTime.setEnabled(false);
		
		labelEndTime = new JLabel("--:--");
		
		JLabel labelVolume = new JLabel("Volume :");
		
		sliderVolume = new JSlider(0,100,100);
		sliderVolume.setPreferredSize(new Dimension(100,20));
		sliderVolume.setMinimumSize(new Dimension(100,20));
		sliderVolume.setMaximumSize(new Dimension(100,20));
		
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(previous);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(playPause);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(next);
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(labelInTime);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(sliderTime);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(labelEndTime);
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(labelVolume);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
		this.panelBottom.add(sliderVolume);
		
		this.model.addObserver(this);
		
		this.principalFrame.getContentPane().add(this.panelBottom);	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
