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

    JComponent features = new FeaturesContainer(albumModel, photoModel)
        .getContainer();
    final JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        true, new TabbedPaneContainer(albumModel, photoModel).getContainer(),
        features);
    rightSplitPane.setOneTouchExpandable(true);
    rightSplitPane.setDividerLocation(320);

    this.container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
        new AlbumTreeContainer(albumModel).getContainer(), rightSplitPane);
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

}
