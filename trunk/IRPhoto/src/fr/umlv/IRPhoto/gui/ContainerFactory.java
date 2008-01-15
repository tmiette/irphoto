package fr.umlv.IRPhoto.gui;

import javax.swing.JComponent;

import fr.umlv.IRPhoto.gui.panel.MainContainer;
import fr.umlv.IRPhoto.gui.panel.TabbedPaneContainer;
import fr.umlv.IRPhoto.gui.panel.album.AlbumListView;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumModelImpl;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeContainer;
import fr.umlv.IRPhoto.gui.panel.features.FeaturesContainer;
import fr.umlv.IRPhoto.gui.panel.features.PhotoSelectionModel;
import fr.umlv.IRPhoto.gui.panel.features.PhotoSelectionModelImpl;
import fr.umlv.IRPhoto.gui.panel.map.MapContainer;

public class ContainerFactory {

  private static final AlbumModel albumModel = new AlbumModelImpl();
  private static final PhotoSelectionModel photoSelectionModel = new PhotoSelectionModelImpl();

  public static JComponent createMainContainer() {
    return new MainContainer().initialize();
  }

  public static JComponent createAlbumTreeContainer() {
    return new AlbumTreeContainer(ContainerFactory.albumModel).initialize();
  }

  public static JComponent createFeaturesContainer() {
    return new FeaturesContainer(photoSelectionModel).initialize();
  }

  public static JComponent createTabbedContainer() {
    return new TabbedPaneContainer().initialize();
  }

  public static JComponent createMapContainer() {
    return new MapContainer().initialize();
  }

  public static JComponent createAlbumListContainer() {
    return new AlbumListView(albumModel).initialize();
  }

}
