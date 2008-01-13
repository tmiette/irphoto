package fr.umlv.IRPhoto.album;

public class Album {

  private static int albumsCreated;

  private String name;

  public Album() {
    this.name = "Album" + albumsCreated++;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }

}
