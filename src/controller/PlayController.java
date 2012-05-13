package controller;

import java.util.List;
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
		System.out.println(id);
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
	
	public void next(){
		playModel.next();
	}
	
	public void changeQueueList(List<Map<String, Object>> list){
		playModel.setQueue(list);
	}
	
}
