package fr.umlv.IRPhoto.gui.panel.album;

import java.util.ArrayList;

import fr.umlv.IRPhoto.album.Photo;

public class PhotoSelectionModelImpl implements PhotoSelectionModel {

  private Photo selectedPhoto;
  private final ArrayList<PhotoSelectionListener> listeners;

  public PhotoSelectionModelImpl() {
    this.listeners = new ArrayList<PhotoSelectionListener>();
  }

  @Override
  public void addPhotoSelectionListener(PhotoSelectionListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public Photo getSelectedPhoto(){
    return this.selectedPhoto;
  }
  
  @Override
  public void selectPhoto(Photo photo) {
    this.selectedPhoto = photo;
    this.firePhotoSelected(photo);
  }

  protected void firePhotoSelected(Photo photo) {
    for (PhotoSelectionListener listener : this.listeners) {
      listener.photoSelected(photo);
    }
  }

}
