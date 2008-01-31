/**
 * 
 */
package fr.umlv.IRPhoto.util;

import java.awt.Color;
import java.awt.Font;

/**
 * 
 * Interface with only graphical constants to define user interface parameters.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface GraphicalConstants {

  /**
   * Default foreground color for components.
   */
  public static final Color BLUE = new Color(98, 151, 200);

  /**
   * Default font.
   */
  public static final Font BOLD_FONT = new Font(null, Font.BOLD, 12);

  /**
   * Default background color for components.
   */
  public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

  /**
   * Color of non-value labels.
   */
  public static final Color DEFAULT_INFOS_LABEL_COLOR = Color.GRAY;

  /**
   * Color of roll over effect for components.
   */
  public static final Color ROLLOVER_BACKGROUND = new Color(0, 0, 205, 70);
}
