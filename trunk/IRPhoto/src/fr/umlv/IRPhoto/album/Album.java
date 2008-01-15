package fr.umlv.IRPhoto.album;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Album {

  private static int albumsCreated;
  private final int id;
  private final ArrayList<Photo> photos;
  private String name;
  private File directory;

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

  public File getDirectory() {
    return this.directory;
  }

  public void setDirectory(File directory) {
    if (this.hasDefaultName()) {
      this.name = directory.getName();
    }
    if (!directory.equals(this.directory)) {
      this.directory = directory;
      this.photos.clear();
      crawle(this.directory, this.photos);
    }
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

  public static void crawle(File directory, final ArrayList<Photo> photos) {

    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (final File f : directory.listFiles()) {
      if (f.isDirectory()) {
        // Create a thread for each sub directory
        executor.execute(new Runnable() {
          @Override
          public void run() {
            crawle(f, photos);
          }
        });
      }else{
        //TODO tester si le fichier est une photo
      }
    }
  }

}
