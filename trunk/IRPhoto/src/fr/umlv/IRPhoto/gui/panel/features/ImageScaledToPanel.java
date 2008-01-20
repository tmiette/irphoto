package fr.umlv.IRPhoto.gui.panel.features;

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

      int height = this.getSize().height;

      int width = this.getSize().width;
      
      double ratioW = (double)image.getWidth(null) / (double)width;
      double ratioH = (double)image.getHeight(null) / (double)height;
      double ratioI = (double)image.getWidth(null) / (double)image.getHeight(null);
      double ratioC = (double)width / (double)height;
      
      System.out.println("height=" + height + " width=" + width);
      
      if (ratioC <= 1) {
        height = (int)(width / ratioI);
      } else {
        width = (int)(height * ratioI);
      }
      System.out.println("new    " + "height=" + height + " width=" + width);
      g.drawImage(image, 0, 0, width , height, this);

    }

  }
  

}