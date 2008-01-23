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
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumUpdateListener;

public class AlbumModelImpl implements AlbumModel {

  private final ArrayList<Album> albums;

  private Album currentAlbum;

  private final ArrayList<AlbumListener> albumListeners;

  private final ArrayList<AlbumSelectionListener> selectionListeners;

  private final ArrayList<AlbumUpdateListener> updateListeners;

  private static MimetypesFileTypeMap mimeTypesFileTypeMap = new MimetypesFileTypeMap();

  public AlbumModelImpl() {
    this.albums = new ArrayList<Album>();
    this.albumListeners = new ArrayList<AlbumListener>();
    this.selectionListeners = new ArrayList<AlbumSelectionListener>();
    this.updateListeners = new ArrayList<AlbumUpdateListener>();
  }

  @Override
  public void addAlbumListener(AlbumListener listener) {
    this.albumListeners.add(listener);
  }

  @Override
  public void addAlbumSelectionListener(AlbumSelectionListener listener) {
    this.selectionListeners.add(listener);
  }

  @Override
  public void addAlbumUpdateListener(AlbumUpdateListener listener) {
    this.updateListeners.add(listener);
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
    this.currentAlbum = album;
    this.fireAlbumSelected(album);
  }

  protected void fireAlbumSelected(Album album) {
    for (AlbumSelectionListener listener : this.selectionListeners) {
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
    for (AlbumUpdateListener listener : this.updateListeners) {
      listener.albumRenamed(album, newName);
    }
  }

  protected void firePhotoAdded(Album album, Photo photo) {
    for (AlbumUpdateListener listener : this.updateListeners) {
      listener.photoAdded(album, photo);
    }
  }

  private void crawle(File directory, final Album album) {

    ExecutorService executor = Executors.newFixedThreadPool(10);

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
