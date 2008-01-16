package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class PhotoListContainer implements ContainerInitializer {

  // Contains photos
  private JPanel photoListPanel;
  // General panel
  private final JPanel mainPanel;

  private final ArrayList<PhotoMiniatureContainer> photoPreviews;

  public PhotoListContainer(Album album) {
    this.photoPreviews = new ArrayList<PhotoMiniatureContainer>();

    this.photoListPanel = createPhotoListPanel();
    for(Photo photo:album.getSortedPhotos(Photo.NAME_ORDER)){
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
    final PhotoMiniatureContainer pp = new PhotoMiniatureContainer(photo);
    this.photoListPanel.add(pp.getComponent());
    this.photoPreviews.add(pp);
    this.photoListPanel.validate();
  }

  @Override
  public JPanel getComponent() {
    return this.mainPanel;
  }

}
