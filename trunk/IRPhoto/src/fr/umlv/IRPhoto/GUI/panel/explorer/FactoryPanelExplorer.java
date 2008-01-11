package fr.umlv.IRPhoto.GUI.panel.explorer;

import javax.swing.JPanel;

public class FactoryPanelExplorer {

	public final static JPanel createPanelTree() {

		return PanelTree.getInstance().getPanel();
	}

	public final static JPanel createPanelLogo(String logoPath) {

		return PanelLogo.getInstance(logoPath).getPanel();
	}

	public final static JPanel createPanelMenu() {

		return PanelMenu.getInstance().getPanel();
	}
}
