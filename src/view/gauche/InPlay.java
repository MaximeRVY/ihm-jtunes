package view.gauche;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.PlayModel;
import controller.PlayController;

public class InPlay implements Observer{

	private JPanel panelGauche;
	private PlayController controller;
	private PlayModel model;
	private JPanel panelInPlay;
	private JPanel panelLect;
	private DefaultTableModel modelTable;
	private JTable tableInPlay;
	
	public InPlay(JPanel panelGauche, PlayModel playModel, PlayController playController){
		this.panelGauche = panelGauche;
		this.controller = playController;
		this.model = playModel;
		createInPlay();
	}
	
	private void createInPlay(){
		this.panelInPlay = new JPanel();
		
		this.panelLect = new JPanel();
		this.panelLect.setPreferredSize(new Dimension(200,200));
		this.panelLect.setMinimumSize(new Dimension(200,200));
		this.panelLect.setMaximumSize(new Dimension(200,200));
		this.panelLect.setLayout(new BoxLayout(this.panelLect, BoxLayout.Y_AXIS));
		
		// Model de la table non editable
		modelTable = new DefaultTableModel(new String[] {" "," "},0){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		
		};
		tableInPlay = new JTable(modelTable);
		JScrollPane scrollPane = new JScrollPane(tableInPlay);
		modelTable.addRow(new String[] {"Title",""});
		modelTable.addRow(new String[] {"Artist",""});
		
		this.panelLect.add(scrollPane);
		
		this.panelInPlay.add(this.panelLect);
		
		this.model.addObserver(this);
		
		this.panelGauche.add(this.panelInPlay);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if((String) arg == "load" || (String) arg == "change"){
			modelTable.setValueAt(this.controller.getInformationsMp3().get("title"), 0, 1);
			modelTable.setValueAt(this.controller.getInformationsMp3().get("artist"), 1, 1);
		}
	}

}
