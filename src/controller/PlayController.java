package controller;

import java.util.Map;

import model.LibraryModel;
import model.PlayModel;

public class PlayController {
	private PlayModel playModel;
	private LibraryModel libraryModel;
	
	
	public PlayController(PlayModel playModel, LibraryModel libraryModel) {
		this.playModel = playModel;
		this.libraryModel = libraryModel;
	}
	
	public void loadAndPlay(Integer id){
		Map<String, Object> file = libraryModel.findById(id);
		playModel.load(file);
		playModel.PlayPause();
	}
	
	public void playPause(){
		playModel.PlayPause();
	}
	
	public void stop(){
		playModel.stop();
	}
	
	public void changeVolume(float volume){
		playModel.setVolume(volume);
	} 
	
	public void changePosition(int position){
		playModel.setPosition(position);
	}
}
