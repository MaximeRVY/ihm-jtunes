package view.centre;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.table.DefaultTableModel;

import model.LibraryModel;
import model.PlaylistModel;
import controller.LibraryController;
import controller.PlayController;
import controller.PlayListController;

public class AllFiles implements Observer {

	private JPanel panelCentre;
	private LibraryModel model;
	private LibraryController libraryController;
	private PlayController playController;
	private JPanel allFilesPanel;
	private JTable table;
	private DefaultTableModel modelTable;
	private JPopupMenu popupMenu;
	private PlayListController playlistController;
	private PlaylistModel playlistModel;
	private Boolean haveSong;
	private String filter = "";
	
	public AllFiles(JPanel panelCentre, LibraryModel model, LibraryController libraryController, PlayController playController, PlaylistModel playlistModel, PlayListController playlistControllerCenter){
		this.panelCentre = panelCentre;
		this.model = model;
		this.libraryController = libraryController;
		this.playController = playController;
		this.playlistModel = playlistModel;
		this.playlistController = playlistControllerCenter;
		this.haveSong = false;
		createAllFiles();
		
		this.playlistModel.addObserver(this);
	}
	
	private void createAllFiles(){
		this.allFilesPanel = new JPanel();
		this.allFilesPanel.setPreferredSize(new Dimension(600,650));
		this.allFilesPanel.setMinimumSize(new Dimension(600,650));
		this.allFilesPanel.setMaximumSize(new Dimension(600,650));
		this.allFilesPanel.setLayout(new BoxLayout(this.allFilesPanel, BoxLayout.Y_AXIS));
		
		// Model de la table non editable
		this.modelTable = new DefaultTableModel(new String[] {"Title","Artist","Album","Time","Genre","Year","NB","id","pathname"},0){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.table = new JTable(modelTable);
		// Largeur des colonnes
		for(int i=0 ; i<this.table.getColumnCount()-1 ; i++)
			this.table.getColumnModel().getColumn(i).setMinWidth(50);
		this.table.getColumnModel().getColumn(3).setMaxWidth(100);
		this.table.getColumnModel().getColumn(5).setMaxWidth(50);
		this.table.getColumnModel().getColumn(6).setMaxWidth(50);
		this.table.getColumnModel().getColumn(7).setPreferredWidth(0);
		this.table.getColumnModel().getColumn(7).setMinWidth(0);
		this.table.getColumnModel().getColumn(7).setMaxWidth(0);
		this.table.getColumnModel().getColumn(8).setPreferredWidth(0);
		this.table.getColumnModel().getColumn(8).setMinWidth(0);
		this.table.getColumnModel().getColumn(8).setMaxWidth(0);
		// Tri automatique sur la colonne
		this.table.setAutoCreateRowSorter(true);
		this.popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Remove");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Supprimer");
				model.removeFile((Integer.valueOf((String) modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7))));
				modelTable.removeRow(table.convertRowIndexToModel(table.getSelectedRow()));
				
			}
		});
		this.popupMenu.add(menuItem);
		menuItem = new JMenuItem("Add to the playlist...");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Add to the playlist...");
				
			}
		});
		this.popupMenu.add(menuItem);
		this.table.setDropMode(DropMode.INSERT_ROWS);
		
		this.table.setTransferHandler(new TransferHandler() {
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
		// Ajout du double clic
		this.table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(haveSong && arg0.getClickCount() == 2){
					playController.loadAndPlay(Integer.valueOf((String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7))));
					System.out.println((String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7)));
					// Ajout de la bibliotheque
					playController.changeInPlayList(getJTable());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(haveSong && e.getButton() == MouseEvent.BUTTON3 && e.isPopupTrigger()){
					
					Point p = new Point(e.getX(), e.getY());
				 	int selectedRow = table.rowAtPoint(p);
				 	table.setRowSelectionInterval(selectedRow, selectedRow);
				 	popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
					
			}
		});
		
		
		// Entrer = play
		this.table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter-play");
		this.table.getActionMap().put("enter-play", new AbstractAction(){
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e){
				if(haveSong){
					playController.loadAndPlay(Integer.valueOf((String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7))));
					
					// Ajout de la bibliotheque
					playController.changeInPlayList(getJTable());
				}
				
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		this.allFilesPanel.add(scrollPane);
		
		this.model.addObserver(this);
		
		this.panelCentre.add(this.allFilesPanel);
		importBibliotheque();
	}
	
	public void importBibliotheque(){
		List<Map<String,Object>> bibliotheque = this.model.getBibliotheque();
		Integer sizeBibliotheque = bibliotheque.size();
		if(sizeBibliotheque > 0){
			haveSong = true;
			for(int i=0; i< sizeBibliotheque; i++){
				addRowJTable(bibliotheque, i);
			}
		}else{
			for(int i=0; i < 32; i++){
				this.modelTable.addRow(new String[] {"", "", "", "", "", "", "", "", ""});
			}
		}
		
		
	}
	
	public List<Map<String, Object>> getJTable(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> file;
		for(int i=0 ; i < modelTable.getRowCount(); i++){
			file = new HashMap<String, Object>();
			file.put("id", Integer.parseInt((String) (modelTable.getValueAt(table.convertRowIndexToModel(i), 7))));
			file.put("pathname", (String) (modelTable.getValueAt(table.convertRowIndexToModel(i), 8)));
			list.add(file);
		}
		return list;
	}
	
	public void addRowJTable(List<Map<String,Object>> bibliotheque, int i){
		String title = (String) bibliotheque.get(i).get("title");
		String artist = (String) bibliotheque.get(i).get("artist");
		String album = (String) bibliotheque.get(i).get("album");
		String genre = (String) bibliotheque.get(i).get("genre");
		String year = (String) bibliotheque.get(i).get("year");
		String duration = (String) bibliotheque.get(i).get("duration");
		Integer nb = (Integer) bibliotheque.get(i).get("nb");
		Integer id = (Integer) bibliotheque.get(i).get("id");
		String pathname = (String) bibliotheque.get(i).get("pathname");
		if(title.toLowerCase().contains(filter) || artist.toLowerCase().contains(filter) || album.toLowerCase().contains(filter) || genre.toLowerCase().contains(filter))
			this.modelTable.addRow(new String[] {title, artist, album, duration, genre, year, nb.toString(), id.toString(), pathname});
	}
	
	public void refresh(List<Map<String,Object>> bibliotheque){
		for(int i=this.table.getRowCount()-1 ; i>=0 ; i--)
			this.modelTable.removeRow(i);
		for(int i=0; i<bibliotheque.size() ; i++){
			addRowJTable(bibliotheque, i);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		List<Map<String,Object>> bibliotheque = this.model.getBibliotheque();
		if(arg==null){
			if(!haveSong){
				for(int i=this.table.getRowCount()-1 ; i>=0 ; i--)
					this.modelTable.removeRow(i);
				haveSong = true;
			}
			// Ajout du dernier element de la bibliotheque
			addRowJTable(bibliotheque, bibliotheque.size()-1);
		}else if(arg.equals("view_library")){
			for(int i=this.table.getRowCount()-1 ; i>=0 ; i--)
				this.modelTable.removeRow(i);
			importBibliotheque();
			
		}else if(((String) arg).startsWith("view_playlist:")){
			String namePlaylist = ((String) arg).split("view_playlist:")[1];
			List<Map<String, Object>> songs = this.playlistModel.findByName(namePlaylist);
			if(songs != null && songs.size() > 0){
				haveSong = true;
				for(int i=this.table.getRowCount()-1 ; i>=0 ; i--)
					this.modelTable.removeRow(i);
				for(Map<String, Object> song : songs){
					String title = (String) song.get("title");
					String artist = (String) song.get("artist");
					String album = (String) song.get("album");
					String genre = (String) song.get("genre");
					String year = (String) song.get("year");
					String duration = (String) song.get("duration");
					Integer nb = (Integer) song.get("nb");
					Integer id = (Integer) song.get("id");
					String pathname = (String) song.get("pathname");
					this.modelTable.addRow(new String[] {title, artist, album, duration, genre, year, nb.toString(), id.toString(), pathname});
				}
			}else{
				for(int i=this.table.getRowCount()-1 ; i>=0 ; i--)
					this.modelTable.removeRow(i);
				for(int i=0; i < 32; i++){
					this.modelTable.addRow(new String[] {"", "", "", "", "", "", "", "", ""});
				}
			}
		}else{
			if(haveSong){
				if(((String) arg).startsWith("filter:") && ((String) arg).split("filter:").length>1)
					this.filter = ((String) arg).split("filter:")[1];
				else
					this.filter = "";
				refresh(bibliotheque);
			}
		}
	}

}
