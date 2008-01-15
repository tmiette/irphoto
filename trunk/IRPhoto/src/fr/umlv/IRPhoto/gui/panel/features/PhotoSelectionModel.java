package fr.umlv.IRPhoto.gui.panel.features;

import fr.umlv.IRPhoto.album.Photo;

public interface PhotoSelectionModel {

  public void selectPhoto(Photo photo);
  
  public void addPhotoSelectionListener(PhotoSelectionListener listener);

}
