/**
 * 
 */
package fr.umlv.IRPhoto.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * 
 * Class with static methods to perform different operations on images and
 * icons.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class ImageUtil {

  /**
   * Returns a scaled image from a source image depending on a container
   * dimension. The scaled instance is centered in the container.
   * 
   * @param srcImg
   *            image to scale.
   * @param containerWidth
   *            container width containing scaled image.
   * @param containerHeight
   *            container height containing scaled image.
   * @return scaled image.
   */
  public static Image getScaledImage(Image srcImg, int containerWidth,
      int containerHeight) {

    BufferedImage resizedImg = new BufferedImage(containerWidth,
        containerHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    // performs ratio calculation
    double imgWidth = srcImg.getWidth(null);
    double imgHeight = srcImg.getHeight(null);
    double ratio = imgWidth / containerWidth;
    if (imgHeight > imgWidth) {
      ratio = imgHeight / containerHeight;
    }
    if (imgWidth < containerWidth && imgHeight < containerHeight) {
      ratio = 1.0;
    }

    // draws scaled image
    int w = (int) (imgWidth / ratio);
    int h = (int) (imgHeight / ratio);
    int x = containerWidth - w;
    int y = containerHeight - h;
    g2.drawImage(srcImg, x / 2, y / 2, w, h, null);
    g2.dispose();
    return resizedImg;
  }

}
