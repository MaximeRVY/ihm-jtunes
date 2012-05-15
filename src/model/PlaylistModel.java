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
		
	}
	
	public List<Map<String, Object>> GetAllPlaylist(){
		return allPlaylist;
		
	}

}
