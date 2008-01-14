package fr.umlv.IRPhoto.gui.panel.albumslist;

import java.io.File;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;

public interface AlbumModel {

  public List<? extends Album> getAlbums();

  public void addAlbum();

  public void removeAlbum(Album album);
  
  public void linkAlbum(Album album, File albumFile);

  public void addAlbumListener(AlbumListener listener);
}
