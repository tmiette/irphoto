package fr.umlv.IRPhoto.gui.panel.album;

import fr.umlv.IRPhoto.album.Photo;

public interface PhotoSelectionModel {

  public Photo getSelectedPhoto();
  
  public void selectPhoto(Photo photo);
  
  public void addPhotoSelectionListener(PhotoSelectionListener listener);

}
