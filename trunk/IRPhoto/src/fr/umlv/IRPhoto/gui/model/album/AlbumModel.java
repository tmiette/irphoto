package fr.umlv.IRPhoto.gui.model.album;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.model.album.listener.PhotoSelectionListener;
import fr.umlv.IRPhoto.gui.model.album.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.gui.model.photo.PhotoSortModel;

/**
 * This interface defines the main model of the application. This model manages
 * the addition and the suppression of albums, the addition of photos in a
 * specified album and the modifications on photos.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface AlbumModel {

  /**
   * Adds a new album to this model.
   */
  public void addAlbum();

  /**
   * Adds a listener that's notified each time an album is added or removed.
   * 
   * @param listener
   *            the new listener.
   */
  public void addAlbumListener(AlbumListener listener);

  /**
   * Adds a listener that's notified each time an album is selected.
   * 
   * @param listener
   *            the new listener.
   */
  public void addAlbumSelectionListener(AlbumSelectionListener listener);

  /**
   * Adds a listener that's notified each time an album is renamed or a photo is
   * added in an album.
   * 
   * @param listener
   *            the new listener.
   */
  public void addAlbumUpdateListener(AlbumUpdateListener listener);

  /**
   * Adds a listener that's notified each time a photo is selected.
   * 
   * @param listener
   *            the new listener.
   */
  public void addPhotoSelectionListener(PhotoSelectionListener listener);

  /**
   * Adds a listener that's notified each time a photo is updated.
   * 
   * @param listener
   *            the new listener.
   */
  public void addPhotoUpdatedListener(PhotoUpdateListener photoUpdatedListener);

  /**
   * Adds an album to this model from a saved file.
   * 
   * @param album
   *            the new album.
   */
  public void addSavedAlbum(Album album);

  /**
   * Creates and returns a new default photo sort model.
   * 
   * @return the photo sort model.
   */
  public PhotoSortModel createNewPhotoSortModel();

  /**
   * Returns the list of albums.
   * 
   * @return the list of albums.
   */
  public List<? extends Album> getAlbums();

  /**
   * Returns the album currently selected.
   * 
   * @return the album currently selected.
   */
  public Album getSelectedAlbum();

  /**
   * Returns the photo currently selected.
   * 
   * @return the photo currently selected.
   */
  public Photo getSelectedPhoto();

  /**
   * Returns the list of album sorted.
   * 
   * @param comparator
   *            the comparator to use for the sort.
   * @return the list of albums sorted.
   */
  public List<? extends Album> getSortedAlbums(Comparator<Album> comparator);

  /**
   * Changes the root directory of an album.
   * 
   * @param album
   *            the album.
   * @param albumFile
   *            the new root directory.
   */
  public void linkAlbum(Album album, File albumFile);

  /**
   * Rename an album.
   * 
   * @param album
   *            the album.
   * @param name
   *            the new name.
   */
  public void nameAlbum(Album album, String name);

  /**
   * Removes albums from this model.
   * 
   * @param albums
   *            the list of albums to remove.
   */
  public void removeAlbum(List<Album> albums);

  /**
   * Changes the current album.
   * 
   * @param album
   *            the new current album.
   */
  public void selectAlbum(Album album);

  /**
   * Changes the current photo.
   * 
   * @param photo
   *            the new photo album.
   */
  public void selectPhoto(Photo photo);

  /**
   * Modifies the geo position of a photo.
   * 
   * @param photo
   *            the photo.
   * @param geo
   *            the new geo position.
   */
  public void updateGeoPosition(Photo photo, GeoPosition geo);
}
