package fr.umlv.IRPhoto.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * 
 * This class defines a default layout manager for a layered pane container.
 * This layout manager adapts the size of the layered pane with the maximum size
 * of contained components.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class DefaultLayeredPaneLayoutManager implements LayoutManager {

  @Override
  public void addLayoutComponent(String name, Component parent) {
    // do nothing
  }

  @Override
  public void layoutContainer(Container parent) {
    int count = parent.getComponentCount();
    for (int i = 0; i < count; i++) {
      Component component = parent.getComponent(i);
      component.setBounds(0, 0, parent.getWidth(), parent.getHeight());
    }
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return preferredLayoutSize(parent);
  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
    int width = 0;
    int height = 0;
    int count = parent.getComponentCount();
    for (int i = 0; i < count; i++) {
      Component c = parent.getComponent(i);
      Dimension preferred = c.getPreferredSize();

      if (preferred.width > width) {
        width = preferred.width;
      }
      if (preferred.height > height) {
        height = preferred.height;
      }

    }
    return new Dimension(width, height);
  }

  @Override
  public void removeLayoutComponent(Component parent) {
    // do nothing
  }

}
