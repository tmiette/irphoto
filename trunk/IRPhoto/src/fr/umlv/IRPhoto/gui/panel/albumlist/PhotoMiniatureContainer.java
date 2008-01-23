package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModel;

public class PhotoMiniatureContainer implements ContainerInitializer {

  private final PhotoModel model;
  private final Photo photo;
  private final JPanel container;

  // Miniature default dimension
  public static final Dimension DEFAULT_MINIATURE_DIMENSION = new Dimension(96,
      96);
  private static final int MAX_CHAR_NAME_LENGTH = 12;

  public PhotoMiniatureContainer(Photo photo, PhotoModel model) {

    this.model = model;
    this.photo = photo;

    final JLabel name = new JLabel(photo.getName());
    if (photo.getName().length() > MAX_CHAR_NAME_LENGTH) {
      name.setText(photo.getName().substring(0, MAX_CHAR_NAME_LENGTH) + "...");
    }
    name.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.container = new JPanel(null);
    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    this.container.setToolTipText(photo.getName());

    ImageIcon icon = photo.getImageIcon();
    double ratio = icon.getIconWidth() / DEFAULT_MINIATURE_DIMENSION.getWidth();
    int w = (int) (icon.getIconWidth() / ratio);
    int h = (int) (icon.getIconHeight() / ratio);
    final ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon
        .getImage(), w, h));

    final JLabel miniature = new JLabel();
    miniature.setMaximumSize(DEFAULT_MINIATURE_DIMENSION);
    miniature.setIcon(thumbnailIcon);
    miniature.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.container.add(miniature);
    this.container.add(name);

    this.container.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        PhotoMiniatureContainer.this.model
            .selectPhoto(PhotoMiniatureContainer.this.photo);
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        container.setBackground(GraphicalConstants.ROLLOVER_BACKGROUND);

      }

      @Override
      public void mouseExited(MouseEvent e) {
        container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
      }
    });

  }

  /**
   * Resizes an image using a Graphics2D object backed by a BufferedImage.
   * 
   * @param srcImg -
   *            source image to scale
   * @param w -
   *            desired width
   * @param h -
   *            desired height
   * @return - the new resized image
   */
  private static Image getScaledImage(Image srcImg, int w, int h) {
    BufferedImage resizedImg = new BufferedImage(
        DEFAULT_MINIATURE_DIMENSION.width, DEFAULT_MINIATURE_DIMENSION.height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    int x = DEFAULT_MINIATURE_DIMENSION.width - w;
    int y = DEFAULT_MINIATURE_DIMENSION.height - h;
    g2.drawImage(srcImg, x / 2, y / 2, w, h, null);
    g2.dispose();
    return resizedImg;
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }
}
