/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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

import main.Main;
import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSelectionModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoUpdatedListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoUpdatedModel;
import fr.umlv.IRPhoto.util.ImageUtil;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoWithoutGPListContainer implements ContainerInitializer {

  private static final Logger logger = Logger.getLogger(Main.loggerName);
  private final AlbumModel albumModel;
  private final PhotoSelectionModel photoSelectionModel;
  private final PhotoUpdatedModel photoUpdatedModel;
  private final JPanel panel;
  private final JPanel photoListPanel;
  private JTextField latitudeField;
  private JTextField longitudeField;
  private final HashMap<Photo, JLabel> photos;
  private Photo photoSelected;
  private JButton button;
  private final JScrollPane scrollPane;
  public static final Dimension DEFAULT_THUMBNAIL_SIZE = new Dimension(100, 100);
  public static final int DEFAULT_WIDTH = DEFAULT_THUMBNAIL_SIZE.width + 20;
  public static final int DEFAULT_THUMBNAIL_NB = 3;
  public static final Border DEFAULT_THUMBNAIL_SELECTED_BORDER = BorderFactory
      .createLineBorder(Color.BLACK);

  /**
   * 
   */
  public PhotoWithoutGPListContainer(AlbumModel albumModel,
      PhotoSelectionModel photoSelectionModel,
      PhotoUpdatedModel photoUpdatedModel) {

    this.photoUpdatedModel = photoUpdatedModel;
    this.photoUpdatedModel.addPhotoUpdatedListener(new PhotoUpdatedListener() {

      @Override
      public void geoppositionUpdated(Photo photo) {
        removePhoto(photo);
        scrollPane.validate();
      }

      @Override
      public void nameUpdated(Photo photo) {
        // TODO Auto-generated method stub

      }

    });
    this.photoSelectionModel = photoSelectionModel;
    this.albumModel = albumModel;
    this.albumModel.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        // do nothing
      }

      @Override
      public void albumRemoved(Album album) {
        // do nothing
      }

      @Override
      public void albumRenamed(Album album, String newName) {
        // do nothing
      }

      @Override
      public void albumSelected(Album album) {
        photoListPanel.removeAll();
        addPhotos(getPhotosWhitoutGP(album.getPhotos()));
        scrollPane.validate();
      }

      @Override
      public void photoAdded(Album album, Photo photo) {
        // do nothing
      }

    });

    this.photos = new HashMap<Photo, JLabel>();

    this.photoListPanel = this.createPhotoListPanel(this
        .getPhotosWhitoutGP(Collections.<Photo> emptyList()));
    final JPanel textFieldPanel = this.createTextFieldPanel();

    this.scrollPane = new JScrollPane(this.photoListPanel);
    this.scrollPane
        .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.scrollPane
        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    Dimension dim = new Dimension(DEFAULT_WIDTH, DEFAULT_THUMBNAIL_SIZE.height
        * DEFAULT_THUMBNAIL_NB);
    this.scrollPane.setPreferredSize(dim);
    this.scrollPane.setMinimumSize(new Dimension(0, 0));

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
          double latitude = Double.parseDouble(latitudeField.getText());
          double longitude = Double.parseDouble(longitudeField.getText());
          if (photoSelected.getGeoPosition() == null) {
            photoSelected.setGeoPosition(new GeoPosition(latitude, longitude));
          } else {
            photoSelected.getGeoPosition().setLatitude(latitude);
            photoSelected.getGeoPosition().setLongitude(longitude);
          }
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
    return jp;
  }

  /**
   * Adds photos to photo list panel.
   * 
   * @param photos
   *            photos to add
   */
  private void addPhotos(List<Photo> photos) {
    logger.info("adding photos to list panel");
    for (Photo photo : photos) {
      this.addPhoto(photo);
    }
  }

  /**
   * Adds a photo to photo list panel.
   * 
   * @param photo
   *            photo to add
   */
  private void addPhoto(final Photo photo) {
    ImageIcon icon = photo.getImageIcon();
    final JLabel label = new JLabel();
    icon = new ImageIcon(ImageUtil.getScaledImage(icon.getImage(),
        DEFAULT_THUMBNAIL_SIZE.width, DEFAULT_THUMBNAIL_SIZE.height));
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
   * @param photo
   *            photo to remove
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
      if (photo.getGeoPosition() == null) {
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

}
