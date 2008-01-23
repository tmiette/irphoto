package fr.umlv.IRPhoto.gui.panel.model.photo;

import java.util.ArrayList;

import fr.umlv.IRPhoto.album.Photo;

public class PhotoModelImpl implements PhotoModel {

  private Photo selectedPhoto;
  private final ArrayList<PhotoSelectionListener> selectionListeners;
  private final ArrayList<PhotoUpdateListener> updateListeners;

  public PhotoModelImpl() {
    this.selectionListeners = new ArrayList<PhotoSelectionListener>();
    this.updateListeners = new ArrayList<PhotoUpdateListener>();
  }

  @Override
  public void addPhotoSelectionListener(PhotoSelectionListener listener) {
    this.selectionListeners.add(listener);
  }

  @Override
  public Photo getSelectedPhoto() {
    return this.selectedPhoto;
  }

  @Override
  public void selectPhoto(Photo photo) {
    this.selectedPhoto = photo;
    this.firePhotoSelected(photo);
  }

  protected void firePhotoSelected(Photo photo) {
    for (PhotoSelectionListener listener : this.selectionListeners) {
      listener.photoSelected(photo);
    }
  }

  @Override
  public void addPhotoUpdatedListener(PhotoUpdateListener photoUpdatedListener) {
    this.updateListeners.add(photoUpdatedListener);
  }

  @Override
  public void nameUpdated(Photo photo) {
    this.fireNameUpdated(photo);
  }

  @Override
  public void geopositionUpdated(Photo photo) {
    this.fireGeopositionUpdated(photo);
  }

  protected void fireNameUpdated(Photo photo) {
    for (PhotoUpdateListener listener : this.updateListeners) {
      listener.nameUpdated(photo);
    }
  }

  protected void fireGeopositionUpdated(Photo photo) {
    for (PhotoUpdateListener listener : this.updateListeners) {
      listener.geoppositionUpdated(photo);
    }
  }

}
