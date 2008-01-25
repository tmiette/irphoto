package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumListener;

/**
 * 
 * This container represents all the albums as a list.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class AlbumListContainer implements ContainerInitializer {

  // album model
  private final AlbumModel albumModel;

  // map the album with its container
  private final HashMap<Album, AlbumDetailContainer> albumsDetailContainers;

  // constraints
  private final GridBagConstraints constraints;

  // container
  private final JPanel container;

  // empty panel
  private final JPanel endPanel;

  /**
   * Constructor of the container.
   * 
   * @param albumModel
   *            the album model.
   */
  public AlbumListContainer(final AlbumModel albumModel) {

    this.albumsDetailContainers = new HashMap<Album, AlbumDetailContainer>();

    // initialize constraints
    this.constraints = new GridBagConstraints();
    this.constraints.fill = GridBagConstraints.HORIZONTAL;
    this.constraints.anchor = GridBagConstraints.PAGE_START;
    this.constraints.gridy = 0;
    this.constraints.weightx = 0.5;
    this.constraints.gridx = 0;
    this.constraints.insets = new Insets(5, 5, 5, 5);

    // initialize empty panel
    this.endPanel = new JPanel(null);

    // initialize main container
    this.container = new JPanel(new GridBagLayout());
    this.container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);

    this.albumModel = albumModel;
    // listen the albums adding and removing
    this.albumModel.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        addAlbum(album);
      }

      @Override
      public void albumRemoved(Album album) {
        AlbumDetailContainer c = albumsDetailContainers.remove(album);
        if (c != null) {
          container.remove(c.getContainer());
          constraints.weighty = 1;
          container.add(endPanel, constraints);
          constraints.weighty = 0;
          container.revalidate();
          container.repaint();
        }
      }
    });

    // add the album already contained in the model
    for (Album album : this.albumModel.getAlbums()) {
      this.addAlbum(album);
    }

  }

  /**
   * Add a new album panel to this container.
   * 
   * @param album
   *            the new album.
   */
  private void addAlbum(Album album) {
    this.constraints.gridy++;
    final AlbumDetailContainer ta = new AlbumDetailContainer(album,
        this.albumModel);
    this.albumsDetailContainers.put(album, ta);
    this.container.add(ta.getContainer(), this.constraints);
    this.constraints.weighty = 1;
    this.container.add(this.endPanel, this.constraints);
    this.constraints.weighty = 0;
    this.container.revalidate();
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

}
