package fr.umlv.IRPhoto.gui.panel.albumtree;

import java.io.File;
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

public class AlbumModelImpl implements AlbumModel {

  private final ArrayList<Album> albums;

  private final ArrayList<AlbumListener> listeners;

  private static MimetypesFileTypeMap mimeTypesFileTypeMap = new MimetypesFileTypeMap();

  public AlbumModelImpl() {
    this.albums = new ArrayList<Album>();
    this.listeners = new ArrayList<AlbumListener>();
  }

  @Override
  public void addAlbum() {
    Album album = new Album();
    this.albums.add(album);
    this.fireAlbumAdded(album);
  }

  @Override
  public void addAlbumListener(AlbumListener listener) {
    this.listeners.add(listener);
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

  protected void fireAlbumAdded(Album album) {
    for (AlbumListener listener : this.listeners) {
      listener.albumAdded(album);
    }
  }

  protected void fireAlbumRemoved(Album album) {
    for (AlbumListener listener : this.listeners) {
      listener.albumRemoved(album);
    }
  }

  protected void fireAlbumUpdated(Album album) {
    for (AlbumListener listener : this.listeners) {
      listener.albumUpdated(album);
    }
  }

  protected void firePhotoAdded(Album album, Photo photo) {
    for (AlbumListener listener : this.listeners) {
      listener.photoAdded(album, photo);
    }
  }

  @Override
  public void linkAlbum(Album album, File albumFile) {
    album.setDirectory(albumFile);
    this.crawle(albumFile, album);
    //this.fireAlbumUpdated(album);
  }

  @Override
  public void nameAlbum(Album album, String name) {
    if (name == null || name.length() > 0) {
      album.setName(name);
      this.fireAlbumUpdated(album);
    }
  }

  @Override
  public List<? extends Album> getSortedAlbums(Comparator<Album> comparator) {
    Collections.sort(this.albums, comparator);
    return Collections.unmodifiableList(this.albums);
  }

  public void crawle(File directory, final Album album) {

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
            Photo photo = new Photo(f);
            album.addPhoto(photo);
            this.firePhotoAdded(album, photo);
          }
        }
      }
    }
  }

}
