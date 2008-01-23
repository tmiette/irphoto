package fr.umlv.IRPhoto.gui;

import javax.swing.JComponent;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.panel.MainContainer;
import fr.umlv.IRPhoto.gui.panel.TabbedPaneContainer;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumListContainer;
import fr.umlv.IRPhoto.gui.panel.albumlist.PhotoMiniatureContainer;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeContainer;
import fr.umlv.IRPhoto.gui.panel.features.FeaturesContainer;
import fr.umlv.IRPhoto.gui.panel.map.MapContainer;
import fr.umlv.IRPhoto.gui.panel.map.PhotoWithoutGPListContainer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModelImpl;

public class ContainerFactory {

  private static AlbumModel albumModel;
  private static final PhotoModel photoModel = new PhotoModelImpl();

  public static JComponent createMainContainer(AlbumModel model) {
    albumModel = model;
    return new MainContainer().getComponent();
  }

  public static JComponent createAlbumTreeContainer() {
    return new AlbumTreeContainer(albumModel).getComponent();
  }

  public static JComponent createFeaturesContainer() {
    return new FeaturesContainer(photoModel)
        .getComponent();
  }

  public static JComponent createTabbedContainer() {
    return new TabbedPaneContainer().getComponent();
  }

  public static JComponent createMapContainer() {
    return new MapContainer(albumModel, photoModel).getComponent();
  }

  public static JComponent createAlbumListContainer() {
    return new AlbumListContainer(albumModel).getComponent();
  }

  public static JComponent createPhotoWithoutGPListContainer() {
    return new PhotoWithoutGPListContainer(albumModel, photoModel).getComponent();
  }

  public static PhotoMiniatureContainer createPhotoMiniatureContainer(
      Photo photo) {
    return new PhotoMiniatureContainer(photo, photoModel);
  }

}
