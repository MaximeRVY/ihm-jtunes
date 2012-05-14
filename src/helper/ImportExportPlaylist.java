package helper;

import java.util.Observable;
import java.util.Observer;

import model.PlaylistModel;

public class ImportExportPlaylist implements Observer{
	private PlaylistModel model;
	
	public ImportExportPlaylist(PlaylistModel model){
		this.model = model;
		model.addObserver(this);
	}
	
	public void importAllSongs(){
		System.out.println("Implémenter l'import de la base de donnée ");
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
