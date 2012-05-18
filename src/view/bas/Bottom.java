package view.bas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.PlayModel;
import controller.PlayController;

public class Bottom implements Observer {

	private JFrame principalFrame;
	private PlayController controller;
	private PlayModel model;
	private JPanel panelBottom;
	private JButton stop;
	private JButton previous;
	private JButton playPause;
	private JButton next;
	private JLabel labelInTime;
	private JSlider sliderTime;
	private JLabel labelEndTime;
	private JSlider sliderVolume;
	private JButton random;
	
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
		
		stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.stop();
				playPause.setText("Play");
			}
		});
		
		previous = new JButton("Previous");
		previous.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.previous();
				
			}
		});
		
		playPause = new JButton("Play");
		playPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.playPause();
				
			}
		});
		
		next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.next();
				
			}
		});
		
		labelInTime = new JLabel("--:--");
		
		sliderTime = new JSlider(0,200,0);
		sliderTime.setPreferredSize(new Dimension(200,20));
		sliderTime.setMinimumSize(new Dimension(200,20));
		sliderTime.setMaximumSize(new Dimension(200,20));
		sliderTime.setEnabled(false);
		sliderTime.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.out.println(sliderTime.getValue()+" "+ sliderTime.getMaximum());
				controller.changePosition(sliderTime.getValue());
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		labelEndTime = new JLabel("--:--");
		
		JLabel labelVolume = new JLabel("Volume :");
		
		sliderVolume = new JSlider(50,100,100);
		sliderVolume.setOrientation(SwingConstants.VERTICAL);
		sliderVolume.setPreferredSize(new Dimension(20,60));
		sliderVolume.setMinimumSize(new Dimension(20,60));
		sliderVolume.setMaximumSize(new Dimension(20,60));
		sliderVolume.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				controller.changeVolume(new Integer(sliderVolume.getValue()).floatValue() / 100);
			}
		});
		
		random = new JButton("Random");
		random.setBackground(Color.LIGHT_GRAY);
		random.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(model.getRandom()){
					random.setBackground(Color.LIGHT_GRAY);
					controller.unSetRandom();
				}else{
					controller.setRandom();
					random.setBackground(Color.GREEN);
				}
				
			}
		});
		
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(stop);
		this.panelBottom.add(Box.createRigidArea(new Dimension(10, 0)));
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
		this.panelBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		this.panelBottom.add(random);
		
		this.model.addObserver(this);
		
		this.principalFrame.getContentPane().add(this.panelBottom);	
	}
	
	public float getVolume(){
		return new Integer(this.sliderVolume.getValue()).floatValue() / 100;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if((String) arg == "play"){
			this.playPause.setText("Pause");
		}else if((String) arg == "pause"){
			if(this.playPause.getText().equals("Pause"))
				this.playPause.setText("Play");
			else
				this.playPause.setText("Pause");
		}else{
			int sec;
			int min;
			String sep = ":";
			
			if((String) arg == "load" || (String) arg == "change_current_played"){
				int duration = this.model.getDuration();
				sec = (duration / 1000) % 60;
		        min = (duration / 1000) / 60;
		        if(sec<10)
		        	sep=":0";       
				this.labelEndTime.setText(min+sep+sec);
				this.sliderTime.setEnabled(true);
				this.sliderTime.setMaximum(duration);
			}
			
			int position = this.model.getPosition();
			sec = (position / 1000) % 60;
	        min = (position / 1000) / 60;
	        sep = ":";
	        if(sec<10)
	        	sep=":0";       
			this.labelInTime.setText(min+sep+sec);
			this.sliderTime.setValue(position);
		}
	}

}
