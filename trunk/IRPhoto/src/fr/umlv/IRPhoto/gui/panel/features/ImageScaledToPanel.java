package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

class ImageScaledToPanel extends JPanel {

  private static final long serialVersionUID = 2334026507944839069L;

  private Image image;

  public ImageScaledToPanel(Image image) {
    this.image = image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public Image getImage(Image image) {
    return this.image;
  }

  public void paintComponent(Graphics g) {

    super.paintComponent(g);

    if (image != null) {

      int maxX = (int) this.getSize().getWidth();
      int maxY = (int) this.getSize().getHeight();
      float x = image.getWidth(null);
      float y = image.getHeight(null);
      float facteur = 1.0f;

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

      int dx = (int) (x / facteur);
      int dy = (int) (y / facteur);
      int ty = (maxY / 2) - (dy / 2);
      int tx = (maxX / 2) - (dx / 2);
      g.drawImage(image, tx, ty, dx, dy, this);

    }

  }

}