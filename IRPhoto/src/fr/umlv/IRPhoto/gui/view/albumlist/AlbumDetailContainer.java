package fr.umlv.IRPhoto.gui.view.albumlist;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.view.ContainerInitializer;
import fr.umlv.IRPhoto.util.GraphicalConstants;
import fr.umlv.IRPhoto.util.IconFactory;

/**
 * 
 * This container represents an album with the miniatures of each photo.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class AlbumDetailContainer implements ContainerInitializer {

  // container
  private final JPanel container;

  // panel with the miniatures
  private final PhotoListContainer photoList;

  /**
   * Constructor of the container.
   * 
   * @param album
   *            the album to display.
   * @param albumModel
   *            the album model.
   */
  public AlbumDetailContainer(final Album album, final AlbumModel albumModel) {

    // initialize the panel with the miniatures
    this.photoList = new PhotoListContainer(album, albumModel);
    this.photoList.getContainer().setVisible(false);
    this.photoList.getContainer().setAlignmentX(Component.LEFT_ALIGNMENT);

    // initialize the label with the name of album
    final JLabel albumNameLabel = new JLabel(album.getName());
    // Label with the number of photos
    final JLabel numberLabel = new JLabel("(" + album.getPhotos().size() + ")");

    // initialize the button to show and hide the panel with miniatures
    final JLabel showButton = new JLabel(IconFactory.getIcon("next-20x20.png"));
    showButton.setToolTipText("Show/hide photos of this album.");
    // add the action listener
    showButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // show or hide the photos panel
        AlbumDetailContainer.this.photoList.getContainer().setVisible(
            !AlbumDetailContainer.this.photoList.getContainer().isVisible());
        if (showButton.getIcon() == IconFactory.getIcon("down-20x20.png")) {
          showButton.setIcon(IconFactory.getIcon("next-20x20.png"));
        } else {
          showButton.setIcon(IconFactory.getIcon("down-20x20.png"));
        }
      }
    });

    // initialize the header panel
    final JPanel titlePanel = new JPanel(null);
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
    titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    titlePanel.setBackground(GraphicalConstants.BLUE);
    titlePanel.add(Box.createHorizontalStrut(10));
    titlePanel.add(albumNameLabel);
    titlePanel.add(Box.createHorizontalStrut(3));
    titlePanel.add(numberLabel);
    titlePanel.add(Box.createHorizontalGlue());
    titlePanel.add(showButton);

    // initialize the main container
    this.container = new JPanel(null);
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(titlePanel);
    this.container.add(this.photoList.getContainer());
    this.container.setBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.RAISED));

    // add a listener to select the album
    this.container.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
          albumModel.selectAlbum(album);
        }

      }
    });

    // listen the update of album model
    albumModel.addAlbumUpdateListener(new AlbumUpdateListener() {
      @Override
      public void albumCleared(Album album2) {
        if (album.equals(album2)) {
          numberLabel.setText("(" + album.getPhotos().size() + ")");
        }
      }

      @Override
      public void albumRenamed(Album album2, String newName) {
        if (album.equals(album2)) {
          // update the label with the name of the album
          albumNameLabel.setText(newName);
        }
      }

      @Override
      public void photoAdded(Album album2, Photo photo) {
        if (album.equals(album2)) {
          numberLabel.setText("(" + photo.getAlbum().getPhotos().size() + ")");
        }
      }
    });

  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

  /**
   * Stops all threads running.
   */
  public void shutdownAll() {
    this.photoList.shutdownAll();
  }

}
