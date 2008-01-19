package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

class ImageScaledToPanel extends JPanel {

   /**
    * 
    */
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

         g.drawImage(image, 0, 0, width, height, this);

      }

   }

}