package fr.umlv.IRPhoto.album;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Album implements Serializable {

  private static final long serialVersionUID = -8637058736078893180L;

  private static int albumsCreated;
  private final int id;
  private final ArrayList<Photo> photos;
  private String name;
  private File directory;
  private boolean busy = false;

  public static void setIdOfSavedAlbum(int id) {
    if (albumsCreated <= id) {
      albumsCreated = id + 1;
    }
  }

  public Album() {
    this.id = albumsCreated++;
    this.name = "Album" + this.id;
    this.photos = new ArrayList<Photo>();
  }

  /**
   * @return the id
   */
  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public File getDirectory() {
    return this.directory;
  }

  public void setDirectory(File directory) {
    if (!directory.equals(this.directory)) {
      this.directory = directory;
      this.photos.clear();
    }
  }

  public List<Photo> getPhotos() {
    return Collections.unmodifiableList(this.photos);
  }

  public List<Photo> getSortedPhotos(Comparator<Photo> comparator) {
    Collections.sort(this.photos, comparator);
    return Collections.unmodifiableList(this.photos);

  }

  public boolean addPhoto(Photo photo) {
    return this.photos.add(photo);
  }

  public boolean hasDefaultName() {
    return this.name.equals("Album" + this.id);
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public int hashCode() {
    return this.id;
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof Album)) {
      return false;
    }

    Album a = (Album) obj;
    return this.id == a.id;
  }

  public void setBusy(boolean busy) {
    this.busy = busy;
  }

  public boolean isBusy() {
    return this.busy;
  }

}
