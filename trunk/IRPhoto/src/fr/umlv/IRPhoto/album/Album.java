package fr.umlv.IRPhoto.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {

  private static int albumsCreated;
  private final int id;
  private final ArrayList<Photo> photos;
  private String name;

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

  public List<Photo> getPhotos() {
    return Collections.unmodifiableList(this.photos);
  }

  public boolean addPhoto(Photo photo) {
    return this.photos.add(photo);
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
