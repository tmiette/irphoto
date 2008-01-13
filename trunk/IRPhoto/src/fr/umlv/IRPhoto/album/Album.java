package fr.umlv.IRPhoto.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {

	private static int albumsCreated;
	private final ArrayList<Photo> photos;
	private String name;

	public Album() {
		this.name = "Album" + albumsCreated++;
		this.photos = new ArrayList<Photo>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Photo> getPhotos() {
		return Collections.unmodifiableList(this.photos);
	}

	public boolean addPhoto(Photo photo) {
		return this.photos.add(photo);
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
