package fr.umlv.IRPhoto.gui.panel.features;

import java.util.ArrayList;

import fr.umlv.IRPhoto.album.Photo;

public class PhotoSelectionModelImpl implements PhotoSelectionModel {

  private final ArrayList<PhotoSelectionListener> listeners;

  public PhotoSelectionModelImpl() {
    this.listeners = new ArrayList<PhotoSelectionListener>();
  }

  @Override
  public void addPhotoSelectionListener(PhotoSelectionListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void selectPhoto(Photo photo) {
    this.firePhotoSelected(photo);
  }

  protected void firePhotoSelected(Photo photo) {
    for (PhotoSelectionListener listener : this.listeners) {
      listener.photoSelected(photo);
    }
  }

}
