package fr.umlv.IRPhoto.gui.panel.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public class PhotoSortModelImpl implements PhotoSortModel {

  private final ArrayList<PhotoSortListener> listeners;

  public PhotoSortModelImpl() {
    this.listeners = new ArrayList<PhotoSortListener>();
  }

  @Override
  public void addPhotoSortListener(PhotoSortListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void sortPhoto(Album album, Comparator<Photo> comparator) {
    this.firePhotoSorted(album.getSortedPhotos(comparator));
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

  protected void firePhotoSorted(List<Photo> photos) {
    for (PhotoSortListener listener : this.listeners) {
      listener.photoSorted(photos);
    }
  }

  /**
   * Returns a photos list which name matches to prefix expression.
   * 
   * @param prefix
   *            photo name to looking for
   * @return photos list matching prefix name
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

}
