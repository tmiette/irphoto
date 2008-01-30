package fr.umlv.IRPhoto.util;

import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 * Class which enable to load and get icons. Each icon is loaded and stored the
 * first it is needed. After this, the same icon is returned for other purposes.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class IconFactory {

  // map to store icons
  private static final HashMap<String, Icon> iconsLoaded = new HashMap<String, Icon>();

  /**
   * 
   * Returns the icon with the specified name.
   * 
   * @param iconName
   *            the icon name with file extension.
   * @return the icon.
   */
  public static Icon getIcon(String iconName) {
    Icon icon = iconsLoaded.get(iconName);
    if (icon == null) {
      icon = new ImageIcon(IconFactory.class.getResource("/icons/" + iconName));
      iconsLoaded.put(iconName, icon);
    }
    return icon;
  }

}
