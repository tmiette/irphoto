package fr.umlv.IRPhoto.gui.view.features;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * 
 * This class represents a panel which draw an image in proportion of the size
 * of its parent container.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class ImageScaledPanel extends JPanel {

  private static final long serialVersionUID = 2334026507944839069L;

  // the image to draw
  private Image image;

  /**
   * Constructor of the image panel.
   * 
   * @param image
   *            the image to draw.
   */
  public ImageScaledPanel(Image image) {
    this.image = image;
  }

  /**
   * Sets the new image to draw.
   * 
   * @param image
   *            the new image.
   */
  public void setImage(Image image) {
    this.image = image;
  }

  /**
   * Returns the image drawn.
   * 
   * @return the image drawn.
   */
  public Image getImage() {
    return this.image;
  }

  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);

    // redefines the paint method
    if (image != null) {

      // initialize the variable
      int maxX = (int) this.getSize().getWidth();
      int maxY = (int) this.getSize().getHeight();
      float x = image.getWidth(null);
      float y = image.getHeight(null);
      float facteur = 1.0f;

      // perform the proportion factor calculation
      if (x > y) {
        if (x > maxX) {
          facteur = x / maxX;
          if ((int) (y / facteur) > maxY) {
            facteur = y / maxY;
          }
        }
      } else {
        if (y > maxY) {
          facteur = y / maxY;
          if ((int) (x / facteur) > maxX) {
            facteur = x / maxX;
          }
        }
      }

      // draw the image with depending on the proportion factor
      int dx = (int) (x / facteur);
      int dy = (int) (y / facteur);
      int ty = (maxY / 2) - (dy / 2);
      int tx = (maxX / 2) - (dx / 2);
      g.drawImage(image, tx, ty, dx, dy, this);

    }

  }

}