package fr.umlv.IRPhoto.album;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {

  private static int albumsCreated;
  private final int id;
  private final ArrayList<Photo> photos;
  private String name;
  private File file;

  public Album() {
    this.id = albumsCreated++;
    this.name = "Album" + this.id;
    this.photos = new ArrayList<Photo>();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public File getFile() {
    return this.file;
  }

  public void setFile(File file) {
    if (this.hasDefaultName()) {
      this.name = file.getName();
    }
    this.file = file;
  }

  public List<Photo> getPhotos() {
    return Collections.unmodifiableList(this.photos);
  }

  public boolean addPhoto(Photo photo) {
    return this.photos.add(photo);
  }

  private boolean hasDefaultName() {
    return this.name.equals("Album" + this.id);
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof Album)) {
      return false;
    }

    Album a = (Album) obj;
    return this.id == a.id;
  }

}
