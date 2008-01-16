package fr.umlv.IRPhoto.album;

import java.io.File;
import java.util.Comparator;

public class Photo {

  private double latitude;
  private double longitude;
  private File file;
  private String type;
  // TODO date de modif

  public static final Comparator<Photo> DATE_ORDER = new Comparator<Photo>() {

    @Override
    public int compare(Photo o1, Photo o2) {
      // TODO Auto-generated method stub
      return 0;
    }

  };

  public static final Comparator<Photo> NAME_ORDER = new Comparator<Photo>() {

    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.getName().compareTo(o2.getName()));
    }

  };

  public static final Comparator<Photo> TYPE_ORDER = new Comparator<Photo>() {

    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.getType().compareTo(o2.getType()));
    }

  };

  public Photo(File file) {
    this.file = file;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getName() {
    return this.file.getName();
  }

  public String getPath() {
    return this.file.getAbsolutePath();
  }

  public String getType() {
    return this.type;
  }

  @Override
  public int hashCode() {
    return this.getPath().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Photo))
      return false;
    Photo p = (Photo) o;
    if (this.hashCode() == p.hashCode())
      return true;
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Photo ").append("(name=").append(this.getName()).append(") ")
        .append("(latidude=").append(this.latitude).append(", longitude=")
        .append(this.longitude).append(")");
    return sb.toString();
  }

}
