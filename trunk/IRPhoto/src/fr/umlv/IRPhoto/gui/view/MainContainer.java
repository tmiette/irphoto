package fr.umlv.IRPhoto.gui.view;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.view.albumtree.AlbumTreeContainer;
import fr.umlv.IRPhoto.gui.view.features.FeaturesContainer;

/**
 * 
 * This class represents the main container of the application. This container
 * is the one which will be the content pane of main frame.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class MainContainer implements ContainerInitializer {

  // main container
  private final JComponent container;

  /**
   * Constructor of this container.
   * 
   * @param albumModel
   *            the album model.
   */
  public MainContainer(AlbumModel albumModel) {

    // initialize the right container
    final JComponent features = new FeaturesContainer(albumModel)
        .getContainer();
    final JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        true, new TabbedPaneContainer(albumModel).getContainer(), features);
    rightSplitPane.setOneTouchExpandable(true);
    rightSplitPane.setDividerLocation(320);

    // initialize the main container
    this.container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
        new AlbumTreeContainer(albumModel).getContainer(), rightSplitPane);
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

}
