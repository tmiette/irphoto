package fr.umlv.IRPhoto.gui;

import javax.swing.JComponent;

/**
 * 
 * This interface defines a component of graphical user interface which contains
 * other components.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface ContainerInitializer {

  /**
   * Returns the container component.
   * 
   * @return the container component.
   */
  public JComponent getContainer();

}
