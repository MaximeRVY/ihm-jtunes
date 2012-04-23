package view.gauche;

import javax.swing.JFrame;

import model.LibraryModel;
import model.PlayModel;

import controller.LibraryController;
import controller.PlayController;

public class LeftContainer {
	private JFrame principalFrame;
	private LibraryController libraryController;
	private LibraryModel libraryModel;
	private PlayModel playModel;
	private PlayController playController;
	
	public LeftContainer(JFrame frame, LibraryController libCon, LibraryModel libMod, PlayController playCon, PlayModel playMod) {
		this.principalFrame = frame;
		this.libraryController = libCon;
		this.libraryModel = libMod;
		this.playController = playCon;
		this.playModel = playMod;
	}
	
	public void createInPlayFrame(){
		
	}
	
	public void createMenuFrame(){
		
	}

}
