package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.features.PhotoSelectionModel;

public class AlbumListView implements ContainerInitializer {

	private final JPanel panel;
	private final AlbumModel model;
	private final GridBagConstraints constraints;
	private final ArrayList<TitleAlbum> titleAlbums;
	private final JPanel endPanel;

	public AlbumListView(AlbumModel albumModel,
			PhotoSelectionModel selectionModel) {
		this.titleAlbums = new ArrayList<TitleAlbum>();
		this.model = albumModel;
		this.model.addAlbumListener(new AlbumListener() {

			@Override
			public void albumAdded(Album album) {
				AlbumListView.this.addAlbum(album);
			}

			@Override
			public void albumRemoved(Album album) {
				AlbumListView.this.removeAlbum(album);
			}

			@Override
			public void albumUpdated(Album album) {
				AlbumListView.this.updateAlbum(album);
				
			}
			
			@Override
			public void photoAdded(Album album, Photo photo) {
			  // TODO Auto-generated method stub
			  
			}

		});

		this.constraints = new GridBagConstraints();
		this.constraints.fill = GridBagConstraints.HORIZONTAL;
		this.constraints.anchor = GridBagConstraints.PAGE_START;
		this.constraints.gridy = 0;
		this.constraints.weightx = 0.5;
		this.constraints.gridx = 0;

		this.endPanel = new JPanel();

		this.panel = createAlbumListPanel();
	}
	
	/**
	 * Updates TitleAlblum corresponding to album
	 * @param album
	 */
	private void updateAlbum(Album album) {
		for (int i = 0; i < this.titleAlbums.size(); i++) {

			if (this.titleAlbums.get(i).getAlbum().equals(album)) {
				this.titleAlbums.get(i).refreshView();
				return;
			}
		}
	}

	private void addAlbum(Album album) {
		this.constraints.gridy++;

		TitleAlbum ta = new TitleAlbum(album);
		this.titleAlbums.add(ta);
		JPanel newTitleAlbumPanel = ta.getPanel();
		this.panel.add(newTitleAlbumPanel, this.constraints);

		this.addEndPanel(this.panel);
		this.panel.revalidate();
	}

	private void removeAlbum(Album album) {

		for (int i = 0; i < this.titleAlbums.size(); i++) {

			if (this.titleAlbums.get(i).getAlbum().equals(album)) {
				this.panel.remove(this.titleAlbums.get(i).getPanel());
				this.titleAlbums.remove(i);
				this.addEndPanel(this.panel);
				// FIXME
				this.panel.revalidate();
				this.panel.repaint();
				return;
			}
		}
	}
	
	private JPanel createAlbumListPanel() {
		JPanel panel = new JPanel(null);
		panel.setLayout(new GridBagLayout());

		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				Color.MAGENTA));

		List<? extends Album> albums = this.model.getAlbums();
		for (Album album : albums) {
			this.constraints.gridy++;
			TitleAlbum ta = new TitleAlbum(album);
			panel.add(ta.getPanel(), this.constraints);
			this.titleAlbums.add(ta);
		}

		this.addEndPanel(panel);
		return panel;
	}

	private void addEndPanel(JPanel panel) {
		this.constraints.weighty = 1;
		panel.add(this.endPanel, this.constraints);
		this.constraints.weighty = 0;
	}

	@Override
	public JComponent initialize() {
		return this.panel;
	}

}
