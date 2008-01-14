package fr.umlv.IRPhoto.gui.panel.album;

import java.util.List;

import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Photo;

public class PhotoListView {

	private final PhotoListModel model;
	private final JPanel panel;

	public PhotoListView(PhotoListModel model) {
		this.model = model;

		this.panel = new JPanel();
		this.addPhotos(this.model.getPhotosSortByName(), this.panel);
	}

	private boolean addPhotos(List<Photo> photos, JPanel panel) {
		for (Photo photo : photos) {
			this.panel.add(PhotoPreview.getPanel(photo));
		}
		return true;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public PhotoListModel getModel() {
		return this.model;
	}
}
