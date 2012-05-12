package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v1;
import org.farng.mp3.id3.AbstractID3v2;

public class LibraryModel extends Observable{
	// model pour la bibliotheque, les playlist et la current playlist
	private List<Map<String,Object>> bibliotheque;
	private int last_id;
	
	
	public LibraryModel() {
		bibliotheque = new ArrayList<Map<String,Object>>();
		last_id = 0;
	}
	
	public void SaveFileInModel(String path){
		Map<String,Object> file = getInformationForSave(path);
		
		this.last_id += 1;
		file.put("id",this.last_id);
		
		this.bibliotheque.add(file);
		setChanged();
		notifyObservers();
	}
	
	private Map<String,Object> getInformationForSave(String path){
		File file = new File(path);
		MP3File mp3;
		Map<String,Object> informationsFile = new HashMap<String, Object>();
		try {
			mp3 = new MP3File(file);
			informationsFile = getInformationsMp3(mp3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		informationsFile.put("pathname", path);
		
		return informationsFile;
		
		
	}
	
	private Map<String,Object> getInformationsMp3(MP3File mp3){
		
		
		String title="", artist="", album="", genre="", year="";
		
		if(mp3.hasID3v1Tag()){
			AbstractID3v1 tag = mp3.getID3v1Tag();
			if(tag != null){
				try{ title = tag.getSongTitle(); }catch(Exception e){}
				try{ artist = tag.getLeadArtist(); }catch(Exception e){}
				try{ album = tag.getAlbumTitle(); }catch(Exception e){}
				try{ genre = tag.getSongGenre(); }catch(Exception e){}
				try{ year = tag.getYearReleased(); }catch(Exception e){}
			}
		}else if(mp3.hasID3v2Tag()){
			AbstractID3v2 tag = mp3.getID3v2Tag();
			if(tag != null){
				try { title = tag.getFrame("TT2").toString();}catch(Exception e){}
				try { artist = tag.getFrame("TP1").toString();}catch(Exception e){}
				try {album = tag.getFrame("TAL").toString();}catch(Exception e){}
				try {year = tag.getFrame("TYE").toString();}catch(Exception e){}
				try { genre = tag.getFrame("TCO").toString();}catch(Exception e){}
			}
		}
		if(title.isEmpty())
			title = "Sans Tittre";
		if(artist.isEmpty())
			artist = "Inconnu";
		if(album.isEmpty())
			album = "Album Inconnu";
	   if(genre.isEmpty())
		   genre = "Genre Inconnu";
	   if(year.isEmpty())
		   year = null;
		   
	   Map<String,Object> retour = new HashMap<String, Object>();
	   
	   retour.put("title", title);
	   retour.put("artist", artist);
	   retour.put("album", album);
	   retour.put("genre", genre);
	   retour.put("year", year);
		
		return retour;
		
	}
}
