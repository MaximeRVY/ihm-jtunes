package view.centre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.LibraryModel;
import controller.LibraryController;
import controller.PlayController;

public class AllFiles implements Observer {

	private JPanel panelCentre;
	private LibraryModel model;
	private LibraryController libraryController;
	private PlayController playController;
	private JPanel allFilesPanel;
	private JTable table;
	private DefaultTableModel modelTable;
	
	public AllFiles(JPanel panelCentre, LibraryModel model, LibraryController libraryController, PlayController playController){
		this.panelCentre = panelCentre;
		this.model = model;
		this.libraryController = libraryController;
		this.playController = playController;
		createAllFiles();
	}
	
	private void createAllFiles(){
		this.allFilesPanel = new JPanel();
		this.allFilesPanel.setPreferredSize(new Dimension(600,650));
		this.allFilesPanel.setMinimumSize(new Dimension(600,650));
		this.allFilesPanel.setMaximumSize(new Dimension(600,650));
		this.allFilesPanel.setLayout(new BoxLayout(this.allFilesPanel, BoxLayout.Y_AXIS));
		
		// Model de la table non editable
		this.modelTable = new DefaultTableModel(new String[] {"Title","Artist","Album","Time","Genre","Year","id"},0){
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
		this.table.getColumnModel().getColumn(5).setMaxWidth(100);
		this.table.getColumnModel().getColumn(6).setPreferredWidth(0);
		this.table.getColumnModel().getColumn(6).setMinWidth(0);
		this.table.getColumnModel().getColumn(6).setMaxWidth(0);
		// Tri automatique sur la colonne
		this.table.setAutoCreateRowSorter(true);
		// Ajout du double clic
		this.table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2)
					playController.loadAndPlay(Integer.valueOf((String) (modelTable.getValueAt(table.getSelectedRow(), 6))));
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
				// TODO Auto-generated method stub
				
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		this.allFilesPanel.add(scrollPane);
		
		this.model.addObserver(this);
		
		this.panelCentre.add(this.allFilesPanel);	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		List<Map<String,Object>> bibliotheque = this.model.getBibliotheque();
		if(arg==null){
			// Ajout de toute la bibliotheque
			/*for(int i=0; i<bibliotheque.size() ; i++)
				this.modelTable.addRow(new String[] {(String) bibliotheque.get(i).get("title")});*/
			// Ajout du dernier element de la bibliotheque
			String title = (String) bibliotheque.get(bibliotheque.size()-1).get("title");
			String artist = (String) bibliotheque.get(bibliotheque.size()-1).get("artist");
			String album = (String) bibliotheque.get(bibliotheque.size()-1).get("album");
			String genre = (String) bibliotheque.get(bibliotheque.size()-1).get("genre");
			String year = (String) bibliotheque.get(bibliotheque.size()-1).get("year");
			String duration = (String) bibliotheque.get(bibliotheque.size()-1).get("duration");
			Integer id = (Integer) bibliotheque.get(bibliotheque.size()-1).get("id");
			this.modelTable.addRow(new String[] {title, artist, album, duration, genre, year, id.toString()});
		}else{
			for(int i=this.table.getRowCount()-1 ; i>=0 ; i--)
				this.modelTable.removeRow(i);
			String filter = ((String) arg).toLowerCase();
			for(int i=0; i<bibliotheque.size() ; i++){
				String title = (String) bibliotheque.get(i).get("title");
				String artist = (String) bibliotheque.get(i).get("artist");
				String album = (String) bibliotheque.get(i).get("album");
				String genre = (String) bibliotheque.get(i).get("genre");
				String year = (String) bibliotheque.get(i).get("year");
				String duration = (String) bibliotheque.get(i).get("duration");
				Integer id = (Integer) bibliotheque.get(i).get("id");
				if(title.toLowerCase().contains(filter) || artist.toLowerCase().contains(filter) || album.toLowerCase().contains(filter) || genre.toLowerCase().contains(filter))
					this.modelTable.addRow(new String[] {title, artist, album, duration, genre, year, id.toString()});
			}
		}
	}

}
