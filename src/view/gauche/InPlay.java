package view.gauche;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.PlayModel;
import controller.PlayController;

public class InPlay implements Observer{

	private JPanel panelGauche;
	private PlayController controller;
	private PlayModel model;
	private JPanel panelInPlay;
	private JPanel panelLect;
	
	public InPlay(JPanel panelGauche, PlayModel playModel, PlayController playController){
		this.panelGauche = panelGauche;
		this.controller = playController;
		this.model = playModel;
		createInPlay();
	}
	
	private void createInPlay(){
		this.panelInPlay = new JPanel();
		
		this.panelLect = new JPanel();
		this.panelLect.setPreferredSize(new Dimension(200,200));
		this.panelLect.setMinimumSize(new Dimension(200,200));
		this.panelLect.setMaximumSize(new Dimension(200,200));
		this.panelLect.setLayout(new BoxLayout(this.panelLect, BoxLayout.Y_AXIS));
		
		this.panelLect.setBackground(Color.GREEN);
		
		this.panelLect.add(new JLabel("coucou"));
		this.panelLect.add(new JLabel("coucou"));
		
		this.panelInPlay.add(this.panelLect);
		
		this.model.addObserver(this);
		
		this.panelGauche.add(this.panelInPlay);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
	}

}
