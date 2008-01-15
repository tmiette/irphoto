package fr.umlv.IRPhoto.gui.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class TabbedPaneContainer implements ContainerInitializer {

  @Override
  public JComponent initialize() {
    // main panel
    final JPanel mainPanel = new JPanel(new BorderLayout());

    // panel which imitate a tabbed pane
    final CardLayout cardLayout = new CardLayout();
    final JPanel cardPanel = new JPanel(cardLayout);

    // scroll pane for the albums details panel
    final JScrollPane scroll = new JScrollPane(ContainerFactory
        .createAlbumListContainer(),
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // buttons panel to imitate tabbed pane
    final JPanel bouttonsPanel = new JPanel(new GridBagLayout());
    final GridBagConstraints gbc = new GridBagConstraints();

    final JLabel album = createTabLabel("Albums");
    final JLabel map = createTabLabel("Map");

    // place and add tabs labels
    gbc.anchor = GridBagConstraints.EAST;
    bouttonsPanel.add(album, gbc);
    bouttonsPanel.add(map, gbc);

    // place and add horizontal scroll bar of albums detail panel
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(0, 3, 0, 0);
    gbc.fill = GridBagConstraints.BOTH;
    bouttonsPanel.add(scroll.getHorizontalScrollBar(), gbc);

    // show map panel when map label is clicked
    map.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.last(cardPanel);
        scroll.getHorizontalScrollBar().setEnabled(false);
      }

    });

    // show albums detail panel when albums label is clicked
    album.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.first(cardPanel);
        scroll.getHorizontalScrollBar().setEnabled(true);
      }
    });

    // adds different panels to main panel
    cardPanel.add(scroll, "Albums");
    cardPanel.add(ContainerFactory.createMapContainer(), "Map");
    mainPanel.add(cardPanel, BorderLayout.CENTER);
    mainPanel.add(bouttonsPanel, BorderLayout.SOUTH);
    
    return mainPanel;
  }

  private static JLabel createTabLabel(String label) {
    final JLabel l = new JLabel(label);
    return l;
  }

}
