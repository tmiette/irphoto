package fr.umlv.IRPhoto.GUI.panel;

import javax.swing.JPanel;

import fr.umlv.IRPhoto.GUI.panel.explorer.PanelExplorer;
import fr.umlv.IRPhoto.GUI.panel.explorer.PanelMenu;
import fr.umlv.IRPhoto.GUI.panel.explorer.PanelTree;
import fr.umlv.IRPhoto.GUI.panel.explorer.PanelLogo;


public class FactoryPanel {

	public final static JPanel createPanelExplorer() {

		return PanelExplorer.getInstance().getPanel();
	}
}
