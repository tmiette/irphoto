package fr.umlv.IRPhoto.gui.model.album.listener;

import fr.umlv.IRPhoto.album.Photo;

/**
 * This interface defines a listener which is notified each time a photo is
 * selected.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface PhotoSelectionListener {

  /**
   * This method is called when a photo is selected.
   * 
   * @param photo
   *            the selected photo.
   */
  public void photoSelected(Photo photo);

}
