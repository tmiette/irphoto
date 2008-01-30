package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortModel;
import fr.umlv.IRPhoto.util.GraphicalConstants;
import fr.umlv.IRPhoto.util.IconFactory;

/**
 * The container represents all the miniatures images of an album.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoListContainer implements ContainerInitializer {

  // max number of threads used to display miniatures
  private static final int MAX_THREADS = 5;

  /**
   * Creates and returns a button to sort the photos of an album by name.
   * 
   * @param model
   *            the sort model.
   * @param album
   *            the album.
   * @return the button.
   */
  private static JButton createAlphaSortButton(final PhotoSortModel model,
      final Album album) {
    final JButton b = new JButton(IconFactory.getIcon("name-32x32.png"));
    b.setToolTipText("Sort photos by last name.");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        model.sortPhoto(album, Photo.PHOTO_NAME_COMPARATOR);
      }
    });
    return b;
  }

  /**
   * Creates and returns a button to sort the photos of an album by last
   * modified date.
   * 
   * @param model
   *            the sort model.
   * @param album
   *            the album.
   * @return the button.
   */
  private static JButton createDateSortButton(final PhotoSortModel model,
      final Album album) {
    final JButton b = new JButton(IconFactory.getIcon("calendar-32x32.png"));
    b.setToolTipText("Sort photos by last modification date.");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        model.sortPhoto(album, Photo.PHOTO_LAST_MODIFIED_DATE_COMPARATOR);
      }
    });
    return b;
  }

  /**
   * Creates and returns a button to sort the photos of an album by type.
   * 
   * @param model
   *            the sort model.
   * @param album
   *            the album.
   * @return the button.
   */
  private static JButton createTypeSortButton(final PhotoSortModel model,
      final Album album) {
    final JButton b = new JButton(IconFactory.getIcon("type-2-2-32x32.png"));
    b.setToolTipText("Sort photos by type.");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        model.sortPhoto(album, Photo.PHOTO_TYPE_COMPARATOR);
      }
    });
    return b;
  }

  // container
  private final JPanel container;

  // executors to display miniatures
  private ExecutorService executor;

  // panel with the miniatures
  private JPanel photoListPanel;

  // map the photo with its miniature panel
  private final HashMap<Photo, PhotoMiniatureContainer> photoMiniatures;

  /**
   * Constructor of the container.
   * 
   * @param album
   *            the album with photos to display.
   * @param albumModel
   *            the album model.
   */
  public PhotoListContainer(final Album album, final AlbumModel albumModel) {

    this.executor = Executors.newFixedThreadPool(MAX_THREADS);

    this.photoMiniatures = new HashMap<Photo, PhotoMiniatureContainer>();

    // creates a sort model specific for this panel
    final PhotoSortModel photoSortModel = albumModel.createNewPhotoSortModel();

    // initialize header panel with sort buttons
    final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));
    topPanel.setBackground(GraphicalConstants.BLUE);
    // Field used for the dynamic search
    final JTextField textField = new JTextField(15);
    textField.addCaretListener(new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        photoSortModel.matchPhoto(album, textField.getText());
      }
    });
    topPanel.add(new JLabel(IconFactory.getIcon("find-32x32.png")));
    topPanel.add(textField);
    // Sorts buttons
    topPanel.add(createAlphaSortButton(photoSortModel, album));
    topPanel.add(createTypeSortButton(photoSortModel, album));
    topPanel.add(createDateSortButton(photoSortModel, album));

    // initialize panel with the miniatures
    this.photoListPanel = createPhotoListPanel();

    // initialize main container
    this.container = new JPanel(new BorderLayout());
    this.container.add(topPanel, BorderLayout.NORTH);
    this.container.add(this.photoListPanel, BorderLayout.CENTER);
    this.container.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        photoListPanel.revalidate();
      }
    });

    // listen to the add of photos
    albumModel.addAlbumUpdateListener(new AlbumUpdateListener() {
      @Override
      public void albumRenamed(Album album, String newName) {
        // do nothing
      }

      @Override
      public void photoAdded(Album album2, final Photo photo) {
        if (album.equals(album2)) {
          addNewMiniature(albumModel, photo);
        }
      }
    });

    // listen to the sort of the photos
    photoSortModel.addPhotoSortListener(new PhotoSortListener() {
      @Override
      public void photoSorted(List<Photo> photos) {
        // remove all the miniatures
        photoListPanel.removeAll();
        // re add them in the panel
        for (Photo photo : photos) {
          PhotoMiniatureContainer photoContainer = photoMiniatures.get(photo);
          if (photoContainer != null) {
            photoListPanel.add(photoContainer.getContainer());
          }
        }
        photoListPanel.revalidate();
        photoListPanel.repaint();
      }
    });

    // add the photos already contained on the album
    new Thread(new Runnable() {
      @Override
      public void run() {
        for (final Photo photo : album.getPhotos()) {
          addNewMiniature(albumModel, photo);
        }
      }
    }).start();

  }

  /**
   * Adds a new miniature panel to the list of miniature already existing. The
   * add operation is invoked in a runnable task to not freeze the application.
   * 
   * @param albumModel
   *            the album model.
   * @param photo
   *            the new photo.
   */
  private void addNewMiniature(final AlbumModel albumModel, final Photo photo) {
    executor.execute(new Runnable() {
      // new runnable task
      @Override
      public void run() {
        PhotoMiniatureContainer c = photoMiniatures.get(photo);
        if (c == null) {
          // creates the miniature panel if it not exists.
          c = new PhotoMiniatureContainer(photo, albumModel);
          photoListPanel.add(c.getContainer());
          photoMiniatures.put(photo, c);
        } else {
          photoListPanel.add(c.getContainer());
        }

        // refresh the panel display
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            photoListPanel.revalidate();
          }
        });
      }
    });
  }

  /**
   * Creates and return the panel which contains the miniatures.
   * 
   * @return the panel.
   */
  private JPanel createPhotoListPanel() {

    final JPanel panel = new JPanel(null);
    panel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);

    // set a specific layout to display photos in line depending of the width of
    // the parent container
    panel.setLayout(new LayoutManager() {

      // height of the miniature panel
      private static final int CHILD_HEIGHT = 131;

      // width of the miniature panel
      private static final int CHILD_WIDTH = 116;

      // pixels between lines
      private static final int HEIGHT_SEPARATION = 2;

      // pixels between columns
      private static final int WIDTH_SEPARATION = 2;

      @Override
      public void addLayoutComponent(String name, Component parent) {
        // nothing
      }

      @Override
      public void layoutContainer(Container parent) {
        // display each miniature
        int count = parent.getComponentCount();
        int line = ((int) container.getSize().getWidth() - WIDTH_SEPARATION)
            / (CHILD_WIDTH + WIDTH_SEPARATION);
        int counter = 0;
        int x = WIDTH_SEPARATION;
        int y = HEIGHT_SEPARATION;
        for (int i = 0; i < count; i++) {
          counter++;
          Component component = parent.getComponent(i);
          Dimension d = component.getPreferredSize();
          component.setBounds(x, y, (int) d.getWidth(), (int) d.getHeight());
          x += (int) d.getWidth() + WIDTH_SEPARATION;
          if (counter == line) {
            counter = 0;
            x = WIDTH_SEPARATION;
            y += (int) d.getHeight() + HEIGHT_SEPARATION;
          }
        }
      }

      @Override
      public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
      }

      @Override
      public Dimension preferredLayoutSize(Container parent) {
        int height = 0;
        int width = 0;
        if (parent.getComponentCount() > 0) {
          int line = ((int) container.getWidth() - WIDTH_SEPARATION)
              / (CHILD_WIDTH + WIDTH_SEPARATION);
          if (line == 0) {
            line = 2;
          }
          height = ((parent.getComponentCount() / line) + 1)
              * (CHILD_HEIGHT + HEIGHT_SEPARATION) + HEIGHT_SEPARATION;
        }
        return new Dimension(width, height);
      }

      @Override
      public void removeLayoutComponent(Component parent) {
        // nothing
      }

    });

    return panel;
  }

  @Override
  public JPanel getContainer() {
    return this.container;
  }

  /**
   * Stops all threads running.
   */
  public void shutdownAll() {
    // shutdown the executor to cancel miniatures creations
    this.executor.shutdownNow();
  }

}
