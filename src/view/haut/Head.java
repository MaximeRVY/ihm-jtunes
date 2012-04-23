package view.haut;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import controller.LibraryController;

import model.LibraryModel;
//TODO Auto-generated constructor stub
public class Head implements Observer{
	private JFrame principalFrame;
	private LibraryModel model;
	private LibraryController controller;
	
	public Head(LibraryModel model, LibraryController controller, JFrame frame) {
		this.principalFrame = frame;
		this.controller = controller;
		this.model = model;
	}
	
	public void add_search(){
		//Add search button 
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
