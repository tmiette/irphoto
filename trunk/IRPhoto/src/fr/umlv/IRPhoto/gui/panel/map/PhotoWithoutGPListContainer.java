/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModel;
import fr.umlv.IRPhoto.gui.panel.album.PhotoUpdatedListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoUpdatedModel;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoWithoutGPListContainer implements ContainerInitializer {

  private final AlbumSelectionModel albumSelectionModel;
  private final PhotoSelectionModel photoSelectionModel;
  private final PhotoUpdatedModel photoUpdatedModel;
  private final JPanel panel;
  private final JPanel photoListPanel;
  private JTextField latitudeField;
  private JTextField longitudeField;
  private final HashMap<Photo, JLabel> photos;
  private Photo photoSelected;
  private JButton button;
  public static final Dimension DEFAULT_THUMBNAIL_SIZE = new Dimension(100, 100);
  public static final int DEFAULT_THUMBNAIL_NB = 3;
  public static final Border DEFAULT_THUMBNAIL_SELECTED_BORDER = BorderFactory
      .createLineBorder(Color.BLACK);

  /**
   * 
   */
  public PhotoWithoutGPListContainer(AlbumSelectionModel albumSelectionModel,
      PhotoSelectionModel photoSelectionModel, PhotoUpdatedModel photoUpdatedModel) {
    this.photoUpdatedModel = photoUpdatedModel;
    this.photoUpdatedModel.addPhotoUpdatedListener(new PhotoUpdatedListener() {

      @Override
      public void geoppositionUpdated(Photo photo) {
       removePhoto(photo);
        photoListPanel.repaint();
      }

      @Override
      public void nameUpdated(Photo photo) {
        // TODO Auto-generated method stub
        
      }
      
    });
    this.photoSelectionModel = photoSelectionModel;
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
            photoListPanel.removeAll();
            addPhotos(getPhotosWhitoutGP(album.getPhotos()));
            photoListPanel.validate();
          }

        });

    this.photos = new HashMap<Photo, JLabel>();

    this.photoListPanel = this.createPhotoListPanel(this
        .getPhotosWhitoutGP(Collections.<Photo> emptyList()));
    final JPanel textFieldPanel = this.createTextFieldPanel();

    JScrollPane scrollPane = new JScrollPane(this.photoListPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    this.panel = new JPanel(new BorderLayout());
    this.panel.add(scrollPane, BorderLayout.CENTER);
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

    this.button = new JButton("OK");
    this.button.addActionListener(new ActionListener() {
      /*
       * (non-Javadoc)
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        if (photoSelected != null && latitudeField != null
            && longitudeField != null
            && isValidCoordinate(latitudeField.getText())
            && isValidCoordinate(longitudeField.getText())) {
          photoSelected
              .setLatitude(Double.parseDouble(latitudeField.getText()));
          photoSelected.setLongitude(Double
              .parseDouble(longitudeField.getText()));
          photoUpdatedModel.geopositionUpdated(photoSelected);
        }
      }
    });

    jp.add(this.latitudeField);
    jp.add(this.longitudeField);
    jp.add(button);
    return jp;
  }

  private boolean isValidCoordinate(String coordinate) {
    try {
      Double.parseDouble(coordinate);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private JPanel createPhotoListPanel(List<Photo> photos) {
    final JPanel jp = new JPanel(null);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

    this.addPhotos(photos);
    Dimension dim = new Dimension(DEFAULT_THUMBNAIL_SIZE.width,
        DEFAULT_THUMBNAIL_SIZE.height * DEFAULT_THUMBNAIL_NB);
    jp.setPreferredSize(dim);
    return jp;
  }

  /**
   * Adds photos to photo list panel.
   * 
   * @param photos photos to add
   */
  private void addPhotos(List<Photo> photos) {
    for (Photo photo : photos) {
      System.out.println("ajout photo " + photo);
      this.addPhoto(photo);
    }
  }

  /**
   * Adds a photo to photo list panel.
   * 
   * @param photo photo to add
   */
  private void addPhoto(final Photo photo) {
    ImageIcon icon = photo.getImageIcon();
    double ratio = icon.getIconWidth() / DEFAULT_THUMBNAIL_SIZE.getWidth();
    int w = (int) (icon.getIconWidth() / ratio);
    int h = (int) (icon.getIconHeight() / ratio);
    final JLabel label = new JLabel();
    label.setPreferredSize(DEFAULT_THUMBNAIL_SIZE);
    icon = new ImageIcon(getScaledImage(icon.getImage(), w, h));
    label.setIcon(icon);
    label.addMouseListener(new MouseAdapter() {
      /*
       * (non-Javadoc)
       * 
       * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
       */
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getClickCount() == 1) {
          // Unselecting photo
          if (photoSelected != null) {
            photos.get(photoSelected).setBorder(
                DEFAULT_THUMBNAIL_SELECTED_BORDER);
          }
          // Selecting photo
          photoSelectionModel.selectPhoto(photo);
          label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
          photoSelected = photo;
        }
      }
    });
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
      this.photoSelected = null;
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