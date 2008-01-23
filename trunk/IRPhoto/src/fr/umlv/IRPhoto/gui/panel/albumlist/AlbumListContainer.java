package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumUpdateListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortModelImpl;

public class AlbumListContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private final AlbumModel albumModel;
  private final PhotoModel photoModel;
  private final GridBagConstraints constraints;
  private final HashMap<Album, AlbumDetailContainer> albumsDetailContainers;
  private final JPanel endPanel;

  public AlbumListContainer(AlbumModel albumModel, PhotoModel photoModel) {
    this.albumsDetailContainers = new HashMap<Album, AlbumDetailContainer>();
    this.photoModel = photoModel;
    this.albumModel = albumModel;
    this.albumModel.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        addAlbum(album);
      }

      @Override
      public void albumRemoved(Album album) {
        removeAlbum(album);
      }
    });
    this.albumModel.addAlbumUpdateListener(new AlbumUpdateListener() {
      @Override
      public void albumRenamed(Album album, String newName) {
        renameAlbum(album, newName);
      }

      @Override
      public void photoAdded(Album album, Photo photo) {
        addPhoto(album, photo);
      }
    });

    this.constraints = new GridBagConstraints();
    this.constraints.fill = GridBagConstraints.HORIZONTAL;
    this.constraints.anchor = GridBagConstraints.PAGE_START;
    this.constraints.gridy = 0;
    this.constraints.weightx = 0.5;
    this.constraints.gridx = 0;
    this.constraints.insets = new Insets(5, 5, 5, 5);

    this.endPanel = new JPanel(null);
    this.mainPanel = createAlbumListPanel();
    this.mainPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);

    for (Album album : this.albumModel.getAlbums()) {
      this.addAlbum(album);
    }

  }

  private JPanel createAlbumListPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    return panel;
  }

  private void addEndPanel() {
    this.constraints.weighty = 1;
    this.mainPanel.add(this.endPanel, this.constraints);
    this.constraints.weighty = 0;
  }

  private void addAlbum(Album album) {
    this.constraints.gridy++;

    final AlbumDetailContainer ta = new AlbumDetailContainer(album,
        this.albumModel, this.photoModel, new PhotoSortModelImpl());
    this.albumsDetailContainers.put(album, ta);
    this.mainPanel.add(ta.getContainer(), this.constraints);
    this.addEndPanel();
    this.mainPanel.revalidate();
  }

  private void removeAlbum(Album album) {
    AlbumDetailContainer c = this.albumsDetailContainers.remove(album);
    if (c != null) {
      this.mainPanel.remove(c.getContainer());
      this.addEndPanel();
      this.mainPanel.revalidate();
      this.mainPanel.repaint();
    }
  }

  private void renameAlbum(Album album, String newName) {
    AlbumDetailContainer c = this.albumsDetailContainers.get(album);
    if (c != null) {
      c.renameAlbum(album, newName);
    }
  }

  private void addPhoto(Album album, Photo photo) {
    AlbumDetailContainer c = this.albumsDetailContainers.get(album);
    if (c != null) {
      c.addPhoto(photo);
    }
  }

  @Override
  public JComponent getContainer() {
    return this.mainPanel;
  }

}
