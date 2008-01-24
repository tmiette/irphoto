package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;

public class PhotoMiniatureContainer implements ContainerInitializer {

  private final Photo photo;
  private final JPanel container;

  // Miniature default dimension
  public static final Dimension DEFAULT_MINIATURE_DIMENSION = new Dimension(96,
      96);
  private static final int MAX_CHAR_NAME_LENGTH = 12;

  public PhotoMiniatureContainer(Photo photo, final AlbumModel model) {

    this.photo = photo;

    final JLabel name = new JLabel(photo.getNameWithoutExtension());
    if (photo.getNameWithoutExtension().length() > MAX_CHAR_NAME_LENGTH) {
      name.setText(photo.getNameWithoutExtension().substring(0, MAX_CHAR_NAME_LENGTH) + "...");
    }
    name.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.container = new JPanel(null);
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    this.container.setToolTipText(photo.getNameWithoutExtension());

    final ImageIcon thumbnailIcon = new ImageIcon(photo.getScaledInstance());
    final JLabel miniature = new JLabel();
    miniature.setMaximumSize(DEFAULT_MINIATURE_DIMENSION);
    miniature.setIcon(thumbnailIcon);
    miniature.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.container.add(miniature);
    this.container.add(name);

    this.container.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        model.selectPhoto(PhotoMiniatureContainer.this.photo);
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        container.setBackground(GraphicalConstants.ROLLOVER_BACKGROUND);

      }

      @Override
      public void mouseExited(MouseEvent e) {
        container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
      }
    });

  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }
}
