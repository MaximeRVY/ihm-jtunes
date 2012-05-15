package view.centre;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
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
	private JPopupMenu popupMenu;
	
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
		this.popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Remove");
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Supprimer");
				
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
		// Ajout du double clic
		this.table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2){
					playController.loadAndPlay(Integer.valueOf((String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6))));
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
				if(e.getButton() == MouseEvent.BUTTON3 && e.isPopupTrigger()){
					
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
				playController.loadAndPlay(Integer.valueOf((String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6))));
				
				// Ajout de la bibliotheque
				playController.changeInPlayList(getJTable());
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
		for(int i=0; i<bibliotheque.size() ; i++){
			String title = (String) bibliotheque.get(i).get("title");
			String artist = (String) bibliotheque.get(i).get("artist");
			String album = (String) bibliotheque.get(i).get("album");
			String genre = (String) bibliotheque.get(i).get("genre");
			String year = (String) bibliotheque.get(i).get("year");
			String duration = (String) bibliotheque.get(i).get("duration");
			Integer id = (Integer) bibliotheque.get(i).get("id");
			this.modelTable.addRow(new String[] {title, artist, album, duration, genre, year, id.toString()});
		}
	}
	
	public List<Map<String, Object>> getJTable(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> file;
		for(int i=0 ; i < modelTable.getRowCount(); i++){
			
		}
		return list;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		List<Map<String,Object>> bibliotheque = this.model.getBibliotheque();
		if(arg==null){
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
