package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controller.LibraryController;

public class Menu extends JMenuBar{
	LibraryController controller;
	JFrame principalFrame;
	public Menu(LibraryController controller, JFrame frame) {
		this.controller = controller;
		this.principalFrame = frame;
		
		JMenu menu = new JMenu("File");
		this.add(menu);
		
		JMenuItem menuItemImport = new JMenuItem("Import File");
		
		menuItemImport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImportFile();
				
			}
		});
		menu.add(menuItemImport);
		
		menuItemImport = new JMenuItem("Import Folder");
		
		menuItemImport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImportFolder();
				
			}
		});
		menu.add(menuItemImport);
		
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(menuItem);
		
	}
	
	private void ImportFolder(){
		final JFileChooser fileChoose = new JFileChooser("./");
		fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int returnVal = fileChoose.showOpenDialog(new JPanel());
		if (returnVal == JFileChooser.APPROVE_OPTION){
			this.principalFrame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			File file = fileChoose.getSelectedFile();
			controller.importFolder(file.getAbsolutePath());
			this.principalFrame.getContentPane().setCursor(Cursor.getDefaultCursor());
		}
	}
	
	private void ImportFile(){
		final JFileChooser fileChoose = new JFileChooser("./");
		
		fileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int returnVal = fileChoose.showOpenDialog(new JPanel());
				
		if(returnVal == JFileChooser.APPROVE_OPTION){
			this.principalFrame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			File file = fileChoose.getSelectedFile();
			this.controller.importFile(file.getAbsolutePath());
			this.principalFrame.getContentPane().setCursor(Cursor.getDefaultCursor());
		}
	}
}
