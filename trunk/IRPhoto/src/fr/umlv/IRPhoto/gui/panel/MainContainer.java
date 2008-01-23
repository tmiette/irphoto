package fr.umlv.IRPhoto.gui.panel;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeContainer;
import fr.umlv.IRPhoto.gui.panel.features.FeaturesContainer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModelImpl;

public class MainContainer implements ContainerInitializer {

  private final JComponent container;

  public MainContainer(AlbumModel albumModel) {

    PhotoModel photoModel = new PhotoModelImpl();

    final JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        true, new TabbedPaneContainer(albumModel, photoModel).getContainer(),
        new FeaturesContainer(photoModel).getContainer());
    rightSplitPane.setOneTouchExpandable(true);
    rightSplitPane.setDividerLocation(400);

    this.container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
        new AlbumTreeContainer(albumModel).getContainer(), rightSplitPane);
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

}
