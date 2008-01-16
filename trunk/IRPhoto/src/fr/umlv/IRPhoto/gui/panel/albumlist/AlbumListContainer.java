package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModel;

public class AlbumListContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private final AlbumModel model;
  private final GridBagConstraints constraints;
  private final HashMap<Album, AlbumDetailContainer> albumsDetailContainers;
  private final JPanel endPanel;

  public AlbumListContainer(AlbumModel albumModel,
      PhotoSelectionModel selectionModel) {
    this.albumsDetailContainers = new HashMap<Album, AlbumDetailContainer>();
    this.model = albumModel;
    this.model.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        AlbumListContainer.this.addAlbum(album);
      }

      @Override
      public void albumRemoved(Album album) {
        AlbumListContainer.this.removeAlbum(album);
      }

      @Override
      public void albumRenamed(Album album, String newName) {
        AlbumListContainer.this.renameAlbum(album, newName);
      }

      @Override
      public void photoAdded(Album album, Photo photo) {
        AlbumListContainer.this.addPhoto(album, photo);
      }

    });

    this.constraints = new GridBagConstraints();
    this.constraints.fill = GridBagConstraints.HORIZONTAL;
    this.constraints.anchor = GridBagConstraints.PAGE_START;
    this.constraints.gridy = 0;
    this.constraints.weightx = 0.5;
    this.constraints.gridx = 0;

    this.endPanel = new JPanel(null);
    this.mainPanel = createAlbumListPanel();
  }

  private JPanel createAlbumListPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.MAGENTA));

    for (Album album : this.model.getAlbums()) {
      this.addAlbum(album);
    }

    return panel;
  }

  private void addEndPanel() {
    this.constraints.weighty = 1;
    this.mainPanel.add(this.endPanel, this.constraints);
    this.constraints.weighty = 0;
  }

  private void addAlbum(Album album) {
    this.constraints.gridy++;

    final AlbumDetailContainer ta = new AlbumDetailContainer(album);
    this.albumsDetailContainers.put(album, ta);
    this.mainPanel.add(ta.getComponent(), this.constraints);
    this.addEndPanel();
    this.mainPanel.revalidate();
  }

  private void removeAlbum(Album album) {
    AlbumDetailContainer c = this.albumsDetailContainers.remove(album);
    if (c != null) {
      this.mainPanel.remove(c.getComponent());
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
  public JComponent getComponent() {
    return this.mainPanel;
  }

}
