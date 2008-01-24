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
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoSortModel;

public class AlbumDetailContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private final PhotoListContainer photoListView;
  private JLabel albumTitle;
  private final Album album;

  public AlbumDetailContainer(Album album, final AlbumModel albumModel,
      PhotoSortModel photoSortModel) {
    this.album = album;

    // Contains panel with photos and search & sort buttons
    this.photoListView = createPhotoListPanel(album, albumModel, photoSortModel);

    this.mainPanel = new JPanel(null);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    final JPanel titlePanel = createTitlePanel(album.getName());
    this.mainPanel.add(titlePanel);
    this.mainPanel.add(this.photoListView.getContainer());
    this.mainPanel.setBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.RAISED));
    this.mainPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
          albumModel.selectAlbum(AlbumDetailContainer.this.album);
        }

      }
    });

  }

  public void renameAlbum(Album album, String newName) {
    this.albumTitle.setText(newName);
  }

  public void addPhoto(Photo photo) {
    this.photoListView.addPhoto(photo);
  }

  private PhotoListContainer createPhotoListPanel(Album album,
      AlbumModel albumModel, PhotoSortModel photoSortModel) {
    final PhotoListContainer view = new PhotoListContainer(album, albumModel,
        photoSortModel);
    view.getContainer().setVisible(false);
    view.getContainer().setAlignmentX(Component.LEFT_ALIGNMENT);
    return view;
  }

  private JPanel createTitlePanel(String title) {
    final JPanel jp = new JPanel(null);
    jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
    jp.setAlignmentX(Component.LEFT_ALIGNMENT);
    jp.setBackground(GraphicalConstants.BLUE);

    this.albumTitle = new JLabel(title);
    jp.add(Box.createHorizontalStrut(10));
    jp.add(this.albumTitle);
    jp.add(Box.createHorizontalGlue());
    jp.add(createShowPhotoListButton());

    return jp;
  }

  private JLabel createShowPhotoListButton() {
    final JLabel l = new JLabel(IconFactory.getIcon("next-20x20.png"));
    l.setToolTipText("Show/hide photos of this album.");
    l.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        AlbumDetailContainer.this.photoListView.getContainer()
            .setVisible(
                !AlbumDetailContainer.this.photoListView.getContainer()
                    .isVisible());
        if (l.getIcon() == IconFactory.getIcon("down-20x20.png")) {
          l.setIcon(IconFactory.getIcon("next-20x20.png"));
        } else {
          l.setIcon(IconFactory.getIcon("down-20x20.png"));
        }
      }
    });
    return l;
  }

  @Override
  public JComponent getContainer() {
    return this.mainPanel;
  }

}
