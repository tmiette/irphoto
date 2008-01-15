package fr.umlv.IRPhoto.gui.panel.albumtree;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;

public interface AlbumModel {

  public List<? extends Album> getAlbums();

  public List<? extends Album> getSortedAlbums(Comparator<Album> comparator);

  public void addAlbum();

  public void removeAlbum(List<Album> albums);

  public void linkAlbum(Album album, File albumFile);

  public void nameAlbum(Album album, String name);

  public void addAlbumListener(AlbumListener listener);
}
