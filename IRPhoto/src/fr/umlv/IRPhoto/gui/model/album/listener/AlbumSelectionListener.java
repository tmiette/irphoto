package fr.umlv.IRPhoto.gui.model.album.listener;

import fr.umlv.IRPhoto.album.Album;

/**
 * This interface defines a listener which is notified each time an album is
 * selected.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public interface AlbumSelectionListener {

  /**
   * This method is called when an album is selected.
   * 
   * @param album
   *            the selected album.
   */
  public void albumSelected(Album album);

}
