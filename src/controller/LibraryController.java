package controller;

import model.LibraryModel;

public class LibraryController {
	private LibraryModel model;

	public LibraryController(LibraryModel model) {
		this.model = model;
	}
	
	public void importFile(String path){
		model.SaveFileInModel(path);
	}

	public void importFolder(String absolutePath) {
		model.saveFolderInModel(absolutePath);		
	}
	
}
