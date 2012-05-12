package view.centre;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.LibraryModel;
import controller.LibraryController;

public class AllFiles implements Observer {

	private JPanel panelCentre;
	private LibraryModel model;
	private LibraryController controller;
	private JPanel allFilesPanel;
	private JTable table;
	private DefaultTableModel modelTable;
	
	public AllFiles(JPanel panelCentre, LibraryModel model, LibraryController controller){
		this.panelCentre = panelCentre;
		this.model = model;
		this.controller = controller;
		createAllFiles();
	}
	
	private void createAllFiles(){
		this.allFilesPanel = new JPanel();
		this.allFilesPanel.setPreferredSize(new Dimension(600,650));
		this.allFilesPanel.setMinimumSize(new Dimension(600,650));
		this.allFilesPanel.setMaximumSize(new Dimension(600,650));
		this.allFilesPanel.setLayout(new BoxLayout(this.allFilesPanel, BoxLayout.Y_AXIS));
		
		//Color
		this.allFilesPanel.setBackground(Color.RED);
		
		modelTable = new DefaultTableModel(new String[] {"Titre","Artiste","Album","Durée"},0){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.table = new JTable(modelTable);
		for(int i=0 ; i<this.table.getColumnCount() ; i++)
			this.table.getColumnModel().getColumn(i).setMinWidth(50);
		this.table.getColumnModel().getColumn(3).setMaxWidth(100);
		JScrollPane scrollPane = new JScrollPane(table);
		this.allFilesPanel.add(scrollPane);
		modelTable.addRow(new String[] {"L'IHM ça pue","Damien Level & Maxime Raverdy","Vivement les vacances","3:30"});
		
		this.model.addObserver(this);
		
		this.panelCentre.add(this.allFilesPanel);	
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
