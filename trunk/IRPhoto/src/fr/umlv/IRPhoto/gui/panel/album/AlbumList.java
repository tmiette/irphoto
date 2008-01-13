package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class AlbumList {

	private final JScrollPane scrollPane;
	private final JPanel scrollablePanel;
	private final JPanel panel;
	private final ArrayList<PhotoPreview> list;

	// TODO constructor with photo collection in argument
	public AlbumList() {

		this.list = new ArrayList<PhotoPreview>();
		addPhotos();

		this.scrollablePanel = new JPanel();
		createPanel();

		this.scrollPane = new JScrollPane(this.scrollablePanel);
		this.scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.panel = new JPanel();
		this.panel.add(this.scrollPane);
	}

	private void addPhotos() {
		for (int i = 0; i < 10; i++) {
			this.list.add(new PhotoPreview());
		}
	}

	private void createPanel() {
		this.scrollablePanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
				Color.blue));
		for (PhotoPreview photo : this.list) {
			this.scrollablePanel.add(photo.getPanel());
		}

	}

	public JPanel getPanel() {
		return this.panel;
	}
}
