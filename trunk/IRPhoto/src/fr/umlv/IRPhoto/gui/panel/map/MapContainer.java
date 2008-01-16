package fr.umlv.IRPhoto.gui.panel.map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class MapContainer implements ContainerInitializer {

  @Override
  public JComponent getComponent() {
    final JPanel mapPanel = new JPanel();
    mapPanel.add(new JButton("TEST"));
    return mapPanel;
  }

}
