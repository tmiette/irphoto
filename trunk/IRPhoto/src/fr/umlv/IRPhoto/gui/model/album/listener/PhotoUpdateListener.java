/**
 * 
 */
package fr.umlv.IRPhoto.gui.model.album.listener;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;

/**
 * This interface defines a listener which is notified each time a photo is
 * updated.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface PhotoUpdateListener {

  /**
   * This method is called when the geo position of a photo is modified.
   * 
   * @param photo
   *            the photo.
   * @param geo
   *            the new geo position.
   */
  public void geopPositionUpdated(Photo photo, GeoPosition geo);

}
