package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

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
		Map<String,Object> informationsFile = new HashMap<String, Object>();
		
		informationsFile = getInformationsMp3(file);
		String duration = getDuration(file);
		informationsFile.put("duration", duration);
		informationsFile.put("pathname", path);
		
		
		return informationsFile;
		
		
	}
	
	public Map<String,Object> getInformationsMp3(File file){
		Mp3File mp3 = null;
		try {
			mp3 = new Mp3File(file.getAbsolutePath());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		String title="", artist="", album="", genre="", year="";
		int duration =0;
		
		if(mp3.hasId3v1Tag()){
			ID3v1 tag = mp3.getId3v1Tag();
			if(tag != null){
				try{ title = tag.getTitle(); }catch(Exception e){}
				try{ artist = tag.getArtist(); }catch(Exception e){}
				try{ album = tag.getAlbum(); }catch(Exception e){}
				try{ genre = tag.getGenreDescription(); }catch(Exception e){}
				try{ year = tag.getYear(); }catch(Exception e){}
			}
		}else if(mp3.hasId3v2Tag()){
			
			ID3v2 tag = mp3.getId3v2Tag();
			if(tag != null){
				try{ title = tag.getTitle(); }catch(Exception e){}
				try{ artist = tag.getArtist(); }catch(Exception e){}
				try{ album = tag.getAlbum(); }catch(Exception e){}
				try{ genre = tag.getGenreDescription(); }catch(Exception e){}
				try{ year = tag.getYear(); }catch(Exception e){}
			}
		}
		if(title.isEmpty()){
			String[] all = file.getName().split("-");
			title = all[all.length-1].split(".")[0];
			
			System.out.println(title);
		}	
		else if(title.startsWith("??TT2 :")){
			title = title.split("\\?\\?TT2 :")[1].trim();
		}
			
		if(artist.isEmpty()){
			artist = "Inconnu";
		}else if(artist.startsWith("??TP1 :")){
			artist = artist.split("\\?\\?TP1 :")[1].trim();
		}
			
		if(album.isEmpty()){
			album = "Album Inconnu";
		}else if(album.startsWith("??TAL :")){
			album = album.split("\\?\\?TAL :")[1].trim();
		}
			
	   if(genre == null || (genre != null && genre.isEmpty()))
		   genre = "Unknow";
	   if(year.isEmpty())
		   year = "";
		   
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
					this.last_id += 1;
					fileInfo.put("id",this.last_id);
					this.bibliotheque.add(fileInfo);
					setChanged();
					notifyObservers();
					count_files += 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
		}
	
	}
	
	public List<Map<String, Object>> getBibliotheque() {
		return bibliotheque;
	}
	
	public void importBibliotheque(List<Map<String, Object>> songs){
		this.bibliotheque.addAll(songs);
		this.last_id = songs.size();
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
	
	public String getDuration(File file) throws UnsupportedAudioFileException, IOException{
		int sec;
		int min;
		String sep = ":";
		AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
	    if (fileFormat instanceof TAudioFileFormat) {
	        Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
	        String key = "duration";
	        Long microseconds = (Long) properties.get(key);
	        int mili = (int) (microseconds / 1000);
	        sec = (mili / 1000) % 60;
	        min = (mili / 1000) / 60;
	        if(sec<10)
	        	sep = ":0";
	    } else {
	        throw new UnsupportedAudioFileException();
	    }
		return min+sep+sec;
		
	}

	public Map<String, Object> findById(Integer id) {
		for(Map<String, Object> file : this.bibliotheque){
			if( ((Integer) file.get("id")).equals(id) ){
				return file;
			}
		}
		return null;
		
	}
	

	public void editJTable(String filter){
		if(filter==null)
			filter="";
		setChanged();
		notifyObservers(filter);
	}

	public void sendLibraryToView() {
		setChanged();
		notifyObservers("view_library");
		
	}
}
