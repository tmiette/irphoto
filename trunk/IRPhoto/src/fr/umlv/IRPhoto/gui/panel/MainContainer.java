package fr.umlv.IRPhoto.gui.panel;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class MainContainer implements ContainerInitializer {

  @Override
  public JComponent initialize() {
    final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        true, ContainerFactory.createAlbumsListContainer(),
        initializeRightContainer());
    return splitPane;
  }

  private static JComponent initializeRightContainer() {
    final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
        ContainerFactory.createTabbedContainer(), ContainerFactory
            .createFeaturesContainer());
    splitPane.setOneTouchExpandable(true);
    return splitPane;
  }

}
