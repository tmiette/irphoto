package fr.umlv.IRPhoto.gui.model.album.listener;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

/**
 * This interface defines a listener which is notified each time an album is
 * updated.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface AlbumUpdateListener {

  /**
   * This method is called when an album is renamed.
   * 
   * @param album
   *            the album.
   * @param newName
   *            the new name.
   */
  public void albumRenamed(Album album, String newName);

  /**
   * This method is called when a photo is added on an album.
   * 
   * @param album
   *            the album.
   * @param photo
   *            the new photo.
   */
  public void photoAdded(Album album, Photo photo);

  /**
   * This method is called when all photos of an album are removed.
   * 
   * @param album
   *            the album.
   */
  public void albumCleared(Album album);

}
