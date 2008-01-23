package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSortModelImpl;

public class AlbumListContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private final AlbumModel model;
  private final GridBagConstraints constraints;
  private final HashMap<Album, AlbumDetailContainer> albumsDetailContainers;
  private final JPanel endPanel;
  private final AlbumSelectionModel albumSelectionModel;

  public AlbumListContainer(AlbumModel albumModel,
      AlbumSelectionModel albumSelectionModel) {
    this.albumSelectionModel = albumSelectionModel;
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
    this.constraints.insets = new Insets(5,5,5,5);

    this.endPanel = new JPanel(null);
    this.endPanel.setBackground(Color.RED);
    this.endPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 10));
    this.mainPanel = createAlbumListPanel();
    this.mainPanel.setBackground(Color.WHITE);
    
    for (Album album : this.model.getAlbums()) {
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
        this.albumSelectionModel,new PhotoSortModelImpl());
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
