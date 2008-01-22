package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSortModel;

public class AlbumDetailContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private final PhotoListContainer photoListView;
  private JLabel albumTitle;
  private final Album album;

  public AlbumDetailContainer(Album album,
      final AlbumSelectionModel albumSelectionModel,
      PhotoSortModel photoSortModel) {
    this.album = album;

    // Contains panel with photos and search & sort buttons
    this.photoListView = createPhotoListPanel(album, photoSortModel);

    this.mainPanel = new JPanel(null);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    final JPanel title = createTitlePanel(album.getName());
    this.mainPanel.add(title);
    this.mainPanel.add(this.photoListView.getComponent());
    this.mainPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    this.mainPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
          albumSelectionModel.selectAlbum(AlbumDetailContainer.this.album);
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
      PhotoSortModel photoSortModel) {
    final PhotoListContainer view = new PhotoListContainer(album,
        photoSortModel);
    view.getComponent().setVisible(false);
    view.getComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
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

  private JButton createShowPhotoListButton() {
    final JButton b = new JButton("->");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AlbumDetailContainer.this.photoListView.getComponent()
            .setVisible(
                !AlbumDetailContainer.this.photoListView.getComponent()
                    .isVisible());
      }
    });
    return b;
  }

  @Override
  public JComponent getComponent() {
    return this.mainPanel;
  }

}
