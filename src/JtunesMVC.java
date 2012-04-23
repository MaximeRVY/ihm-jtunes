import javax.swing.JFrame;

import model.LibraryModel;
import model.PlayModel;
import view.haut.Head;
import controller.LibraryController;


public class JtunesMVC {
	private JFrame principalFrame;
	
	public void createFrame(){
		this.principalFrame = new JFrame();
		this.principalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JtunesMVC() {
		createFrame();
		
		LibraryModel libraryModel = new LibraryModel();
		PlayModel playModel = new PlayModel();
		
		LibraryController controllerHead = new LibraryController(libraryModel);
		Head vueHaut = new Head(libraryModel, controllerHead, this.principalFrame);
		
		LibraryController controllerLeft = new LibraryController(libraryModel);
		
		
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
