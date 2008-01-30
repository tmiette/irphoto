package fr.umlv.IRPhoto.gui.panel.model.album;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.PhotoSelectionListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortModelImpl;

public class AlbumModelImpl implements AlbumModel {

  private static final int MAX_THREADS = 2;

  private final ArrayList<Album> albums;

  private Album currentAlbum;

  private Photo currentPhoto;

  private final ExecutorService executor;

  private final ArrayList<PhotoSelectionListener> photoSelectionListeners;

  private final ArrayList<PhotoUpdateListener> photoUpdateListeners;

  private final ArrayList<AlbumListener> albumListeners;

  private final ArrayList<AlbumSelectionListener> albumSelectionListeners;

  private final ArrayList<AlbumUpdateListener> albumUpdateListeners;

  private static MimetypesFileTypeMap mimeTypesFileTypeMap = new MimetypesFileTypeMap();

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

  @Override
  public void addAlbum() {
    Album album = new Album();
    this.albums.add(album);
    this.fireAlbumAdded(album);
  }

  @Override
  public List<? extends Album> getAlbums() {
    return this.albums;
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
  public List<? extends Album> getSortedAlbums(Comparator<Album> comparator) {
    Collections.sort(this.albums, comparator);
    return Collections.unmodifiableList(this.albums);
  }

  @Override
  public Album getCurrentAlbum() {
    return this.currentAlbum;
  }

  @Override
  public void selectAlbum(Album album) {
    if (this.currentAlbum == null || !this.currentAlbum.equals(album)) {
      if (album.getPhotos().size() != 0) {
        this.currentAlbum = album;
        this.fireAlbumSelected(album);
      }
    }
  }

  @Override
  public Photo getSelectedPhoto() {
    return this.currentPhoto;
  }

  @Override
  public void updateGeoPosition(Photo photo, GeoPosition geo) {
    if (geo != null) {
      photo.setGeoPosition(geo);
      this.fireGeopositionUpdated(photo, geo);
    }
  }

  @Override
  public void selectPhoto(Photo photo) {
    this.currentPhoto = photo;
    this.firePhotoSelected(photo);
  }

  @Override
  public PhotoSortModel createNewPhotoSortModel() {
    return new PhotoSortModelImpl();
  }

  protected void fireAlbumSelected(Album album) {
    for (AlbumSelectionListener listener : this.albumSelectionListeners) {
      listener.albumSelected(album);
    }
  }

  protected void fireAlbumAdded(Album album) {
    for (AlbumListener listener : this.albumListeners) {
      listener.albumAdded(album);
    }
  }

  protected void fireAlbumRemoved(Album album) {
    for (AlbumListener listener : this.albumListeners) {
      listener.albumRemoved(album);
    }
  }

  protected void fireAlbumRenamed(Album album, String newName) {
    for (AlbumUpdateListener listener : this.albumUpdateListeners) {
      listener.albumRenamed(album, newName);
    }
  }

  protected void firePhotoAdded(Album album, Photo photo) {
    for (AlbumUpdateListener listener : this.albumUpdateListeners) {
      listener.photoAdded(album, photo);
    }
  }

  protected void firePhotoSelected(Photo photo) {
    for (PhotoSelectionListener listener : this.photoSelectionListeners) {
      listener.photoSelected(photo);
    }
  }

  protected void fireGeopositionUpdated(Photo photo, GeoPosition geo) {
    for (PhotoUpdateListener listener : this.photoUpdateListeners) {
      listener.geopPositionUpdated(photo, geo);
    }
  }

  private void crawle(File directory, final Album album) {

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
        String mimeType = mimeTypesFileTypeMap.getContentType(f);
        for (final String mime : ImageIO.getReaderMIMETypes()) {
          if (mimeType.equals(mime)) {
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

}
