package fr.umlv.IRPhoto.GUI.panel.explorer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class PanelLogo extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Image logo;

	// PanelTree is a singleton
	private static PanelLogo instance;

	// Synchronised object
	private static Object o = new Object();

	public static PanelLogo getInstance(String logoPath) {

		// multithreading safe
		synchronized (o) {
			if (instance == null) {
				return new PanelLogo(logoPath);
			}
		}
		return instance;
	}

	private PanelLogo(String logoPath) {

		this.logo = getToolkit().getImage(logoPath);
		this.setPreferredSize(new Dimension(150, 150));
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponents(g);
		g.drawImage(this.logo, 0, 0, getWidth(), getHeight(), this);
	}

	public JPanel getPanel() {

		return this;
	}

}
