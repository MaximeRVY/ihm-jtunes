package view.gauche;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import view.CreatePlaylist;

import model.LibraryModel;

import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListDarkColorScheme;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import com.explodingpixels.macwidgets.SourceListClickListener.Button;

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
		
		this.panelLeftMenu.setLayout(new BoxLayout(this.panelLeftMenu, BoxLayout.Y_AXIS));
		
		SourceListModel listModel = new SourceListModel();
		SourceListCategory Music = new SourceListCategory("Music");
		listModel.addCategory(Music);
		listModel.addItemToCategory(new SourceListItem("My Library"), Music);
		SourceListCategory Playlist = new SourceListCategory("Playlist");
		listModel.addCategory(Playlist);
		listModel.addItemToCategory(new SourceListItem("Add a Playlist"), Playlist);
		SourceList sourceList = new SourceList(listModel);
		sourceList.addSourceListClickListener(new SourceListClickListener() {
			
			@Override
			public void sourceListItemClicked(SourceListItem arg0, Button arg1, int arg2) {
				if(arg0.getText()=="My Library"){
					System.out.println("Mettre la bibliotheque dans la JTable");
				}else if(arg0.getText()=="Add a Playlist"){
					//System.out.println("Faire une Frame/Alert avec textfield pour ajouter");
					CreatePlaylist create = new CreatePlaylist();
					create.startDialog();
					System.out.println("Créer une playlist avec "+create.getTextField());
				}else{
					System.out.println("Mettre la playlist dans la JTable");
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
		
		this.model.addObserver(this);
		
		this.panelGauche.add(this.panelLeftMenu);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
	
}
