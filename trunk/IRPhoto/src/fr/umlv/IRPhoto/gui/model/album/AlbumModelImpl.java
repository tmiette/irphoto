package fr.umlv.IRPhoto.gui.model.album;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.model.album.listener.PhotoSelectionListener;
import fr.umlv.IRPhoto.gui.model.album.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.gui.model.photo.PhotoSortModel;
import fr.umlv.IRPhoto.gui.model.photo.PhotoSortModelImpl;

/**
 * This class is a simple implementation of an album model.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class AlbumModelImpl implements AlbumModel {

  // number of threads used to search photos in directories
  private static final int MAX_THREADS = 2;

  // album listeners
  private final ArrayList<AlbumListener> albumListeners;

  // list of albums
  private final ArrayList<Album> albums;

  // album selection listeners
  private final ArrayList<AlbumSelectionListener> albumSelectionListeners;

  // album update listeners
  private final ArrayList<AlbumUpdateListener> albumUpdateListeners;

  // current album
  private Album currentAlbum;

  // current photo
  private Photo currentPhoto;

  // executor used to search photos in directories
  private final ExecutorService executor;

  // photo selection listeners
  private final ArrayList<PhotoSelectionListener> photoSelectionListeners;

  // photo update listeners
  private final ArrayList<PhotoUpdateListener> photoUpdateListeners;

  /**
   * Default constructor.
   */
  public AlbumModelImpl() {
    this.executor = Executors.newFixedThreadPool(MAX_THREADS);
    this.albums = new ArrayList<Album>();
    this.albumListeners = new ArrayList<AlbumListener>();
    this.albumSelectionListeners = new ArrayList<AlbumSelectionListener>();
    this.albumUpdateListeners = new ArrayList<AlbumUpdateListener>();
    this.photoSelectionListeners = new ArrayList<PhotoSelectionListener>();
    this.photoUpdateListeners = new ArrayList<PhotoUpdateListener>();
  }

  @Override
  public void addAlbum() {
    Album album = new Album();
    this.albums.add(album);
    this.fireAlbumAdded(album);
  }

  @Override
  public void addAlbumListener(AlbumListener listener) {
    this.albumListeners.add(listener);
  }

  @Override
  public void addAlbumSelectionListener(AlbumSelectionListener listener) {
    this.albumSelectionListeners.add(listener);
  }

  @Override
  public void addAlbumUpdateListener(AlbumUpdateListener listener) {
    this.albumUpdateListeners.add(listener);
  }

  @Override
  public void addPhotoSelectionListener(PhotoSelectionListener listener) {
    this.photoSelectionListeners.add(listener);
  }

  @Override
  public void addPhotoUpdatedListener(PhotoUpdateListener photoUpdatedListener) {
    this.photoUpdateListeners.add(photoUpdatedListener);
  }

  @Override
  public void addSavedAlbum(Album album) {
    this.albums.add(album);
    Album.setIdOfSavedAlbum(album.getId());
  }

  /**
   * This method browses a directory and all its sub directories. Each time a
   * readable image file is encountered a new photo is added to the album.
   * 
   * @param directory
   *            the root directory.
   * @param album
   *            the album in which to add photos.
   */
  private void crawle(File directory, final Album album) {
    if (directory.listFiles() != null)
      for (final File f : directory.listFiles()) {
        if (f.isDirectory()) {
          // Create a thread for each sub directory
          executor.execute(new Runnable() {
            @Override
            public void run() {
              crawle(f, album);
            }
          });
        } else {
          String mimeType = URLConnection.guessContentTypeFromName(f.getName());
          for (final String mime : ImageIO.getReaderMIMETypes()) {
            // if the file is a readable photo
            if (mimeType != null && mimeType.equals(mime)) {
              try {
                Photo photo = new Photo(f, album);
                photo.setType(mimeType);
                album.addPhoto(photo);
                this.firePhotoAdded(album, photo);
              } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
              }
            }
          }
        }
      }
  }

  @Override
  public PhotoSortModel createNewPhotoSortModel() {
    return new PhotoSortModelImpl();
  }

  /**
   * This method is called each time an album is added to this model.
   * 
   * @param album
   *            the new album.
   */
  protected void fireAlbumAdded(Album album) {
    for (AlbumListener listener : this.albumListeners) {
      listener.albumAdded(album);
    }
  }

  /**
   * This method is called each time an album is removed from this model.
   * 
   * @param album
   *            the removed album.
   */
  protected void fireAlbumRemoved(Album album) {
    for (AlbumListener listener : this.albumListeners) {
      listener.albumRemoved(album);
    }
  }

  /**
   * This method is called each time an album is renamed.
   * 
   * @param album
   *            the album.
   * @param newName
   *            the new name.
   */
  protected void fireAlbumRenamed(Album album, String newName) {
    for (AlbumUpdateListener listener : this.albumUpdateListeners) {
      listener.albumRenamed(album, newName);
    }
  }

  /**
   * This method is called each time an album is selected.
   * 
   * @param album
   *            the selected album.
   */
  protected void fireAlbumSelected(Album album) {
    for (AlbumSelectionListener listener : this.albumSelectionListeners) {
      listener.albumSelected(album);
    }
  }

  /**
   * This method is called each time the geo position of a photo is modified.
   * 
   * @param photo
   *            the photo.
   * @param geo
   *            the new geo position.
   */
  protected void fireGeopositionUpdated(Photo photo, GeoPosition geo) {
    for (PhotoUpdateListener listener : this.photoUpdateListeners) {
      listener.geopPositionUpdated(photo, geo);
    }
  }

  /**
   * This method is called each time a photo is added to an album.
   * 
   * @param album
   *            the album.
   * @param photo
   *            the new photo.
   */
  protected void firePhotoAdded(Album album, Photo photo) {
    for (AlbumUpdateListener listener : this.albumUpdateListeners) {
      listener.photoAdded(album, photo);
    }
  }

  /**
   * This method is called each time a photo is selected.
   * 
   * @param photo
   *            the selected photo.
   */
  protected void firePhotoSelected(Photo photo) {
    for (PhotoSelectionListener listener : this.photoSelectionListeners) {
      listener.photoSelected(photo);
    }
  }

  @Override
  public List<? extends Album> getAlbums() {
    return this.albums;
  }

  @Override
  public Album getSelectedAlbum() {
    return this.currentAlbum;
  }

  @Override
  public Photo getSelectedPhoto() {
    return this.currentPhoto;
  }

  @Override
  public List<? extends Album> getSortedAlbums(Comparator<Album> comparator) {
    Collections.sort(this.albums, comparator);
    return Collections.unmodifiableList(this.albums);
  }

  @Override
  public void linkAlbum(Album album, File albumFile) {
    if (album.hasDefaultName()) {
      this.nameAlbum(album, albumFile.getName());
    }
    album.setDirectory(albumFile);
    this.crawle(albumFile, album);
  }

  @Override
  public void nameAlbum(Album album, String name) {
    if (album != null && (name == null || name.length() > 0)) {
      album.setName(name);
      this.fireAlbumRenamed(album, name);
    }
  }

  @Override
  public void removeAlbum(List<Album> albums) {
    for (Album album : albums) {
      if (this.albums.contains(album)) {
        this.albums.remove(album);
        this.fireAlbumRemoved(album);
      }
    }
  }

  @Override
  public void selectAlbum(Album album) {
    if (this.currentAlbum == null || !this.currentAlbum.equals(album)) {
      this.currentAlbum = album;
      this.fireAlbumSelected(album);
    }
  }

  @Override
  public void selectPhoto(Photo photo) {
    this.currentPhoto = photo;
    this.firePhotoSelected(photo);
  }

  @Override
  public void updateGeoPosition(Photo photo, GeoPosition geo) {
    if (geo != null) {
      photo.setGeoPosition(geo);
      this.fireGeopositionUpdated(photo, geo);
    }
  }
}
