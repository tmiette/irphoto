package fr.umlv.IRPhoto.gui.panel.albumslist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;

public class AlbumModelImpl implements AlbumModel {

  private final ArrayList<Album> albums;

  private final ArrayList<AlbumListener> listeners;

  public AlbumModelImpl() {
    this.albums = new ArrayList<Album>();
    this.listeners = new ArrayList<AlbumListener>();
  }

  @Override
  public void addAlbum() {
    Album album = new Album();
    this.albums.add(album);
    this.fireAlbumAdded(album);
  }

  @Override
  public void addAlbumListener(AlbumListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public List<? extends Album> getAlbums() {
    return this.albums;
  }

  @Override
  public void removeAlbum(List<Album> albums) {
    for (Album album : albums) {
      if (this.albums.contains(album)) {
        this.albums.remove(album);
        this.fireAlbumRemoved(album);
      }
    }
  }

  protected void fireAlbumAdded(Album album) {
    for (AlbumListener listener : this.listeners) {
      listener.albumAdded(album);
    }
  }

  protected void fireAlbumRemoved(Album album) {
    for (AlbumListener listener : this.listeners) {
      listener.albumRemoved(album);
    }
  }

  protected void fireAlbumLinked(Album album) {
    for (AlbumListener listener : this.listeners) {
      listener.albumLinked(album);
    }
  }

  @Override
  public void linkAlbum(Album album, File albumFile) {
    album.setFile(albumFile);
    this.fireAlbumLinked(album);
  }

}
