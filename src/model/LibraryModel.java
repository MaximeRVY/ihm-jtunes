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
		Map<String, Object> file;
		try {
			file = getInformationForSave(path);
			this.last_id += 1;
			file.put("id",this.last_id);
			
			this.bibliotheque.add(file);
			setChanged();
			notifyObservers();
		} catch (Exception e) {
			// Do nothing for the moment
		}
		
		
	}
	
	private Map<String,Object> getInformationForSave(String path) throws Exception{
		File file = new File(path);
		MP3File mp3;
		Map<String,Object> informationsFile = new HashMap<String, Object>();
		mp3 = new MP3File(file);
		informationsFile = getInformationsMp3(mp3);
		
		
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
			title = "Sans Titre";
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

	public void saveFolderInModel(String absolutePath) {
		File folder = new File(absolutePath);
		
		List<File> files = new ArrayList<File>();
		addFilesRecursively(folder, files);
		
		Integer count_files = 1;
		
		for(File file : files){
			if(isMp3(file)){
				Map<String, Object> fileInfo;
				try {
					fileInfo = getInformationForSave(file.getAbsolutePath());
					if(fileInfo.get("title").equals("Sans Titre")){
						fileInfo.put("title", "Sans Titre "+count_files);
					}
					this.last_id += 1;
					fileInfo.put("id",this.last_id);
					this.bibliotheque.add(fileInfo);
					setChanged();
					notifyObservers();
					count_files += 1;
				} catch (Exception e) {
					// Do nothing
				}
			}
				
		}
		
		
	
	}
	
	public List<Map<String, Object>> getBibliotheque() {
		return bibliotheque;
	}

	private Boolean isMp3(File file){
		String fileName = file.getName();
		int mid= fileName.lastIndexOf(".");
		String ext;
		if(mid > 0){
			ext=fileName.substring(mid+1,fileName.length());
			if("mp3".equals(ext)){
				return true;
			}
		}
		return false;
	}
	
	private void addFilesRecursively(File file, List<File> all) {
	    final File[] children = file.listFiles();
	    if (children != null) {
	        for (File child : children) {
	            all.add(child);
	            addFilesRecursively(child, all);
	        }
	    }
	}

}
