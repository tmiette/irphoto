/**
 * 
 */
package fr.umlv.IRPhoto.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class ImageUtil {

  /**
   * Gets an image with specific width and height at center in container with
   * giver width and height.
   * 
   * @param srcImg image to scale
   * @param width image width requested
   * @param height image height requested
   * @param containerWidth container width containing scaled image
   * @param containerHeight container height containing scaled image
   * @return image scaled
   */
  public static Image getScaledImage(Image srcImg, int width, int height,
      int containerWidth, int containerHeight) {
    BufferedImage resizedImg = new BufferedImage(containerWidth,
        containerHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    int x = containerWidth - width;
    int y = containerHeight - height;
    g2.drawImage(srcImg, x / 2, y / 2, width, height, null);
    g2.dispose();
    return resizedImg;
  }
}
