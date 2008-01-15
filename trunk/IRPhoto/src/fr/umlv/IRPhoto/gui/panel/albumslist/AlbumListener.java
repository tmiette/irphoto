package fr.umlv.IRPhoto.gui.panel.albumslist;

import fr.umlv.IRPhoto.album.Album;

public interface AlbumListener {

  public void albumAdded(Album album);

  public void albumRemoved(Album album);
  
  public void albumUpdated(Album album);

}
