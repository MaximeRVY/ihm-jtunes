package view.gauche;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.PlayModel;
import controller.PlayController;

public class InPlay implements Observer{

	private JPanel panelGauche;
	private PlayController controller;
	private PlayModel model;
	private JPanel panelInPlay;
	
	public InPlay(JPanel panelGauche, PlayModel playModel, PlayController playController){
		this.panelGauche = panelGauche;
		this.controller = playController;
		this.model = playModel;
		createInPlay();
	}
	
	private void createInPlay(){
		this.panelInPlay = new JPanel();
		this.panelInPlay.setPreferredSize(new Dimension(300,200));
		this.panelInPlay.setMinimumSize(new Dimension(300,200));
		this.panelInPlay.setMaximumSize(new Dimension(300,200));
		this.panelInPlay.setLayout(new BoxLayout(this.panelInPlay, BoxLayout.Y_AXIS));
		
		//color
		this.panelInPlay.setBackground(Color.GREEN);
		
		this.model.addObserver(this);
		
		this.panelGauche.add(this.panelInPlay);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
	}

}
