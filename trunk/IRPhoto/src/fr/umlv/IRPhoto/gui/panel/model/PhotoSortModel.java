package fr.umlv.IRPhoto.gui.panel.model;

import java.util.Comparator;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public interface PhotoSortModel {

  public void addPhotoSortListener(PhotoSortListener listener);

  public void sortPhoto(Album album, Comparator<Photo> comparator);

  public void matchPhoto(Album album, String prefix);

}
