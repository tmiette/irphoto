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

public interface AlbumModel {

  public Album getCurrentAlbum();

  public List<? extends Album> getAlbums();

  public List<? extends Album> getSortedAlbums(Comparator<Album> comparator);

  public void addSavedAlbum(Album album);

  public void addAlbum();

  public void removeAlbum(List<Album> albums);

  public void linkAlbum(Album album, File albumFile);

  public void nameAlbum(Album album, String name);

  public void selectAlbum(Album album);

  public void addAlbumListener(AlbumListener listener);

  public void addAlbumSelectionListener(AlbumSelectionListener listener);

  public void addAlbumUpdateListener(AlbumUpdateListener listener);

  public Photo getSelectedPhoto();

  public void selectPhoto(Photo photo);

  public void updateGeoPosition(Photo photo, GeoPosition geo);

  public void addPhotoSelectionListener(PhotoSelectionListener listener);

  public void addPhotoUpdatedListener(PhotoUpdateListener photoUpdatedListener);
  
  public PhotoSortModel createNewPhotoSortModel();
}
