/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.album;

import fr.umlv.IRPhoto.album.Photo;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 *
 */
public interface PhotoUpdatedModel {

  public void addPhotoUpdatedListener(PhotoUpdatedListener photoUpdatedListener);
  
  public void nameUpdated(Photo photo);
  
  public void geopositionUpdated(Photo photo);
}
