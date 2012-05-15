import helper.ImportExportBibliotheque;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.LibraryModel;
import model.PlayModel;
import model.PlaylistModel;
import view.Menu;
import view.bas.Bottom;
import view.centre.AllFiles;
import view.gauche.InPlay;
import view.gauche.LeftContainer;
import view.gauche.LeftMenu;
import view.haut.Head;
import controller.LibraryController;
import controller.PlayController;
import controller.PlayListController;


public class JtunesMVC {
	private JFrame principalFrame;
	
	public void createFrame(){
		this.principalFrame = new JFrame();
		this.principalFrame.getContentPane().setLayout(new BoxLayout(this.principalFrame.getContentPane(), BoxLayout.Y_AXIS));
		this.principalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JtunesMVC() {
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		createFrame();
		
		LibraryModel libraryModel = new LibraryModel();
		PlayModel playModel = new PlayModel();
		PlaylistModel playlistModel = new PlaylistModel();
		
		ImportExportBibliotheque importBibilio = new ImportExportBibliotheque(libraryModel);
		importBibilio.importAllSongs();
		libraryModel.addObserver(importBibilio);
		LibraryController controllerLib = new LibraryController(libraryModel);
		
		Menu menu = new Menu(controllerLib, this.principalFrame);
		this.principalFrame.setJMenuBar(menu);
			
		
		LibraryController controllerHead = new LibraryController(libraryModel);
		Head vueHaut = new Head(libraryModel, controllerHead, this.principalFrame);
		
		JPanel panelCentre = new JPanel();
		panelCentre.setPreferredSize(new Dimension(800,550));
		panelCentre.setMinimumSize(new Dimension(800,550));
		panelCentre.setMaximumSize(new Dimension(800,550));
		panelCentre.setLayout(new BoxLayout(panelCentre, BoxLayout.X_AXIS));
		
		LibraryController libControllerLeft = new LibraryController(libraryModel);
		PlayController playControllerLeft = new PlayController(playModel, libraryModel);
		PlayListController playlistControllerLeft = new PlayListController(playlistModel, libraryModel);
		LeftContainer leftContainer = new LeftContainer(panelCentre, libControllerLeft, libraryModel, playControllerLeft, playModel, playlistControllerLeft, playlistModel);
		
		InPlay inPlay = leftContainer.getInPlay();
		LeftMenu leftMenu = leftContainer.getLeftContainer();
		
		LibraryController libControllerCenter = new LibraryController(libraryModel);
		PlayController playControllerCenter = new PlayController(playModel, libraryModel);
		AllFiles allFiles = new AllFiles(panelCentre, libraryModel, libControllerCenter, playControllerCenter);
		
		this.principalFrame.getContentPane().add(panelCentre);
		
		PlayController playControllerBottom = new PlayController(playModel, libraryModel);
		Bottom bottom = new Bottom(this.principalFrame, playModel, playControllerBottom);
		
		playControllerBottom.addBottomView(bottom);
		playControllerCenter.addBottomView(bottom);
		playControllerBottom.addAllFilesView(allFiles);
		playControllerCenter.addAllFilesView(allFiles);
		
		this.principalFrame.setSize(new Dimension(800, 600));
		
		this.principalFrame.pack();
		this.principalFrame.setLocationRelativeTo(null);
		this.principalFrame.setVisible(true);
		this.principalFrame.setResizable(false);
	}
	
	public static void main(String[] args) {
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new JtunesMVC();
	      }
	    });
	}
}
