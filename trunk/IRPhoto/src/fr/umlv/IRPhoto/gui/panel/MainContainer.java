package fr.umlv.IRPhoto.gui.panel;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class MainContainer implements ContainerInitializer {

  @Override
  public JComponent initialize() {
    final JSplitPane spliter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        true, ContainerFactory.createAlbumsListContainer(),
        initializeRightContainer());
    return spliter;
  }

  public static JComponent initializeRightContainer() {
    final JSplitPane spliter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
        ContainerFactory.createTabbedContainer(), ContainerFactory
            .createFeaturesContainer());
    spliter.setOneTouchExpandable(true);
    return spliter;
  }

}
