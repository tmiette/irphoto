package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import fr.umlv.IRPhoto.album.Album;

public class AlbumListView {

	private final JPanel albumListPanel;
	private final JPanel panel;
	// private final ArrayList<PhotoPreview> list;
	private final AlbumListModel model;

	public AlbumListView(AlbumListModel model) {
		this.model = model;

		// this.list = new ArrayList<PhotoPreview>();
		// addPhotos();

		this.albumListPanel = new JPanel();
		createPanel();

		JScrollPane scrollPane = new JScrollPane(this.albumListPanel);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		this.panel = new JPanel();
		this.panel.add(scrollPane);
	}

	// private void addPhotos() {
	// for (int i = 0; i < 10; i++) {
	// this.list.add(new PhotoPreview());
	// }
	// }

	private void createPanel() {
		this.albumListPanel.setBorder(BorderFactory.createMatteBorder(10, 10,
				10, 10, Color.blue));
		List<Album> albums = this.model.getAlbums();
		for (Album album : albums) {
			this.addAlbum(album, this.albumListPanel);
		}

	}

	private void addAlbum(Album album, Container container) {
		// TODO Auto-generated method stub

	}

	public JPanel getPanel() {
		return this.panel;
	}
}
