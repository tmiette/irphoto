package fr.umlv.IRPhoto.gui.panel.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public class PhotoListModel {

	private final ArrayList<Photo> photos;

	public PhotoListModel(Album album) {
		this.photos = new ArrayList<Photo>();
		this.photos.addAll(album.getPhotos());
	}

	public List<Photo> getPhotosSortByName() {

		Collections.sort(this.photos, Photo.NAME_ORDER);
		return Collections.unmodifiableList(this.photos);
	}

	public List<Photo> getPhotosSortByType() {

		Collections.sort(this.photos, Photo.TYPE_ORDER);
		return Collections.unmodifiableList(this.photos);
	}

	public List<Photo> getPhotosSortByDate() {

		Collections.sort(this.photos, Photo.DATE_ORDER);
		return Collections.unmodifiableList(this.photos);
	}

	public List<Photo> getPhotos() {
		return Collections.unmodifiableList(this.photos);
	}

}
