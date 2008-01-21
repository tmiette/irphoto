package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.awt.Color;
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
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModel;

public class PhotoMiniatureContainer implements ContainerInitializer {

  private final PhotoSelectionModel model;
  private final Photo photo;
  private final JPanel mainPanel;

  // Miniature default dimension
  public static final Dimension DEFAULT_MINIATURE_DIMENSION = new Dimension(96,
      96);
  private static final int MAX_CHAR_NAME_LENGTH = 12;

  public PhotoMiniatureContainer(Photo photo, PhotoSelectionModel model) {

    this.model = model;
    this.photo = photo;

    final JLabel name = new JLabel(photo.getName());
    if (photo.getName().length() > MAX_CHAR_NAME_LENGTH) {
      name.setText(photo.getName().substring(0, MAX_CHAR_NAME_LENGTH) + "...");
    }
    name.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.mainPanel = new JPanel(null);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.mainPanel.setBackground(Color.WHITE);
    this.mainPanel.setToolTipText(photo.getName());

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

    this.mainPanel.add(miniature);
    this.mainPanel.add(name);

    this.mainPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        PhotoMiniatureContainer.this.model
            .selectPhoto(PhotoMiniatureContainer.this.photo);
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        mainPanel.setBackground(new Color(0, 0, 205, 70));

      }

      @Override
      public void mouseExited(MouseEvent e) {
        mainPanel.setBackground(Color.WHITE);
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
  public JComponent getComponent() {
    return this.mainPanel;
  }
}
