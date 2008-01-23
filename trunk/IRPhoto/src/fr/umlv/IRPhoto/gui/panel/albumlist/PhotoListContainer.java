package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;

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
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSortListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSortModel;

public class PhotoListContainer implements ContainerInitializer {

  // Contains photos
  private JPanel photoListPanel;
  // General panel
  private final JPanel mainPanel;
  private final JPanel topPanel;
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

    this.topPanel = createTopPanel();
    this.photoListPanel = createPhotoListPanel();
    for (Photo photo : album.getPhotos()) {
      this.addPhoto(photo);
    }

    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(this.topPanel, BorderLayout.NORTH);
    this.mainPanel.add(this.photoListPanel, BorderLayout.CENTER);

    this.mainPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        photoListPanel.revalidate();
      }
    });

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
    panel.setBackground(GraphicalConstants.BLUE);

    panel.add(new JLabel(IconFactory.getIcon("find-32x32.png")));

    // Text field used to filter photo
    final JTextField textField = new JTextField(15);
    panel.add(textField);

    textField.addCaretListener(new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        photoSortModel.matchPhoto(album, textField.getText());
      }
    });

    // Sorts buttons
    panel.add(createAlphaSortButton());
    panel.add(createTypeSortButton());
    panel.add(createDateSortButton());

    return panel;
  }

  private JPanel createPhotoListPanel() {
    final JPanel panel = new JPanel(null);
    panel.setBackground(Color.WHITE);

    panel.setLayout(new LayoutManager() {

      private static final int CHILD_WIDTH = 116;
      private static final int CHILD_HEIGHT = 131;
      private static final int WIDTH_SEPARATION = 2;
      private static final int HEIGHT_SEPARATION = 2;

      @Override
      public void addLayoutComponent(String name, Component parent) {
        // nothing
      }

      @Override
      public void removeLayoutComponent(Component parent) {
        // nothing
      }

      @Override
      public void layoutContainer(Container parent) {
        int count = parent.getComponentCount();
        int line = ((int) mainPanel.getSize().getWidth() - WIDTH_SEPARATION)
            / (CHILD_WIDTH + WIDTH_SEPARATION);
        int counter = 0;
        int x = WIDTH_SEPARATION;
        int y = HEIGHT_SEPARATION;
        for (int i = 0; i < count; i++) {
          counter++;
          Component component = parent.getComponent(i);
          Dimension d = component.getPreferredSize();
          component.setBounds(x, y, (int) d.getWidth(), (int) d.getHeight());
          x += (int) d.getWidth() + WIDTH_SEPARATION;
          if (counter == line) {
            counter = 0;
            x = WIDTH_SEPARATION;
            y += (int) d.getHeight() + HEIGHT_SEPARATION;
          }
        }
      }

      @Override
      public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
      }

      @Override
      public Dimension preferredLayoutSize(Container parent) {

        int height = 0;
        int width = 0;
        if (parent.getComponentCount() > 0) {
          int line = ((int) mainPanel.getWidth() - WIDTH_SEPARATION)
              / (CHILD_WIDTH + WIDTH_SEPARATION);
          if (line == 0) {
            line = 2;
          }
          height = ((parent.getComponentCount() / line) + 1)
              * (CHILD_HEIGHT + HEIGHT_SEPARATION) + HEIGHT_SEPARATION;
        }
        return new Dimension(width, height);
      }

    });

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
    final JButton b = new JButton(IconFactory.getIcon("type-2-2-32x32.png"));
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
    final JButton b = new JButton(IconFactory.getIcon("name-32x32.png"));
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
    photoListPanel.repaint();
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
