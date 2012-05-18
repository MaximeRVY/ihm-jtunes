package model;

import helper.HelpForList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class PlaylistModel extends Observable{
	
	private Integer lastId;
	private List<Map<String, Object>> allPlaylist;
	
	public PlaylistModel() {
		allPlaylist = new ArrayList<Map<String,Object>>();
		lastId = 0;
	}
	
	public void add(String name){
		this.lastId += 1;
		Map<String, Object> playlist = new HashMap<String, Object>();
		
		playlist.put("name", name);
		playlist.put("id", lastId);
		playlist.put("songs", new ArrayList<Map<String, Object>>());
		allPlaylist.add(playlist);
		setChanged();
		notifyObservers("new_playlist");
	}
	
	public void addOneSong(String namePlaylist, Map<String, Object> song){
		Integer index = HelpForList.instance.indexByName(allPlaylist, namePlaylist);
		if(index != -1){
			Map<String, Object> playlist = allPlaylist.get(index);
			((List<Map<String, Object>>) playlist.get("songs")).add(song);
		}
		setChanged();
		notifyObservers("new_music_into_playlist:"+namePlaylist);
	}
	
	public List<Map<String, Object>> GetAllPlaylist(){
		return allPlaylist;
		
	}

	public void importAllplaylist(List<Map<String, Object>> allPlaylist2) {
		this.allPlaylist.addAll(allPlaylist2);
		this.lastId = allPlaylist2.size();
		System.out.println(this.lastId);
	}

	public void sendPlaylistToView(String name) {
		setChanged();
		notifyObservers("view_playlist:"+name);
		
	}
	
	public List<Map<String, Object>> findSongsByName(String name){
		for(Map<String, Object> playlist : this.allPlaylist){
			if( ((String) playlist.get("name")).equals(name) ){
				return (List<Map<String, Object>>) playlist.get("songs");
			}
		}
		return null;
	}
	public Map<String, Object> findByName(String name){
		for(Map<String, Object> playlist : this.allPlaylist){
			if( ((String) playlist.get("name")).equals(name) ){
				return playlist;
			}
		}
		return null;
	}

	public void removeToPlaylist(String playlistNameSaw, Integer id_song) {
		List<Map<String, Object>> songs = findSongsByName(playlistNameSaw);
		Integer index = HelpForList.instance.indexById(songs, id_song);
		Integer index_name = HelpForList.instance.indexByName(allPlaylist, playlistNameSaw);
		Map<String, Object> song = ((List<Map<String, Object>>) allPlaylist.get(index_name).get("songs")).get(index);
		((List<Map<String, Object>>) allPlaylist.get(index_name).get("songs")).remove(song);
		setChanged();
		notifyObservers("remove_to_playlist:"+id_song+"/"+playlistNameSaw);
	}

}
