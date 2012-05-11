package view.centre;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.LibraryModel;
import controller.LibraryController;

public class AllFiles implements Observer {

	private JPanel panelCentre;
	private LibraryModel model;
	private LibraryController controller;
	private JPanel allFilesPanel;
	
	public AllFiles(JPanel panelCentre, LibraryModel model, LibraryController controller){
		this.panelCentre = panelCentre;
		this.model = model;
		this.controller = controller;
		createAllFiles();
	}
	
	private void createAllFiles(){
		this.allFilesPanel = new JPanel();
		this.allFilesPanel.setPreferredSize(new Dimension(600,650));
		this.allFilesPanel.setMinimumSize(new Dimension(600,650));
		this.allFilesPanel.setMaximumSize(new Dimension(600,650));
		this.allFilesPanel.setLayout(new BoxLayout(this.allFilesPanel, BoxLayout.Y_AXIS));
		
		//Color
		this.allFilesPanel.setBackground(Color.RED);
		
		this.model.addObserver(this);
		
		this.panelCentre.add(this.allFilesPanel);	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
