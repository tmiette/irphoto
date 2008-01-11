package fr.umlv.IRPhoto.gui.panel;

import javax.swing.JPanel;

import fr.umlv.IRPhoto.gui.panel.explorer.PanelExplorer;
import fr.umlv.IRPhoto.gui.panel.explorer.PanelLogo;
import fr.umlv.IRPhoto.gui.panel.explorer.PanelMenu;
import fr.umlv.IRPhoto.gui.panel.explorer.PanelTree;


public class FactoryPanel {

	public final static JPanel createPanelExplorer() {

		return PanelExplorer.getInstance().getPanel();
	}
}
