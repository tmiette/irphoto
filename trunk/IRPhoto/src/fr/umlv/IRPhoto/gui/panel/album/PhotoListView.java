package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public class PhotoListView {

  private final Album album;
  // Contains search text field and sort buttons
  private final JPanel topPanel;
  // Contains photos
  private final JPanel photoListPanel;
  // General panel
  private final JPanel panel;

  public PhotoListView(Album album) {
    this.album = album;

    this.topPanel = createTopPanel();

    this.photoListPanel = createPhotoListPanel();
    this.addPhotos(this.album.getSortedPhotos(Photo.NAME_ORDER),
        this.photoListPanel);

    this.panel = new JPanel(new BorderLayout());
    this.panel.add(this.topPanel, BorderLayout.NORTH);
    this.panel.add(this.photoListPanel, BorderLayout.CENTER);
  }

  private JPanel createPhotoListPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(Color.red);
    return panel;
  }

  private JPanel createTopPanel() {
    JPanel panel = new JPanel(null);
    panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 3));

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

  private boolean addPhotos(List<Photo> photos, JPanel panel) {
    for (Photo photo : photos) {
      panel.add(PhotoPreview.getPanel(photo));
    }
    return true;
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

  public JPanel getPanel() {
    return this.panel;
  }

}
