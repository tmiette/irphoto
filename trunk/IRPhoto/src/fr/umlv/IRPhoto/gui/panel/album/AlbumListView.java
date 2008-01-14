package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import fr.umlv.IRPhoto.album.Album;

public class AlbumListView {

	private final JPanel albumListPanel;
	private final JPanel panel;
	private final AlbumListModel model;

	public AlbumListView(AlbumListModel model) {
		this.model = model;

		this.albumListPanel = new JPanel();
		createAlbumListPanel();

		JScrollPane scrollPane = new JScrollPane(this.albumListPanel);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		this.panel = new JPanel();
		this.panel.setPreferredSize(new Dimension(800, 600));
		this.panel.add(scrollPane);
		scrollPane.setPreferredSize(scrollPane.getParent().getPreferredSize());
	}

	private void createAlbumListPanel() {
		this.albumListPanel.setLayout(new BoxLayout(this.albumListPanel, BoxLayout.Y_AXIS));
		this.albumListPanel.setBorder(BorderFactory.createMatteBorder(1, 1,
				1, 1, Color.MAGENTA));
		List<Album> albums = this.model.getAlbums();
		for (Album album : albums) {
			this.albumListPanel.add(new TitleAlbum(album).getPanel());
		}

	}

	public JPanel getPanel() {
		return this.panel;
	}
}
