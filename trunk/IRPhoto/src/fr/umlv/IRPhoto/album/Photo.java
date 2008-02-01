package fr.umlv.IRPhoto.album;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.swing.ImageIcon;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;

import fr.umlv.IRPhoto.util.ImageUtil;

/**
 * 
 * This class represents a photo containing in an album.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class Photo implements Serializable {

  /**
   * 
   * This class defines a latitude and a longitude to represent a position on a
   * map.
   * 
   * @author MIETTE Tom
   * @author MOURET Sebastien
   * 
   */
  public static class GeoPosition implements Serializable {

    private static final long serialVersionUID = 1103404436818445389L;

    /**
     * Tests if the two parameters are corrects coordinates to create a new geo
     * position and returns it.
     * 
     * @param latitude
     *            the latitude string.
     * @param longitude
     *            the longitude string.
     * @return the new geo position if the coordinates are correct, or null.
     */
    public static GeoPosition validateCoordinates(String latitude,
        String longitude) {
      try {
        double longitudeDouble = Double.parseDouble(longitude);
        double latitudeDouble = Double.parseDouble(latitude);
        return new GeoPosition(latitudeDouble, longitudeDouble);
      } catch (NumberFormatException e1) {
        return null;
      }
    }

    private double latitude;

    private double longitude;

    /**
     * Constructor of a geo position.
     */
    public GeoPosition(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
    }

    /**
     * Returns the latitude value.
     * 
     * @return the latitude value.
     */
    public double getLatitude() {
      return this.latitude;
    }

    /**
     * Returns the longitude value.
     * 
     * @return the longitude value.
     */
    public double getLongitude() {
      return this.longitude;
    }

    /**
     * Sets the latitude value of this geo position.
     * 
     * @param latitude
     *            the new latitude value of this geo position.
     */
    public void setLatitude(double latitude) {
      this.latitude = latitude;
    }

    /**
     * Sets the longitude value of this geo position.
     * 
     * @param longitude
     *            the new longitude value of this geo position.
     */
    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

  }

  /**
   * Default dimension of the miniature of a photo.
   */
  public static final Dimension DEFAULT_MINIATURE_DIMENSION = new Dimension(96,
      96);

  /**
   * Comparator of photos and order them by last modified date.
   */
  public static final Comparator<Photo> PHOTO_LAST_MODIFIED_DATE_COMPARATOR = new Comparator<Photo>() {
    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.date.compareTo(o2.date));
    }

  };

  /**
   * Comparator of photos and order them by name.
   */
  public static final Comparator<Photo> PHOTO_NAME_COMPARATOR = new Comparator<Photo>() {
    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.getName().compareTo(o2.getName()));
    }

  };

  /**
   * Comparator of photos and order them by type.
   */
  public static final Comparator<Photo> PHOTO_TYPE_COMPARATOR = new Comparator<Photo>() {
    @Override
    public int compare(Photo o1, Photo o2) {
      return (o1.getType().compareTo(o2.getType()));
    }

  };

  private static final long serialVersionUID = -7367951897048766147L;

  // album which contains this photo
  private final Album album;

  // last modified date of this photo
  private Date date;

  // dimension of this photo
  private Dimension dimension;

  // file of this photo
  private final File file;

  // geo position of this photo
  private GeoPosition geoPosition;

  // exif data
  private Metadata metadata;

  // name of this photo without file extension
  private String nameWithoutExtension;

  // scaled representation of this photo
  private transient Image scaledImage;

  // type of this photo
  private String type;

  /**
   * Constructor of a new photo.
   * 
   * @param file
   *            the file of this photo.
   * @param album
   *            the album which contains this photo.
   * @throws FileNotFoundException
   *             if the file does not exist.
   */
  public Photo(File file, Album album) throws FileNotFoundException {
    if (!file.exists() || !file.canRead()) {
      throw new FileNotFoundException("The file " + file.getAbsolutePath()
          + " does not exist.");
    }
    this.file = file;
    this.date = new Date(this.file.lastModified());
    this.album = album;

    // read exif data of the file (only jpeg file)
    try {
      this.metadata = JpegMetadataReader.readMetadata(file);
    } catch (JpegProcessingException e) {
      this.metadata = null;
    }

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

  /**
   * Returns the album which contains this photo.
   * 
   * @return the album which contains this photo.
   */
  public Album getAlbum() {
    return this.album;
  }

  /**
   * Returns the dimension of this photo.
   * 
   * @return the dimension of this photo.
   */
  public Dimension getDimension() {
    // if the dimension does not exist already, calculates it with the scaled
    // image
    if (this.dimension == null) {
      getScaledInstance();
    }
    return this.dimension;
  }

  /**
   * Returns the geo position of this photo.
   * 
   * @return the geo position of this photo.
   */
  public GeoPosition getGeoPosition() {
    return this.geoPosition;
  }

  /**
   * Returns an icon of this photo. The icon is recalculated each time this
   * method is called.
   * 
   * @return an icon of this photo.
   */
  public ImageIcon getImageIcon() {
    ImageIcon icon = new ImageIcon(this.getPath());
    // set the dimension of the photo
    this.dimension = new Dimension(icon.getIconWidth(), icon.getIconHeight());
    return icon;
  }

  /**
   * Returns the exif data of the photo.
   * 
   * @return the exif data.
   */
  public Metadata getMetadata() {
    return this.metadata;
  }

  /**
   * Returns the name of this photo.
   * 
   * @return the name of this photo.
   */
  public String getName() {
    return this.file.getName();
  }

  /**
   * Returns the name of this photo without file extension.
   * 
   * @return the name of this photo without file extension.
   */
  public String getNameWithoutExtension() {
    if (nameWithoutExtension == null) {
      nameWithoutExtension = getName();
      int index = nameWithoutExtension.lastIndexOf(".");
      if (index != -1) {
        nameWithoutExtension = nameWithoutExtension.substring(0, index);
      }
    }
    return nameWithoutExtension;
  }

  /**
   * Returns the absolute path of this photo.
   * 
   * @return the absolute path of this photo.
   */
  public String getPath() {
    return this.file.getAbsolutePath();
  }

  /**
   * Returns a scaled image of this photo with the default dimension.
   * 
   * @return a scaled image of this photo.
   */
  public Image getScaledInstance() {
    // the scaled image is calculated the first time this method is called
    if (this.scaledImage == null) {
      scaledImage = ImageUtil
          .getScaledImage(getImageIcon().getImage(),
              DEFAULT_MINIATURE_DIMENSION.width,
              DEFAULT_MINIATURE_DIMENSION.height);
    }
    return scaledImage;
  }

  /**
   * Returns the type of this image.
   * 
   * @return the type of this image.
   */
  public String getType() {
    return this.type;
  }

  @Override
  public int hashCode() {
    return this.getPath().hashCode();
  }

  /**
   * Sets the geo position of this photo.
   * 
   * @param geoPosition
   *            the new geo position of this photo.
   */
  public void setGeoPosition(GeoPosition geoPosition) {
    this.geoPosition = geoPosition;
  }

  /**
   * Sets the type of this photo.
   * 
   * @param type
   *            the new type of this photo.
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Photo ").append("(name=").append(this.getName()).append(") ");
    return sb.toString();
  }

}
