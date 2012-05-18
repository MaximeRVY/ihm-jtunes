package view.gauche;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import model.LibraryModel;
import model.PlaylistModel;
import view.CreatePlaylist;

import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;

import controller.LibraryController;
import controller.PlayListController;

public class LeftMenu implements Observer {

	private JPanel panelGauche;
	private LibraryModel libraryModel;
	private LibraryController libraryController;
	private PlayListController playlistController;
	private JPanel panelLeftMenu;
	private SourceListModel listModel;
	private SourceListCategory playlist_source_list;
	private PlaylistModel playlistModel;
	
	public LeftMenu(JPanel panelGauche, LibraryModel model, LibraryController controller, PlayListController playlistController, PlaylistModel playlistModel){
		this.panelGauche = panelGauche;
		this.libraryModel = model;
		this.libraryController = controller;
		this.playlistController = playlistController;
		this.playlistModel = playlistModel;
		
		this.playlistModel.addObserver(this);
		createLeftMenu();
	}
	
	private void createLeftMenu(){
		this.panelLeftMenu = new JPanel();
		
		this.panelLeftMenu.setLayout(new BoxLayout(this.panelLeftMenu, BoxLayout.Y_AXIS));
		
		this.listModel = new SourceListModel();
		SourceListCategory Music = new SourceListCategory("Music");
		listModel.addCategory(Music);
		listModel.addItemToCategory(new SourceListItem("My Library"), Music);
		playlist_source_list = new SourceListCategory("Playlist");
		listModel.addCategory(playlist_source_list);
		importAllplaylist();
		listModel.addItemToCategory(new SourceListItem("Add a Playlist"), playlist_source_list);
		SourceList sourceList = new SourceList(listModel);
		
		sourceList.setTransferHandler(new TransferHandler(){
			public boolean canImport(JComponent dest, DataFlavor[] flavors) {
		         return true;
		      }

		      public boolean importData(JComponent src, Transferable transferable) {
		    	  DataFlavor[] flavors = transferable.getTransferDataFlavors();
		    	  DataFlavor listFlavor = null;
		    	  DataFlavor objectFlavor = null;
		    	  DataFlavor readerFlavor = null;
		    	  int lastFlavor = flavors.length - 1;

		    	  for (int f = 0; f <= lastFlavor; f++) {
		    	      if (flavors[f].isFlavorJavaFileListType()) {
		    	        listFlavor = flavors[f];
		    	      }
		    	      if (flavors[f].isFlavorSerializedObjectType()) {
		    	        objectFlavor = flavors[f];
		    	      }
		    	      if (flavors[f].isRepresentationClassReader()) {
		    	        readerFlavor = flavors[f];
		    	      }
		    	    }
		    	  
		    	try {
		    		  DataFlavor bestTextFlavor = DataFlavor
			    	          .selectBestTextFlavor(flavors);
			    	      BufferedReader br = null;
			    	      String line = null;
			    	  if (bestTextFlavor != null){
			        	Reader r = bestTextFlavor.getReaderForText(transferable);
			            br = new BufferedReader(r);
			            line = br.readLine();
			            while (line != null) {
			            	String data = line.trim();
				            String[] result = data.split("file://");
						    if(result.length == 2){
						    	libraryController.importFile(result[1].trim());
						    }
						       
				              line = br.readLine();
			            }
			            br.close();
			          }
				} catch (Exception e) {
					// TODO: handle exception
					return false;
				}
				return true;     
		      } 
		});
		
		sourceList.addSourceListClickListener(new SourceListClickListener() {
			
			@Override
			public void sourceListItemClicked(SourceListItem arg0, Button arg1, int arg2) {
				if(arg0.getText()=="My Library"){
					playlistController.changeToPlaylistOrLibrary("library");
				}else if(arg0.getText()=="Add a Playlist"){
					CreatePlaylist create = new CreatePlaylist();
					create.startDialog();
					createPlaylistName(create.getTextField());
				}else{
					playlistController.changeToPlaylistOrLibrary(arg0.getText());
				}
			}
			
			@Override
			public void sourceListCategoryClicked(SourceListCategory arg0, Button arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		//sourceList.setColorScheme(new SourceListDarkColorScheme());
		
		sourceList.getComponent().setPreferredSize(new Dimension(200,350));
		sourceList.getComponent().setMinimumSize(new Dimension(200,350));
		sourceList.getComponent().setMaximumSize(new Dimension(200,350));
		
		this.panelLeftMenu.add(sourceList.getComponent());
		
		this.libraryModel.addObserver(this);
		
		this.panelGauche.add(this.panelLeftMenu);
	}
	
	private void importAllplaylist() {
		List<Map<String, Object>> playlists = this.playlistModel.GetAllPlaylist();
		for(Map<String, Object> playlist : playlists){
			String name = (String) playlist.get("name");
			listModel.addItemToCategory(new SourceListItem(name), playlist_source_list);
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String argument = (String) arg1;
		if(argument != null && argument.equals("new_playlist")){
			List<Map<String, Object>> playlists = this.playlistModel.GetAllPlaylist();
			String last_name = (String) playlists.get(playlists.size() - 1).get("name");
			listModel.removeItemFromCategoryAtIndex(playlist_source_list, playlist_source_list.getItems().size() - 1);
			listModel.addItemToCategory(new SourceListItem(last_name), playlist_source_list);
			listModel.addItemToCategory(new SourceListItem("Add a Playlist"), playlist_source_list);			
		}
		
	}
	
	public void createPlaylistName(String name){
		if(name != null && !name.isEmpty()){
			this.playlistController.CreatePlaylist(name);
		}
			
	}

	
	
}
