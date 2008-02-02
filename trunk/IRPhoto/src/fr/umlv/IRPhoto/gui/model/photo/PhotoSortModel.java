package fr.umlv.IRPhoto.gui.model.photo;

import java.util.Comparator;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

/**
 * 
 * This interface defines a model which can sort a collection of photos with a
 * specific comparator or a matching expression. This model is used by each
 * photo collections to perform sorts of this collection.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface PhotoSortModel {

  /**
   * Adds a listener that's notified each time a change to the data model
   * occurs.
   * 
   * @param listener
   *            the new listener.
   */
  public void addPhotoSortListener(PhotoSortListener listener);

  /**
   * Selects a all photos of an album which names match to a prefix.
   * 
   * @param album
   *            the album.
   * @param prefix
   *            the matching prefix.
   */
  public void matchPhoto(Album album, String prefix);

  /**
   * Sorts the list of photos of an album with the specified comparator.
   * 
   * @param album
   *            the album.
   * @param comparator
   *            the comparator.
   */
  public void sortPhoto(Album album, Comparator<Photo> comparator);

}
