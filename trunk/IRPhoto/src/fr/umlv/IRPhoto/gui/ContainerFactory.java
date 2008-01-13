package fr.umlv.IRPhoto.gui;

import javax.swing.JComponent;

import fr.umlv.IRPhoto.gui.panel.MainContainer;
import fr.umlv.IRPhoto.gui.panel.TabbedPaneContainer;
import fr.umlv.IRPhoto.gui.panel.albumsdetail.AlbumsDetailContainer;
import fr.umlv.IRPhoto.gui.panel.albumslist.AlbumTree;
import fr.umlv.IRPhoto.gui.panel.features.FeaturesPanel;
import fr.umlv.IRPhoto.gui.panel.map.MapContainer;

public class ContainerFactory {

  public static JComponent createMainContainer() {
    return new MainContainer().initialize();
  }

  public static JComponent createAlbumsListContainer() {
    return new AlbumTree().initialize();
  }

  public static JComponent createFeaturesContainer() {
    return new FeaturesPanel().initialize();
  }

  public static JComponent createTabbedContainer() {
    return new TabbedPaneContainer().initialize();
  }

  public static JComponent createMapContainer() {
    return new MapContainer().initialize();
  }

  public static JComponent createAlbumsDetailContainer() {
    return new AlbumsDetailContainer().initialize();
  }

}
