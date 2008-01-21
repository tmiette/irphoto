/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.album;

import java.util.ArrayList;

import fr.umlv.IRPhoto.album.Photo;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 *
 */
public class PhotoUpdatedModelImpl implements PhotoUpdatedModel {

  private final ArrayList<PhotoUpdatedListener> listeners;
  
  /**
   * 
   */
  public PhotoUpdatedModelImpl() {
    this.listeners = new ArrayList<PhotoUpdatedListener>();
  }
  public void addPhotoUpdatedListener(PhotoUpdatedListener photoUpdatedListener) {
    this.listeners.add(photoUpdatedListener);
  }
  
  public void nameUpdated(Photo photo) {
    this.fireNameUpdated(photo);
  }
  
  public void geopositionUpdated(Photo photo) {
    this.fireGeopositionUpdated(photo);
  }
  
  protected void fireNameUpdated(Photo photo) {
    for (PhotoUpdatedListener listener : this.listeners) {
      listener.nameUpdated(photo);
    }
  }
  
  protected void fireGeopositionUpdated(Photo photo) {
    for (PhotoUpdatedListener listener : this.listeners) {
      listener.geoppositionUpdated(photo);
    }
  }
}
