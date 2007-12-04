package fr.umlv.IRPhoto.GUI.panel.explorer;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.GUI.panel.FactoryPanel;

public class PanelExplorer {

	private final JPanel panel;
	
	private final JPanel panelMenu;
	
	private final JPanel panelTree;
	
	private final JPanel panelLogo;
	
	// PanelTree is a singleton
	private static PanelExplorer instance;

	// Synchronised object
	private static Object o = new Object();

	private PanelExplorer() {
	
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		
		this.panelMenu = FactoryPanelExplorer.createPanelMenu();
		this.panelTree = FactoryPanelExplorer.createPanelTree();
		this.panelLogo = FactoryPanelExplorer.createPanelLogo("lib/img/logo.gif");
		
		this.panel.add(this.panelMenu, BorderLayout.NORTH);
		this.panel.add(this.panelTree, BorderLayout.CENTER);
		this.panel.add(this.panelLogo, BorderLayout.SOUTH);
	}


	public static PanelExplorer getInstance() {

		// multithreading safe
		synchronized (o) {
			if (instance == null) {
				return new PanelExplorer();
			}
		}
		return instance;
	}
	
	
	public JPanel getPanel() {

		return panel;
	}
}
