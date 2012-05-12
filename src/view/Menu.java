package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controller.LibraryController;

public class Menu extends JMenuBar{
	LibraryController controller;
	
	public Menu(LibraryController controller) {
		this.controller = controller;
		
		
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
			File file = fileChoose.getSelectedFile();
			controller.importFolder(file.getAbsolutePath());
		}
	}
	
	private void ImportFile(){
		final JFileChooser fileChoose = new JFileChooser("./");
		
		fileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int returnVal = fileChoose.showOpenDialog(new JPanel());
				
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fileChoose.getSelectedFile();
			this.controller.importFile(file.getAbsolutePath());
		}
	}
}
