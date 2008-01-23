package fr.umlv.IRPhoto.gui.panel.model.album;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public interface AlbumUpdateListener {

  public void albumRenamed(Album album, String newName);

  public void photoAdded(Album album, Photo photo);
  
}
