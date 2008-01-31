/**
 * 
 */
package fr.umlv.IRPhoto.gui.model.album.listener;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface PhotoUpdateListener {

  public void geopPositionUpdated(Photo photo, GeoPosition geo);
}
