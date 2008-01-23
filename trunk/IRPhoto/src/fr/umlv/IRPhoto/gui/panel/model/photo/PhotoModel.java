package fr.umlv.IRPhoto.gui.panel.model.photo;

import fr.umlv.IRPhoto.album.Photo;

public interface PhotoModel {

  public Photo getSelectedPhoto();
  
  public void selectPhoto(Photo photo);
  
  public void nameUpdated(Photo photo);
  
  public void geopositionUpdated(Photo photo);
  
  public void addPhotoSelectionListener(PhotoSelectionListener listener);

  public void addPhotoUpdatedListener(PhotoUpdateListener photoUpdatedListener);
  
}
