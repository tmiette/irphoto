/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.album;

import fr.umlv.IRPhoto.album.Photo;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 *
 */
public interface PhotoUpdatedListener {

  public void nameUpdated(Photo photo);
  public void geoppositionUpdated(Photo photo);
}
