package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.LibraryModel;
import model.PlayModel;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import view.bas.Bottom;
import view.centre.AllFiles;

public class PlayController {
	private PlayModel playModel;
	private LibraryModel libraryModel;
	private Bottom bottomView;
	private AllFiles allFilesView;
	
	
	public PlayController(PlayModel playModel, LibraryModel libraryModel) {
		this.playModel = playModel;
		this.libraryModel = libraryModel;
	}
	
	public void addBottomView(Bottom bottomView){
		this.bottomView = bottomView;
	}
	
	public void addAllFilesView(AllFiles allFilesView){
		this.allFilesView = allFilesView;
	}
	
	public void loadAndPlay(Integer id){
		Map<String, Object> file = libraryModel.findById(id);
		playModel.load(file);
		playModel.PlayPause();
		changeVolume(bottomView.getVolume());
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
	
	public void changeInPlayList(List<Map<String, Object>> list){
		playModel.setQueue(list);
	}
	
	public void previous(){
		playModel.previous();
	}
	
	public Map<String,Object> getInformationsMp3(){
		MP3File mp3 = null;
		try {
			mp3 = new MP3File(new File((String) playModel.getCurrentPlayed().get("pathname")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return libraryModel.getInformationsMp3(mp3);
	}
}
