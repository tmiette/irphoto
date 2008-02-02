package fr.umlv.IRPhoto.gui.model.photo;

import java.util.List;

import fr.umlv.IRPhoto.album.Photo;

/**
 * This interface defines a listener for a photo sort model.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface PhotoSortListener {

  /**
   * This method is called when a list of photos is sorted or matched.
   * 
   * @param photos
   *            the list of photos sorted or matched.
   */
  public void photoSorted(List<Photo> photos);

}
