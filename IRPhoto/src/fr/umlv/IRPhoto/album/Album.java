package fr.umlv.IRPhoto.album;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * This class represents an album of photos. The album is represented by a
 * directory called the root of the album. The collections of photos contains
 * all readable photos in this root directory and all its sub-directories.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class Album implements Serializable {

  // counter of albums instantiated
  private static int albumsCreated;

  private static final long serialVersionUID = -8637058736078893180L;

  /**
   * Sets the counter of albums instantiated.
   * 
   * @param id
   *            the id of saved album.
   */
  public static void setIdOfSavedAlbum(int id) {
    if (albumsCreated <= id) {
      // increments the counter of albums instantiated
      albumsCreated = id + 1;
    }
  }

  // root directory of the album
  private File directory;

  // unique id of the album
  private final int id;

  // name of this album
  private String name;

  // list of the photos
  private final ArrayList<Photo> photos;

  /**
   * Default constructor of an album.
   */
  public Album() {
    this.id = albumsCreated++;
    // use the default name
    this.name = "Album" + this.id;
    this.photos = new ArrayList<Photo>();
  }

  /**
   * Add a new photo to the list of this album.
   * 
   * @param photo
   *            the new photo.
   * @return if the add method succeed.
   */
  public boolean addPhoto(Photo photo) {
    return this.photos.add(photo);
  }

  /**
   * Removes all photos of this album.
   */
  public void clear() {
    this.photos.clear();
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof Album)) {
      return false;
    }

    Album a = (Album) obj;
    return this.id == a.id;
  }

  /**
   * Returns the root directory of this album.
   * 
   * @return the root directory of this album.
   */
  public File getDirectory() {
    return this.directory;
  }

  /**
   * Returns the unique id of this album.
   * 
   * @return the unique id of this album.
   */
  public int getId() {
    return this.id;
  }

  /**
   * Returns the name of this album.
   * 
   * @return the name of this album.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the list of photos of this album.
   * 
   * @return the list of photos of this album.
   */
  public List<Photo> getPhotos() {
    return Collections.unmodifiableList(this.photos);
  }

  /**
   * Returns the list of photos of this album sorted with a defined comparator.
   * 
   * @param comparator
   *            the comparator.
   * @return the list of sorted photos.
   */
  public List<Photo> getSortedPhotos(Comparator<Photo> comparator) {
    Collections.sort(this.photos, comparator);
    return Collections.unmodifiableList(this.photos);

  }

  /**
   * Returns true if this album has its default name.
   * 
   * @return true if this album has its default name.
   */
  public boolean hasDefaultName() {
    return this.name.equals("Album" + this.id);
  }

  @Override
  public int hashCode() {
    return this.id;
  }

  /**
   * Sets the root directory of this album.
   * 
   * @param directory
   *            the new root directory of this album.
   */
  public void setDirectory(File directory) {
    if (!directory.equals(this.directory)) {
      this.directory = directory;
      this.photos.clear();
    }
  }

  /**
   * Sets the name of this album.
   * 
   * @param name
   *            the new name of this album.
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }

}
