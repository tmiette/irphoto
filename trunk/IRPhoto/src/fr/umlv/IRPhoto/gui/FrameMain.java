package fr.umlv.IRPhoto.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import fr.umlv.IRPhoto.gui.panel.FactoryPanel;

public class FrameMain {

	private final JFrame jf;

	private final JPanel panelExplorer;

	public FrameMain() {

		this.jf = new JFrame();
		this.jf.setTitle("IRPhoto");
		this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panelExplorer = FactoryPanel.createPanelExplorer();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				this.panelExplorer, null);
		this.jf.getContentPane().add(splitPane);

		this.jf.pack();
		this.jf.setVisible(true);
	}
}
