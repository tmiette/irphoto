package fr.umlv.IRPhoto.gui.view.map;

import java.awt.BorderLayout;
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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
public class PhotosWithoutCoordinatesContainer implements ContainerInitializer {

  // minimum width of photos list panel
  public static final int DEFAULT_WIDTH = Photo.DEFAULT_MINIATURE_DIMENSION.width + 20;

  // minimum number of miniatures always displayed
  public static final int MINIMUM_NUMBER_OF_MINIATURES = 2;

  // minimum dimension for photos list panel
  private static final Dimension LIST_MINIMUM_DIMENSION = new Dimension(
      DEFAULT_WIDTH, Photo.DEFAULT_MINIATURE_DIMENSION.height
          * MINIMUM_NUMBER_OF_MINIATURES);

  // maximum number of threads
  private static final int MAX_THREADS = 2;

  // main container
  private final JPanel container;

  // executor service
  private ExecutorService executor;

  // map to store photos labels
  private final HashMap<Photo, JLabel> photosLabels;

  // panel which lists photos miniatures
  private final JPanel photosListPanel;

  /**
   * Constructor of this container.
   * 
   * @param albumModel
   *            the album model.
   */
  public PhotosWithoutCoordinatesContainer(final AlbumModel albumModel) {

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

    // listen to albums modifications
    albumModel.addAlbumListener(new AlbumListener() {
      @Override
      public void albumAdded(Album album) {
        if (!executor.isShutdown()) {
          executor.shutdownNow();
        }
        photosListPanel.removeAll();
        scrollPane.revalidate();
        addPhotos(getPhotosWhitoutCoordinates(album.getPhotos()));
      }

      @Override
      public void albumRemoved(Album album) {
        if (album.equals(albumModel.getCurrentAlbum())) {
          photosListPanel.removeAll();
          scrollPane.revalidate();
        }
      }
    });
    // listen to albums selections
    albumModel.addAlbumSelectionListener(new AlbumSelectionListener() {
      @Override
      public void albumSelected(Album album) {
        if (!executor.isShutdown()) {
          executor.shutdownNow();
        }
        photosListPanel.removeAll();
        scrollPane.revalidate();
        addPhotos(getPhotosWhitoutCoordinates(album.getPhotos()));
      }

    });
    // listen to photos update
    albumModel.addPhotoUpdatedListener(new PhotoUpdateListener() {
      @Override
      public void geopPositionUpdated(Photo photo, GeoPosition geo) {
        // removes a photo from photos list panel
        final JLabel label = photosLabels.get(photo);
        if (label != null) {
          photosListPanel.remove(label);
        }
        scrollPane.revalidate();
      }

    });

  }

  /**
   * Adds a single photo miniature to the photos list panel.
   * 
   * @param photo
   *            the photo to add.
   */
  private void addPhoto(final Photo photo) {
    final JLabel label = new JLabel(new ImageIcon(photo.getScaledInstance()));
    label.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < photosListPanel.getComponentCount(); i++) {
          ((JLabel) photosListPanel.getComponent(i))
              .setBorder(GraphicalConstants.DEFAULT_THUMBNAIL_UNSELECTED_BORDER);
        }
        label.setBorder(GraphicalConstants.DEFAULT_THUMBNAIL_SELECTED_BORDER);
      }
    });
    this.photosLabels.put(photo, label);
    this.photosListPanel.add(label);
  }

  /**
   * Adds a list of photos to the photos list panel.
   * 
   * @param photos
   *            photos to add.
   */
  private void addPhotos(List<Photo> photos) {
    // Delegate the add of each photo to an executor
    executor = Executors.newFixedThreadPool(MAX_THREADS);
    for (final Photo photo : photos) {
      executor.execute(new Runnable() {
        @Override
        public void run() {
          addPhoto(photo);
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

  @Override
  public JComponent getContainer() {
    return this.container;
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

}
