package fr.umlv.IRPhoto.gui.panel.model.album;

import fr.umlv.IRPhoto.album.Album;

public interface AlbumListener {

  public void albumAdded(Album album);

  public void albumRemoved(Album album);

  //public void albumRenamed(Album album, String newName);

  //public void photoAdded(Album album, Photo photo);
  
  //public void albumSelected(Album album);

}
