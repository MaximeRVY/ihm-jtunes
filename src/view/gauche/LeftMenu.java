package view.gauche;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.LibraryModel;

import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListDarkColorScheme;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;

import controller.LibraryController;

public class LeftMenu implements Observer {

	private JPanel panelGauche;
	private LibraryModel model;
	private LibraryController controller;
	private JPanel panelLeftMenu;
	
	public LeftMenu(JPanel panelGauche, LibraryModel model, LibraryController controller){
		this.panelGauche = panelGauche;
		this.model = model;
		this.controller = controller;
		createLeftMenu();
	}
	
	private void createLeftMenu(){
		this.panelLeftMenu = new JPanel();
		this.panelLeftMenu.setPreferredSize(new Dimension(200,350));
		this.panelLeftMenu.setMinimumSize(new Dimension(200,350));
		this.panelLeftMenu.setMaximumSize(new Dimension(200,350));
		this.panelLeftMenu.setLayout(new BoxLayout(this.panelLeftMenu, BoxLayout.Y_AXIS));
		
		// Color
		this.panelLeftMenu.setBackground(Color.YELLOW);
		
		SourceListModel listModel = new SourceListModel();
		SourceListCategory category = new SourceListCategory("Category");
		listModel.addCategory(category);
		listModel.addItemToCategory(new SourceListItem("Item"), category);
		SourceList sourceList = new SourceList(listModel);
		sourceList.setColorScheme(new SourceListDarkColorScheme());
		
		this.panelLeftMenu.add(sourceList.getComponent());
		
		this.model.addObserver(this);
		
		this.panelGauche.add(this.panelLeftMenu);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
	
}
