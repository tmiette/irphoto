package fr.umlv.IRPhoto.gui.panel;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeContainer;
import fr.umlv.IRPhoto.gui.panel.features.FeaturesContainer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;

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
