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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import main.Main;
import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.util.IconFactory;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoWithoutGPListContainer implements ContainerInitializer {

  private ExecutorService executor;
  private static final Object lock = new Object();
  private static final Logger logger = Logger.getLogger(Main.loggerName);
  private final AlbumModel albumModel;
  private final JPanel panel;
  private final JPanel photoListPanel;
  private JTextField latitudeField;
  private JTextField longitudeField;
  private final HashMap<Photo, JLabel> photos;
  private Photo previousSelectedPhoto;
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
  public PhotoWithoutGPListContainer(AlbumModel albumModel) {

    this.albumModel = albumModel;
    this.albumModel.addAlbumListener(new AlbumListener() {
      @Override
      public void albumRemoved(Album album) {
        if (photoListPanel != null) {
          photoListPanel.removeAll();
          photoListPanel.repaint();
        }

      }

      @Override
      public void albumAdded(Album album) {
        photoListPanel.removeAll();
        if (executor != null) {
          System.out.println("Shutdown now !!!");
          executor.shutdownNow();
        }
        addPhotos(getPhotosWhitoutGP(album.getPhotos()));

      }
    });
    this.albumModel.addAlbumSelectionListener(new AlbumSelectionListener() {
      @Override
      public void albumSelected(Album album) {
        photoListPanel.removeAll();
        if (executor != null) {
          System.out.println("Shutdown now !!!");
          executor.shutdownNow();
        }
        addPhotos(getPhotosWhitoutGP(album.getPhotos()));
      }

    });
    this.albumModel.addPhotoUpdatedListener(new PhotoUpdateListener() {

      @Override
      public void geopPositionUpdated(Photo photo, GeoPosition geo) {
        removePhoto(photo);
        scrollPane.validate();
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
   * Gets a panel containing two text fields to set longitude and latitude values.
   * @return jpanel with boxlayout 
   */
  private JPanel createTextFieldPanel() {
    JPanel jp = new JPanel(null);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

    this.latitudeField = new JTextField("");
    this.longitudeField = new JTextField("");

    this.button = new JButton("OK", IconFactory.getIcon("globe-12x12.png"));
    this.button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        if (albumModel.getSelectedPhoto() != null) {
          GeoPosition g = GeoPosition.validateCoordinates(latitudeField
              .getText(), longitudeField.getText());
          if (g != null) {
            albumModel.updateGeoPosition(albumModel.getSelectedPhoto(), g);
          } else {
            longitudeField.setText("");
            latitudeField.setText("");
          }
        }

      }
    });

    jp.add(this.latitudeField);
    jp.add(this.longitudeField);
    jp.add(button);
    return jp;
  }

  /**
   * Gets a panel containing photos without geoposition coordinates.
   * @param photos list of photos
   * @return jpanel containing photos
   */
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
    logger.info("adding photos to list panel");
    
    // Delegate work to executor
    executor = Executors.newFixedThreadPool(2);
    for (final Photo photo : photos) {
      executor.execute(new Runnable() {
        @Override
        public void run() {
          synchronized (lock) {
            addPhoto(photo);
          }
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              photoListPanel.revalidate();
            }
          });
        }
      });
    }
    executor.shutdown();
  }

  /**
   * Adds a single photo to a photo list panel.
   * 
   * @param photo photo to add
   */
  private void addPhoto(final Photo photo) {
    // ImageIcon icon = photo.getImageIcon();
    final JLabel label = new JLabel();
    ImageIcon icon = new ImageIcon(photo.getScaledInstance());
    label.setIcon(icon);
    label.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getClickCount() == 1) {
          // Unselecting photo
          if (previousSelectedPhoto != null) {
            photos.get(previousSelectedPhoto).setBorder(
                DEFAULT_THUMBNAIL_SELECTED_BORDER);
          }
          // Selecting photo
          albumModel.selectPhoto(photo);
          label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
          previousSelectedPhoto = photo;
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
      this.previousSelectedPhoto = null;
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
  public JComponent getContainer() {
    return this.panel;
  }

}
