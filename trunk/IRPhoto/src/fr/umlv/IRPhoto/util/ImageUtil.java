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
   * Gets an image scaled to container dimension and puts it at center of this container.
   * 
   * @param srcImg image to scale
   * @param containerWidth container width containing scaled image
   * @param containerHeight container height containing scaled image
   * @return image scaled
   */
  public static Image getScaledImage(Image srcImg, int containerWidth, int containerHeight) {
    BufferedImage resizedImg = new BufferedImage(containerWidth,
        containerHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    double ratio = srcImg.getWidth(null) / containerWidth;
    int w = (int) (srcImg.getWidth(null) / ratio);
    int h = (int) (srcImg.getHeight(null) / ratio);
    int x = containerWidth - w;
    int y = containerHeight - h;
    g2.drawImage(srcImg, x / 2, y / 2, w, h, null);
    g2.dispose();
    return resizedImg;
  }
}
