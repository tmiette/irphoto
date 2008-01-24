package fr.umlv.IRPhoto.gui.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumListContainer;
import fr.umlv.IRPhoto.gui.panel.map.MapContainer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;

public class TabbedPaneContainer implements ContainerInitializer {

  private final JComponent container;
  private final ArrayList<JLabel> tabs = new ArrayList<JLabel>();
  private JLabel currentTab;

  public TabbedPaneContainer(AlbumModel albumModel) {
    this.container = new JPanel(new BorderLayout());

    // panel which imitate a tabbed pane
    final CardLayout cardLayout = new CardLayout();
    final JPanel cardPanel = new JPanel(cardLayout);

    // scroll pane for the albums details panel
    final JScrollPane scroll = new JScrollPane(new AlbumListContainer(
        albumModel).getContainer(),
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // buttons panel to imitate tabbed pane
    final JPanel bouttonsPanel = new JPanel(new GridBagLayout());
    final GridBagConstraints gbc = new GridBagConstraints();

    final JLabel album = createTabLabel("Albums");
    final JLabel map = createTabLabel("Map");
    setCurrentTab(album);

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
    try {
      cardPanel.add(new MapContainer(albumModel).getContainer(),
          "Map");
    } catch (UnknownHostException e1) {
      cardPanel.add(new JPanel(), "Map");
    }
    this.container.add(cardPanel, BorderLayout.CENTER);
    this.container.add(bouttonsPanel, BorderLayout.SOUTH);
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

  private void setCurrentTab(JLabel label) {
    for (JLabel l : tabs) {
      l.setIcon(IconFactory.getIcon("tab.png"));
    }
    label.setIcon(IconFactory.getIcon("tab-blue.png"));
    currentTab = label;
  }

  private JLabel createTabLabel(String label) {
    final JLabel l = new JLabel(label, IconFactory.getIcon("tab.png"),
        SwingConstants.CENTER);
    l.setBackground(GraphicalConstants.BLUE);
    l.setVerticalTextPosition(JLabel.CENTER);
    l.setHorizontalTextPosition(JLabel.CENTER);
    l.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        l.setIcon(IconFactory.getIcon("tab-blue.png"));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        if (!l.equals(currentTab)) {
          l.setIcon(IconFactory.getIcon("tab.png"));
        }
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        setCurrentTab(l);
      }

    });
    tabs.add(l);
    return l;
  }
}
