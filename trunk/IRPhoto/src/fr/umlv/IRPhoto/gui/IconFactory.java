package fr.umlv.IRPhoto.gui;

import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconFactory {

  private static final HashMap<String, Icon> iconsLoaded = new HashMap<String, Icon>();

  public static Icon getIcon(String iconName) {
    Icon icon = iconsLoaded.get(iconName);
    if (icon == null) {
      icon = new ImageIcon(IconFactory.class.getResource("/icons/" + iconName));
      iconsLoaded.put(iconName, icon);
    }
    return icon;
  }

}
