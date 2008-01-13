package fr.umlv.IRPhoto.gui.panel.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;

public class AlbumListModel {
	
	private final ArrayList<Album> albums;

	public AlbumListModel() {
		this.albums = new ArrayList<Album>();
	}
	
	public boolean addAlbum(Album album) {
		return this.albums.add(album);
	}

	public boolean removeAlbum(Album album) {
		return this.albums.remove(album);
	}
	
	public List<Album> getAlbums() {
		return Collections.unmodifiableList(this.albums);
	}
}
