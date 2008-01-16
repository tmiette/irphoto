package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class AlbumDetailContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private final PhotoListContainer photoListView;
  private JLabel albumTitle;

  public AlbumDetailContainer(Album album) {

    // Contains panel with photos and search & sort buttons
    this.photoListView = createPhotoListPanel(album);

    this.mainPanel = new JPanel(null);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.mainPanel.add(createTitlePanel(album.getName()));
    this.mainPanel.add(this.photoListView.getComponent());
    this.mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  public void renameAlbum(Album album, String newName) {
    this.albumTitle.setText(newName);
  }

  public void addPhoto(Photo photo) {
    this.photoListView.addPhoto(photo);
  }

  private PhotoListContainer createPhotoListPanel(Album album) {
    final PhotoListContainer view = new PhotoListContainer(album);
    view.getComponent().setVisible(false);
    view.getComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
    return view;
  }

  private JPanel createTitlePanel(String title) {
    final JPanel jp = new JPanel(new BorderLayout());
    jp.setAlignmentX(Component.LEFT_ALIGNMENT);

    this.albumTitle = new JLabel(title);
    jp.add(this.albumTitle, BorderLayout.WEST);

    final JButton showPhotoList = createShowPhotoListButton();
    jp.add(showPhotoList, BorderLayout.EAST);

    return jp;
  }

  private JButton createShowPhotoListButton() {
    final JButton b = new JButton("->");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AlbumDetailContainer.this.photoListView.getComponent().setVisible(
            !AlbumDetailContainer.this.photoListView.getComponent().isVisible());
      }
    });
    return b;
  }

  @Override
  public JComponent getComponent() {
    return this.mainPanel;
  }

}