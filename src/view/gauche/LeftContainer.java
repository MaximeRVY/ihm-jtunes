package view.gauche;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.LibraryModel;
import model.PlayModel;
import model.PlaylistModel;
import controller.LibraryController;
import controller.PlayController;
import controller.PlayListController;

public class LeftContainer{
	private JPanel panelCentre;
	private LibraryController libraryController;
	private LibraryModel libraryModel;
	private PlayModel playModel;
	private PlayController playController;
	private PlaylistModel playlistModel;
	private PlayListController playlistController;
	private JPanel panelGauche;
	private InPlay inPlay;
	private LeftMenu leftMenu;
	
	
	public LeftContainer(JPanel panelCentre, LibraryController libCon, LibraryModel libMod, PlayController playCon, PlayModel playMod, PlayListController playlistController, PlaylistModel playlistModel) {
		this.panelCentre = panelCentre;
		this.libraryController = libCon;
		this.libraryModel = libMod;
		this.playController = playCon;
		this.playModel = playMod;
		this.playlistController = playlistController;
		this.playlistModel = playlistModel;
		createLeftContainer();
	}
	
	private void createLeftContainer(){
		this.panelGauche = new JPanel();
		this.panelGauche.setPreferredSize(new Dimension(200,550));
		this.panelGauche.setMinimumSize(new Dimension(200,550));
		this.panelGauche.setMaximumSize(new Dimension(200,550));
		this.panelGauche.setLayout(new BoxLayout(this.panelGauche, BoxLayout.Y_AXIS));
		
		createMenuPanel();
		createInPlayPanel();
		
		this.panelCentre.add(this.panelGauche);
	}
	
	private void createInPlayPanel(){
		this.inPlay = new InPlay(this.panelGauche, this.playModel, this.playController);
	}
	
	private void createMenuPanel(){
		this.leftMenu = new LeftMenu(this.panelGauche, this.libraryModel, this.libraryController, this.playlistController, this.playlistModel);
	}
	
	public InPlay getInPlay(){
		return this.inPlay;
	}

	public LeftMenu getLeftContainer(){
		return this.leftMenu;
	}
}
