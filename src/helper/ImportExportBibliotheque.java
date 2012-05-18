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

import model.LibraryModel;

public class ImportExportBibliotheque implements Observer{
	private LibraryModel model;
	private static String basePath = "base/bibliotheque.sqlite";
	
	public ImportExportBibliotheque(LibraryModel lib){
		this.model = lib;
	}
	
	public void importAllSongs(){
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
			
			try{
				statement.executeUpdate("create table songs (id integer PRIMARY KEY,title string, artist string, album string, genre string," +
						"year string, duration string, pathname string)");
			}catch(Exception e){
				
			}
			
		    //statement.executeUpdate("insert into songs values(1, 'Sans titre', 'Artiste', 'album', 'genre', '1990', '5:02', 'toto')");
		    ResultSet rs = statement.executeQuery("select * from songs");
		    Map<String, Object> song;
		    List<Map<String, Object>> songs = new ArrayList<Map<String,Object>>();
		    while(rs.next())
		    {
		    	song = new HashMap<String, Object>();
		    	
		    	song.put("id", rs.getInt("id"));
		    	song.put("title",rs.getString("title"));
		    	song.put("artist", rs.getString("artist"));
		    	song.put("album", rs.getString("album"));
		    	song.put("genre", rs.getString("genre"));
		    	song.put("year", rs.getString("year"));
		    	song.put("duration", rs.getString("duration"));
		    	song.put("year", rs.getInt("nb"));
		    	song.put("pathname", rs.getString("pathname"));
		    	
		    	songs.add(song);
		     }
		    
		    this.model.importBibliotheque(songs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
	
	
	public void exportOneSong(Map<String,Object> song){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + basePath);
			//Statement statement = connection.createStatement();
			//statement.setQueryTimeout(30);
			PreparedStatement statement = connection.prepareStatement("insert into songs" +
					"(id, title, artist, album, genre, year, duration , pathname)" +
					" values" +
					"(? , ? , ? , ? , ? , ? , ? , ?);");
			String title = (String) song.get("title");
			String artist = (String) song.get("artist");
			String album = (String) song.get("album");
			String genre = (String) song.get("genre");
			String year = (String) song.get("year");
			String duration = (String) song.get("duration");
			Integer nb = (Integer) song.get("nb");
			Integer id = (Integer) song.get("id");
			String pathname = (String) song.get("pathname");
			
			statement.setInt(1, id);
			statement.setString(2,title);
			statement.setString(3, artist);
			statement.setString(4, album);
			statement.setString(5, genre);
			statement.setString(6, year);
			statement.setString(7, duration);
			statement.setString(8, pathname);
			statement.setInt(9, nb);
			statement.executeUpdate();
			statement.close();
			//System.out.println("insert into songs values("+id+", '"+title+"', '"+artist+"', '"+album+"', '"+genre+"', '"+year+"', '"+duration+"', '"+pathname+"')");
			
			//statement.executeUpdate("insert into songs values("+id+", '"+title+"', '"+artist+"', '"+album+"', '"+genre+"', '"+year+"', '"+duration+"', '"+pathname+"')");
			 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
	public void update(Observable o, Object arg) {
		if(arg == null){
			List<Map<String,Object>> bibliotheque = this.model.getBibliotheque();
			exportOneSong(bibliotheque.get(bibliotheque.size()-1));
		}
		
	}
	

}
