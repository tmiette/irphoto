package fr.umlv.IRPhoto.gui.model.album.listener;

import fr.umlv.IRPhoto.album.Album;

/**
 * This interface defines a listener which is notified each time an album is
 * added or removed.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface AlbumListener {

  /**
   * This method is called when an album is added.
   * 
   * @param album
   *            the new album.
   */
  public void albumAdded(Album album);

  /**
   * This method is called when an album is removed.
   * 
   * @param album
   *            the removed album.
   */
  public void albumRemoved(Album album);

}
