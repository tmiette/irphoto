package fr.umlv.IRPhoto.gui.panel.album;

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

  protected void fireAlbumRenamed(Album album, String newName) {
    for (AlbumListener listener : this.listeners) {
      listener.albumRenamed(album, newName);
    }
  }

  protected void firePhotoAdded(Album album, Photo photo) {
    for (AlbumListener listener : this.listeners) {
      listener.photoAdded(album, photo);
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
    if (name == null || name.length() > 0) {
      album.setName(name);
      this.fireAlbumRenamed(album, name);
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
            try {
              Photo photo = new Photo(f);
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
