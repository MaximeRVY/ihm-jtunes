package controller;

import java.util.Map;

import model.LibraryModel;
import model.PlaylistModel;

public class PlayListController {
	private PlaylistModel playlistModel;
	private LibraryModel libraryModel;
	
	public PlayListController(PlaylistModel playlistModel, LibraryModel libraryModel) {
		this.playlistModel = playlistModel;
		this.libraryModel = libraryModel;
	}
	
	public void CreatePlaylist(String name){
		playlistModel.add(name);
	}
	
	public void addOneSongToPlaylist(String namePlaylist, Integer idSong){
		Map<String, Object> song = libraryModel.findById(idSong);
		playlistModel.addOneSong(namePlaylist, song);
	}
	
	public void changeToPlaylistOrLibrary(String name){
		if(name.equals("library")){
			libraryModel.sendLibraryToView();
		}else{
			System.out.println("Change playlist");
		}
	}

}
