package fr.umlv.IRPhoto.album;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.Date;

import javax.swing.ImageIcon;

public class Photo implements Serializable {

  private static final long serialVersionUID = -7367951897048766147L;
  
  private final File file;
  private String type;
  private transient ImageIcon icon;
 private GeoPosition geoPosition;
  private Dimension dimension;
  private Date date;
  private final Album album;

  public static final Comparator<Photo> PHOTO_LAST_MODIFIED_DATE_COMPARATOR = new Comparator<Photo>() {

    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.date.compareTo(o2.date));
    }

  };

  public static final Comparator<Photo> PHOTO_NAME_COMPARATOR = new Comparator<Photo>() {

    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.getName().compareTo(o2.getName()));
    }

  };

  public static final Comparator<Photo> PHOTO_TYPE_COMPARATOR = new Comparator<Photo>() {

    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.getType().compareTo(o2.getType()));
    }

  };

  public Photo(File file, Album album) throws FileNotFoundException {
    if (!file.exists() || !file.canRead()) {
      throw new FileNotFoundException("The file " + file.getAbsolutePath()
          + " does not exist.");
    }
    this.file = file;
    this.type = URLConnection.guessContentTypeFromName(this.file.getName());
    this.date = new Date(this.file.lastModified());
    // TODO appel bloquant
    this.icon = new ImageIcon(this.getPath());
    this.album = album;
  }
  
  /**
   * @return the geoPosition
   */
  public GeoPosition getGeoPosition() {
    return this.geoPosition;
  }
  
  /**
   * @param geoPosition the geoPosition to set
   */
  public void setGeoPosition(GeoPosition geoPosition) {
    this.geoPosition = geoPosition;
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

  public void setType(String type) {
    this.type = type;
  }

  public ImageIcon getImageIcon() {
    if(this.icon == null){
      this.icon = new ImageIcon(this.getPath());
    }
    return this.icon;
  }

  public Dimension getDimension() {
    if (this.dimension == null) {
      this.dimension = new Dimension(this.icon.getIconWidth(), this.icon
          .getIconHeight());
    }
    return this.dimension;
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
    sb.append("Photo ").append("(name=").append(this.getName()).append(") ");
    return sb.toString();
  }
  
  public static class GeoPosition implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1103404436818445389L;
    private double latitude;
    private double longitude;
    
    /**
     * 
     */
    public GeoPosition(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
      return this.longitude;
    }
    
    /**
     * @return the latitude
     */
    public double getLatitude() {
      return this.latitude;
    }
    
    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
      this.latitude = latitude;
    }
    
    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

  }

  /**
   * @return the album
   */
  public Album getAlbum() {
    return this.album;
  }

}
