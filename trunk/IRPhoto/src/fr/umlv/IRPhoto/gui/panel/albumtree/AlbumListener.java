package fr.umlv.IRPhoto.gui.panel.albumtree;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public interface AlbumListener {

  public void albumAdded(Album album);

  public void albumRemoved(Album album);
  
  public void albumUpdated(Album album);
  
  public void photoAdded(Album album, Photo photo);

}
