/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoWithoutGPListContainer implements ContainerInitializer {

  private final AlbumSelectionModel albumSelectionModel;
  private final JPanel panel;
  private final JPanel photoListPanel;
  private JTextField latitudeField;
  private JTextField longitudeField;
  private final HashMap<Photo, JLabel> photos;
  public static final Dimension DEFAULT_THUMBNAIL_SIZE = new Dimension(30, 30);

  /**
   * 
   */
  public PhotoWithoutGPListContainer(AlbumSelectionModel albumSelectionModel) {
    this.albumSelectionModel = albumSelectionModel;
    this.albumSelectionModel
        .addAlbumSelectionListener(new AlbumSelectionListener() {
          /*
           * (non-Javadoc)
           * 
           * @see fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener#albumSelected(fr.umlv.IRPhoto.album.Album)
           */
          @Override
          public void albumSelected(Album album) {
            addPhotos(getPhotosWhitoutGP(album.getPhotos()));
          }

        });

    this.photos = new HashMap<Photo, JLabel>();

    this.photoListPanel = this.createPhotoListPanel(this
        .getPhotosWhitoutGP(Collections.<Photo> emptyList()));
    final JPanel textFieldPanel = this.createTextFieldPanel();
    this.panel = new JPanel(new BorderLayout());
    this.panel.add(this.photoListPanel, BorderLayout.CENTER);
    this.panel.add(textFieldPanel, BorderLayout.SOUTH);
  }

  /**
   * @return
   */
  private JPanel createTextFieldPanel() {
    JPanel jp = new JPanel(null);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
    
    this.latitudeField = new JTextField("latitude");
    this.longitudeField = new JTextField("longitude");

    final JButton button = new JButton("OK");
    button.addActionListener(new ActionListener() {
      /*
       * (non-Javadoc)
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // changer les coordonnees de la photo selectionnee
      }
    });

    jp.add(this.latitudeField);
    jp.add(this.longitudeField);
    jp.add(button);
    return jp;
  }

  private JPanel createPhotoListPanel(List<Photo> photos) {
    final JPanel jp = new JPanel(null);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

    this.addPhotos(photos);

    return jp;
  }

  /**
   * Adds photos to photo list panel.
   * 
   * @param photos photos to add
   */
  private void addPhotos(List<Photo> photos) {
    for (Photo photo : photos) {
      this.addPhoto(photo);
    }
  }

  /**
   * Adds a photo to photo list panel.
   * 
   * @param photo photo to add
   */
  private void addPhoto(Photo photo) {
    ImageIcon icon = photo.getImageIcon();
    double ratio = icon.getIconWidth() / DEFAULT_THUMBNAIL_SIZE.getWidth();
    int w = (int) (icon.getIconWidth() / ratio);
    int h = (int) (icon.getIconHeight() / ratio);
    JLabel label = new JLabel();
    label.setPreferredSize(DEFAULT_THUMBNAIL_SIZE);
    icon = new ImageIcon(getScaledImage(icon.getImage(), w, h));
    label.setIcon(icon);
    this.photos.put(photo, label);
    this.photoListPanel.add(label);
  }

  /**
   * Removes a photo from photo list panel.
   * 
   * @param photo photo to remove
   */
  private void removePhoto(Photo photo) {
    JLabel label = this.photos.get(photo);
    if (label != null) {
      this.photoListPanel.remove(label);
      this.photos.remove(photo);
    }
  }

  /**
   * Gets a list of photos which are not containing Geoposition information.
   * 
   * @param photos
   * @return photos list with no Geoposition information
   */
  private List<Photo> getPhotosWhitoutGP(List<Photo> photos) {
    ArrayList<Photo> photosWithoutGP = new ArrayList<Photo>();
    for (Photo photo : photos) {
      if (photo.getLatitude() == 0 && photo.getLongitude() == 0) {
        photosWithoutGP.add(photo);
      }
    }

    return photosWithoutGP;
  }

  /*
   * (non-Javadoc)
   * 
   * @see fr.umlv.IRPhoto.gui.ContainerInitializer#getComponent()
   */
  @Override
  public JComponent getComponent() {
    return this.panel;
  }

  private static Image getScaledImage(Image srcImg, int w, int h) {
    BufferedImage resizedImg = new BufferedImage(DEFAULT_THUMBNAIL_SIZE.width,
        DEFAULT_THUMBNAIL_SIZE.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    int x = DEFAULT_THUMBNAIL_SIZE.width - w;
    int y = DEFAULT_THUMBNAIL_SIZE.height - h;
    g2.drawImage(srcImg, x / 2, y / 2, w, h, null);
    g2.dispose();
    return resizedImg;
  }
}
