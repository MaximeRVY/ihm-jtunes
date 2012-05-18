package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.explodingpixels.macwidgets.SourceListItem;

import model.LibraryModel;
import model.PlaylistModel;

public class ImportExportPlaylist implements Observer{
	private PlaylistModel model;
	private LibraryModel libraryModel;
	private static String basePath = "base/bibliotheque.sqlite";
	
	public ImportExportPlaylist(PlaylistModel model, LibraryModel libraryModel){
		this.model = model;
		this.libraryModel = libraryModel;
		model.addObserver(this);
	}
	
	public void importAllPlaylist(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Connection connection;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + basePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			
			
			try{
				statement.executeUpdate("create table playlists (id integer PRIMARY KEY,name string)");
			}catch(Exception e){
				
			}
			try{
				statement.executeUpdate("create table playlist_song (id_playlist integer," +
						" id_song integer," +
						" FOREIGN KEY(id_song) REFERENCES songs(id) ON DELETE CASCADE," +
						" FOREIGN KEY(id_playlist) REFERENCES playlists(id) ON DELETE CASCADE)");
				
			}catch (Exception e) {
				//e.printStackTrace();
			}
			try {
				statement.executeUpdate("CREATE UNIQUE INDEX pk_index ON 'playlist_song'('id_song','id_playlist');");
			} catch (Exception e) {
				//e.printStackTrace();
			}
			ResultSet rs = statement.executeQuery("select * from playlists");
			List<Map<String, Object>> allPlaylist = new ArrayList<Map<String,Object>>();
			
			while(rs.next()){
				String name = rs.getString("name");				
				Integer id = rs.getInt("id");
				Map<String, Object> playlist = null;
				playlist =  new HashMap<String, Object>();
				playlist.put("id", id);
				playlist.put("name", rs.getString("name"));
				playlist.put("songs", new ArrayList<Map<String, Object>>());				
				allPlaylist.add(playlist);
				
		    }
			
			ResultSet rs2 = statement.executeQuery("select * from playlists, playlist_song where " +
					"playlists.id = playlist_song.id_playlist");
					while(rs2.next()){
						String name = rs2.getString("name");
						Integer index= HelpForList.instance.indexByName(allPlaylist, name);
						Map<String, Object> playlist = allPlaylist.get(index);
						((List<Map<String, Object>>) playlist.get("songs")).add(libraryModel.findById(rs2.getInt("id_song")));
					}
			model.importAllplaylist(allPlaylist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	
	private void save_playlist(Integer id,String name) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + basePath);
			PreparedStatement statement = connection.prepareStatement("insert into playlists (id, name)" +
					"values " +
					"(?, ?)");
			statement.setInt(1, id);
			statement.setString(2, name);
			statement.executeUpdate();
			statement.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void AddToPlaylist(Integer idPlaylist, Integer lastIdAdded) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + basePath);
			PreparedStatement statement = connection.prepareStatement("insert into playlist_song (id_playlist, id_song)" +
					"values " +
					"(?, ?)");
			statement.setInt(1, idPlaylist);
			statement.setInt(2, lastIdAdded);
			statement.executeUpdate();
			statement.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void deleteToPlaylist(Integer idPlaylist, String id_song) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + basePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			statement.executeUpdate("DELETE FROM playlist_song "+
						"WHERE id_song="+id_song+
						" AND id_playlist="+idPlaylist);
		}catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e);
			}
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String argument = (String) arg1;
		if(argument != null && argument.equals("new_playlist")){
			List<Map<String, Object>> playlists = this.model.GetAllPlaylist();
			String name = (String) playlists.get(playlists.size() - 1).get("name");
			Integer id = (Integer) playlists.get(playlists.size() - 1).get("id");
			save_playlist(id, name);
		}else if( argument != null && argument.startsWith("new_music_into_playlist:")){
			String namePlaylist = argument.split("new_music_into_playlist:")[1];
			Map<String, Object> playlist = model.findByName(namePlaylist);
			Integer idPlaylist = (Integer) playlist.get("id");
			List<Map<String, Object>> songs = (List<Map<String, Object>>) playlist.get("songs");
			Integer lastIdAdded = (Integer) songs.get(songs.size() - 1).get("id");
			AddToPlaylist(idPlaylist, lastIdAdded);
		}else if( argument != null && argument.startsWith("remove_to_playlist:")){
			String result = argument.split("remove_to_playlist:")[1];
			String namePlaylist = result.split("/")[1];
			Map<String, Object> playlist = model.findByName(namePlaylist);
			Integer idPlaylist = (Integer) playlist.get("id");
			String id_song = result.split("/")[0];
			deleteToPlaylist(idPlaylist, id_song);			
		}
		
	}
	

}
