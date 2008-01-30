package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Component;
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
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.util.GraphicalConstants;

/**
 * This container represents a photo miniature with its name without file
 * extension.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class PhotoMiniatureContainer implements ContainerInitializer {

  // maximum number of characters displayed for the name of the photo
  private static final int MAX_CHAR_NAME_LENGTH = 12;

  // container
  private final JPanel container;

  /**
   * Constructor of the container.
   * 
   * @param photo
   *            the photo to display.
   * @param model
   *            the album model.
   */
  public PhotoMiniatureContainer(final Photo photo, final AlbumModel model) {

    // initialize the name label
    final JLabel nameLabel = new JLabel(photo.getNameWithoutExtension());
    if (photo.getNameWithoutExtension().length() > MAX_CHAR_NAME_LENGTH) {
      nameLabel.setText(photo.getNameWithoutExtension().substring(0,
          MAX_CHAR_NAME_LENGTH)
          + "...");
    }
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // initialize the miniature
    final JLabel miniature = new JLabel(
        new ImageIcon(photo.getScaledInstance()));
    miniature.setMaximumSize(Photo.DEFAULT_MINIATURE_DIMENSION);
    miniature.setAlignmentX(Component.CENTER_ALIGNMENT);

    // initialize the main container
    this.container = new JPanel(null);
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    this.container.setToolTipText(photo.getNameWithoutExtension());
    this.container.add(miniature);
    this.container.add(nameLabel);

    // add a mouse listener for the roll over effect
    this.container.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        model.selectPhoto(photo);
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
