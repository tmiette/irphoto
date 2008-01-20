package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSortListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSortModel;

public class PhotoListContainer implements ContainerInitializer {

  // Contains photos
  private JPanel photoListPanel;
  // General panel
  private final JPanel mainPanel;

  private final HashMap<Photo, PhotoMiniatureContainer> photoMiniatures;

  private final Album album;

  private final PhotoSortModel photoSortModel;

  public PhotoListContainer(Album album, PhotoSortModel photoSortModel) {
    this.album = album;
    this.photoSortModel = photoSortModel;
    this.photoSortModel.addPhotoSortListener(new PhotoSortListener() {
      @Override
      public void photoSorted(List<Photo> photos) {
        displayPhotoList(photos);
      }
    });

    this.photoMiniatures = new HashMap<Photo, PhotoMiniatureContainer>();

    this.photoListPanel = createPhotoListPanel();
    for (Photo photo : album.getPhotos()) {
      this.addPhoto(photo);
    }

    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(createTopPanel(), BorderLayout.NORTH);
    this.mainPanel.add(this.photoListPanel, BorderLayout.CENTER);
  }

  /**
   * Returns a panel containing search text field, alphabetic & type & name
   * buttons to sort photos list. Search text field filters photos which name
   * matches to text entered.
   * 
   * @return JPanel containing components
   */
  private JPanel createTopPanel() {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));
    // panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.add(Box.createHorizontalGlue());

    panel.add(new JLabel(IconFactory.getIcon("find-32x32.png")));

    // Text field used to search photo
    final JTextField textField = new JTextField(2);
    // textField.setHorizontalAlignment(JTextField.LEADING);
    textField.setColumns(20);
    panel.add(textField);

    textField.addCaretListener(new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        photoSortModel.matchPhoto(album, textField.getText());
      }
    });

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
    final JButton b = new JButton(IconFactory.getIcon("calendar-32x32.png"));
    b.setToolTipText("Sort photos by last modification date.");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        photoSortModel.sortPhoto(album,
            Photo.PHOTO_LAST_MODIFIED_DATE_COMPARATOR);
      }
    });
    return b;
  }

  private JButton createTypeSortButton() {
    final JButton b = new JButton(IconFactory.getIcon("calendar-32x32.png"));
    b.setToolTipText("Sort photos by type.");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        photoSortModel.sortPhoto(album, Photo.PHOTO_TYPE_COMPARATOR);
      }
    });
    return b;
  }

  private JButton createAlphaSortButton() {
    final JButton b = new JButton(IconFactory.getIcon("calendar-32x32.png"));
    b.setToolTipText("Sort photos by last name.");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        photoSortModel.sortPhoto(album, Photo.PHOTO_NAME_COMPARATOR);
      }
    });
    return b;
  }

  /**
   * Displays and adds a photos list to the panel. This function remove all
   * photos from this panel and adds new photos.
   * 
   * @param photos
   *            photos list to display in panel
   */
  private void displayPhotoList(List<Photo> photos) {
    photoListPanel.removeAll();
    for (Photo photo : photos) {
      PhotoMiniatureContainer photoContainer = photoMiniatures.get(photo);
      if (photoContainer != null) {
        photoListPanel.add(photoContainer.getComponent());
      }
    }
    photoListPanel.revalidate();
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
    this.photoListPanel.revalidate();
  }

  @Override
  public JPanel getComponent() {
    return this.mainPanel;
  }

}
