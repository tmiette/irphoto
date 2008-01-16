package fr.umlv.IRPhoto.gui;

import javax.swing.JComponent;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.panel.MainContainer;
import fr.umlv.IRPhoto.gui.panel.TabbedPaneContainer;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModelImpl;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModel;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModelImpl;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumListContainer;
import fr.umlv.IRPhoto.gui.panel.albumlist.PhotoMiniatureContainer;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeContainer;
import fr.umlv.IRPhoto.gui.panel.features.FeaturesContainer;
import fr.umlv.IRPhoto.gui.panel.map.MapContainer;

public class ContainerFactory {

  private static final AlbumModel albumModel = new AlbumModelImpl();
  private static final PhotoSelectionModel photoSelectionModel = new PhotoSelectionModelImpl();

  public static JComponent createMainContainer() {
    return new MainContainer().getComponent();
  }

  public static JComponent createAlbumTreeContainer() {
    return new AlbumTreeContainer(ContainerFactory.albumModel).getComponent();
  }

  public static JComponent createFeaturesContainer() {
    return new FeaturesContainer(photoSelectionModel).getComponent();
  }

  public static JComponent createTabbedContainer() {
    return new TabbedPaneContainer().getComponent();
  }

  public static JComponent createMapContainer() {
    return new MapContainer().getComponent();
  }

  public static JComponent createAlbumListContainer() {
    return new AlbumListContainer(albumModel).getComponent();
  }

  public static PhotoMiniatureContainer createPhotoMiniatureContainer(
      Photo photo) {
    return new PhotoMiniatureContainer(photo, photoSelectionModel);
  }

}
