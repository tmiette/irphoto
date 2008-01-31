/**
 * 
 */
package fr.umlv.IRPhoto.gui.view.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.model.album.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.gui.view.ContainerInitializer;
import fr.umlv.IRPhoto.util.GraphicalConstants;
import fr.umlv.IRPhoto.util.IconFactory;

/**
 * This class represents the collapse panel which lists all photos without
 * coordinates of an album.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoWithoutGPListContainer implements ContainerInitializer {

  private ExecutorService executor;
  private static final Object lock = new Object();
  private final AlbumModel albumModel;
  // main container
  private final JPanel container;
  // panel which lists photos miniatures
  private final JPanel photosListPanel;
  // map to store photos labels
  private final HashMap<Photo, JLabel> photosLabels;
  private Photo previousSelectedPhoto;

  // minimum width of photos list panel
  public static final int DEFAULT_WIDTH = Photo.DEFAULT_MINIATURE_DIMENSION.width + 20;

  // minimum number of miniatures always displayed
  public static final int MINIMUM_NUMBER_OF_MINIATURES = 2;

  // default border of the selected miniature
  public static final Border DEFAULT_THUMBNAIL_SELECTED_BORDER = BorderFactory
      .createLineBorder(Color.BLACK);

  // minimum dimension for photos list panel
  private static final Dimension LIST_MINIMUM_DIMENSION = new Dimension(
      DEFAULT_WIDTH, Photo.DEFAULT_MINIATURE_DIMENSION.height
          * MINIMUM_NUMBER_OF_MINIATURES);

  /**
   * Constructor of this container.
   * 
   * @param albumModel
   *            the album model.
   */
  public PhotoWithoutGPListContainer(final AlbumModel albumModel) {

    this.photosLabels = new HashMap<Photo, JLabel>();

    // initialize photos list panel
    this.photosListPanel = new JPanel(null);
    this.photosListPanel.setLayout(new BoxLayout(photosListPanel,
        BoxLayout.Y_AXIS));
    this.photosListPanel
        .setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    final JScrollPane scrollPane = new JScrollPane(this.photosListPanel,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(LIST_MINIMUM_DIMENSION);
    scrollPane.setMinimumSize(new Dimension(0, 0));

    // initialize text fields panel
    final JPanel textFieldsPanel = new JPanel(null);
    textFieldsPanel.setLayout(new BoxLayout(textFieldsPanel, BoxLayout.Y_AXIS));
    final JTextField latitudeField = new JTextField();
    final JTextField longitudeField = new JTextField();
    final JButton submitButton = new JButton("OK", IconFactory
        .getIcon("globe-12x12.png"));
    submitButton.addActionListener(new ActionListener() {
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
    textFieldsPanel.add(latitudeField);
    textFieldsPanel.add(longitudeField);
    textFieldsPanel.add(submitButton);

    // initialize main container
    this.container = new JPanel(new BorderLayout());
    this.container.add(scrollPane, BorderLayout.CENTER);
    this.container.add(textFieldsPanel, BorderLayout.SOUTH);

    this.albumModel = albumModel;
    // listen to albums modifications
    this.albumModel.addAlbumListener(new AlbumListener() {
      @Override
      public void albumRemoved(Album album) {
        if (album.equals(albumModel.getCurrentAlbum())) {
          photosListPanel.removeAll();
          scrollPane.validate();
        }
      }

      @Override
      public void albumAdded(Album album) {
        photosListPanel.removeAll();
        if (executor != null) {
          System.out.println("Shutdown now !!!");
          executor.shutdownNow();
        }
        addPhotos(getPhotosWhitoutCoordinates(album.getPhotos()));

      }
    });
    // listen to albums selections
    this.albumModel.addAlbumSelectionListener(new AlbumSelectionListener() {
      @Override
      public void albumSelected(Album album) {
        photosListPanel.removeAll();
        if (executor != null) {
          System.out.println("Shutdown now !!!");
          executor.shutdownNow();
        }
        addPhotos(getPhotosWhitoutCoordinates(album.getPhotos()));
      }

    });
    // listen to photos update
    this.albumModel.addPhotoUpdatedListener(new PhotoUpdateListener() {
      @Override
      public void geopPositionUpdated(Photo photo, GeoPosition geo) {
        // removes a photo from photos list panel
        final JLabel label = photosLabels.get(photo);
        if (label != null) {
          photosListPanel.remove(label);
          previousSelectedPhoto = null;
        }
        scrollPane.validate();
      }

    });

  }

  /**
   * Adds photos to photo list panel.
   * 
   * @param photos
   *            photos to add
   */
  private void addPhotos(List<Photo> photos) {
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
              photosListPanel.revalidate();
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
   * @param photo
   *            photo to add
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
            photosLabels.get(previousSelectedPhoto).setBorder(
                DEFAULT_THUMBNAIL_SELECTED_BORDER);
          }
          // Selecting photo
          albumModel.selectPhoto(photo);
          label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
          previousSelectedPhoto = photo;
        }
      }
    });
    this.photosLabels.put(photo, label);
    this.photosListPanel.add(label);
  }

  /**
   * Returns all photos without coordinates of an album.
   * 
   * @param photos
   *            the list of photos to analyze.
   * @return photos without coordinates.
   */
  private ArrayList<Photo> getPhotosWhitoutCoordinates(List<Photo> photos) {
    ArrayList<Photo> photosWithoutCoordinates = new ArrayList<Photo>();
    for (Photo photo : photos) {
      if (photo.getGeoPosition() == null) {
        photosWithoutCoordinates.add(photo);
      }
    }
    return photosWithoutCoordinates;
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

}
