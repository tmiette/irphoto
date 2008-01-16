package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class PhotoListContainer implements ContainerInitializer {

  // Contains photos
  private JPanel photoListPanel;
  // General panel
  private final JPanel mainPanel;

  private final HashMap<Photo, PhotoMiniatureContainer> photoMiniatures;

  public PhotoListContainer(Album album) {
    this.photoMiniatures = new HashMap<Photo, PhotoMiniatureContainer>();

    this.photoListPanel = createPhotoListPanel();
    for (Photo photo : album.getSortedPhotos(Photo.NAME_ORDER)) {
      this.addPhoto(photo);
    }

    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(createTopPanel(), BorderLayout.NORTH);
    this.mainPanel.add(this.photoListPanel, BorderLayout.CENTER);
  }

  private JPanel createTopPanel() {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));

    final JTextField textField = new JTextField(2);
    textField.setHorizontalAlignment(JTextField.LEADING);
    textField.setColumns(20);
    panel.add(textField);

    final JButton alphaSortButton = createAlphaSortButton();
    panel.add(alphaSortButton);

    final JButton typeSortButton = createTypeSortButton();
    panel.add(typeSortButton);

    final JButton dateSortButton = createDateSortButton();
    panel.add(dateSortButton);

    return panel;
  }

  private JPanel createPhotoListPanel() {
    final JPanel panel = new JPanel();
    panel.setBackground(Color.red);
    return panel;
  }

  private JButton createDateSortButton() {
    final JButton b = new JButton("date");
    return b;
  }

  private JButton createTypeSortButton() {
    final JButton b = new JButton("type");
    return b;
  }

  private JButton createAlphaSortButton() {
    final JButton b = new JButton("alpha");
    return b;
  }

  public void addPhoto(Photo photo) {
    PhotoMiniatureContainer c = this.photoMiniatures.get(photo);
    if (c == null) {
      c = ContainerFactory.createPhotoMiniatureContainer(photo);
      this.photoListPanel.add(c.getComponent());
      this.photoMiniatures.put(photo, c);
    } else {
      this.photoListPanel.add(c.getComponent());
    }
    this.photoListPanel.validate();
  }

  @Override
  public JPanel getComponent() {
    return this.mainPanel;
  }

}
