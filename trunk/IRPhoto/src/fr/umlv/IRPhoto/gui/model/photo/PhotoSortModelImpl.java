package fr.umlv.IRPhoto.gui.model.photo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

/**
 * This class is a simple implementation of an photo sort model.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoSortModelImpl implements PhotoSortModel {

  /**
   * Returns a photos list which names match to prefix expression.
   * 
   * @param prefix
   *            the prefix expression.
   * @return the list of photos.
   */
  private static List<Photo> getPhotoListMatching(Album album, String prefix) {
    ArrayList<Photo> list = new ArrayList<Photo>();
    for (Photo photo : album.getPhotos()) {
      String name = photo.getName().toLowerCase();
      if (name.startsWith(prefix.toLowerCase())) {
        list.add(photo);
      }
    }
    return list;
  }

  // listeners
  private final ArrayList<PhotoSortListener> listeners;

  /**
   * Default constructor.
   */
  public PhotoSortModelImpl() {
    this.listeners = new ArrayList<PhotoSortListener>();
  }

  @Override
  public void addPhotoSortListener(PhotoSortListener listener) {
    this.listeners.add(listener);
  }

  /**
   * This method notifies each listener when a list of photos is sorted or
   * matched.
   * 
   * @param photos
   *            the list of photos sorted or matched.
   */
  protected void firePhotoSorted(List<Photo> photos) {
    for (PhotoSortListener listener : this.listeners) {
      listener.photoSorted(photos);
    }
  }

  @Override
  public void matchPhoto(Album album, String prefix) {
    if (prefix.matches("^ *")) {
      // If nothing or spaces entered default photos list is displayed
      this.firePhotoSorted(album.getPhotos());
    } else {
      // Displaying photos matching string expression whit leading and
      // trailing whitespace omitted
      this.firePhotoSorted(getPhotoListMatching(album, prefix.trim()));
    }
  }

  @Override
  public void sortPhoto(Album album, Comparator<Photo> comparator) {
    this.firePhotoSorted(album.getSortedPhotos(comparator));
  }

}
