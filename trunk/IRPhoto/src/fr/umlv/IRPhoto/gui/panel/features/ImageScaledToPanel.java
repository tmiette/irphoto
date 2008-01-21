package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

class ImageScaledToPanel extends JPanel {

  private static final long serialVersionUID = 2334026507944839069L;

  Image image;

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

      /*
       * int height = this.getSize().height; int width = this.getSize().width;
       * 
       * double ratioW = (double) image.getWidth(null) / (double) width; double
       * ratioH = (double) image.getHeight(null) / (double) height; double
       * ratioI = (double) image.getWidth(null) / (double)
       * image.getHeight(null); double ratioC = (double) width / (double)
       * height;
       * 
       * if (ratioC <= 1) { height = (int) (width / ratioI); } else { width =
       * (int) (height * ratioI); } g.drawImage(image, 0, 0, width, height,
       * this);
       */

      int maxX = (int) this.getParent().getSize().getWidth();
      int maxY = (int) this.getParent().getSize().getHeight();
      float x = image.getWidth(null);
      float y = image.getHeight(null);
      float facteur = 1.0f;

      if (x > y) {
        if (x > maxX) {
          facteur = x / maxX;
        }
      } else {
        if (y > maxY) {
          facteur = y / maxY;
        }
      }

      int dx = (int) (x / facteur);
      int dy = (int) (y / facteur);
      
      // int ty = (maxY / 2) - (dy / 2);
      // int tx = (maxX / 2) - (dx / 2);
      g.drawImage(image, 0, 0, dx, dy, this);

      this.setBackground(Color.WHITE);

    }

  }

}