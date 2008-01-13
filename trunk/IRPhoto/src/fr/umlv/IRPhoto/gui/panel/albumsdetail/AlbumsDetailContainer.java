package fr.umlv.IRPhoto.gui.panel.albumsdetail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class AlbumsDetailContainer implements ContainerInitializer {

  @Override
  public JComponent initialize() {
    final JPanel photoPanel = new JPanel();
    final JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    for (int i = 0; i < 50; i++) {
      panel2.add(new JButton("button" + i));
    }
    panel2.setBackground(Color.BLUE);
    panel2.setPreferredSize(new Dimension(2000, 2000));
    photoPanel.add(panel2);
    return photoPanel;
  }

}
