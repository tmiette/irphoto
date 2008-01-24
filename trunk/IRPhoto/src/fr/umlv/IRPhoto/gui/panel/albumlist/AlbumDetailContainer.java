package fr.umlv.IRPhoto.gui.panel.albumlist;

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
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortModel;

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
  private final JPanel mainPanel;

  // panel with the miniatures
  private final PhotoListContainer photoList;

  /**
   * Constructor of the container.
   * 
   * @param album
   *            the album to display.
   * @param albumModel
   *            the album model.
   * @param photoSortModel
   *            the model to sort the photos.
   */
  public AlbumDetailContainer(final Album album, final AlbumModel albumModel,
      PhotoSortModel photoSortModel) {

    // initialize the panel with the miniatures
    this.photoList = new PhotoListContainer(album, albumModel, photoSortModel);
    this.photoList.getContainer().setVisible(false);
    this.photoList.getContainer().setAlignmentX(Component.LEFT_ALIGNMENT);

    // initialize the label with the name of album
    final JLabel albumNameLabel = new JLabel(album.getName());

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
    titlePanel.add(Box.createHorizontalGlue());
    titlePanel.add(showButton);

    // initialize the main container
    this.mainPanel = new JPanel(null);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.mainPanel.add(titlePanel);
    this.mainPanel.add(this.photoList.getContainer());
    this.mainPanel.setBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.RAISED));

    // add a listener to select the album
    this.mainPanel.addMouseListener(new MouseAdapter() {
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
      public void albumRenamed(Album album2, String newName) {
        if (album.equals(album2)) {
          // update the label with the name of the album
          albumNameLabel.setText(newName);
        }
      }

      @Override
      public void photoAdded(Album album, Photo photo) {
        // do nothing
      }
    });

  }

  public void addPhoto(Photo photo) {
    this.photoList.addPhoto(photo);
  }

  @Override
  public JComponent getContainer() {
    return this.mainPanel;
  }

}
