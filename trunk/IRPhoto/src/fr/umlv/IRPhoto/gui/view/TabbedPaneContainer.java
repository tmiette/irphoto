package fr.umlv.IRPhoto.gui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.view.albumlist.AlbumListContainer;
import fr.umlv.IRPhoto.gui.view.map.MapContainer;
import fr.umlv.IRPhoto.util.GraphicalConstants;
import fr.umlv.IRPhoto.util.IconFactory;

/**
 * 
 * This class represents a container which imitates the behavior of a tabbed
 * pane. This container displays the album list with their photos in a first tab
 * and the world map in another tab.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class TabbedPaneContainer implements ContainerInitializer {

  // main container
  private final JComponent container;

  // label currently selected
  private JLabel currentTab;

  // list of labels imitating tabs
  private final ArrayList<JLabel> tabs = new ArrayList<JLabel>();

  /**
   * Constructor of this container.
   * 
   * @param albumModel
   *            the album model.
   */
  public TabbedPaneContainer(AlbumModel albumModel) {

    // initialize panel which imitate a tabbed pane with cardlayout
    final CardLayout cardLayout = new CardLayout();
    final JPanel cardPanel = new JPanel(cardLayout);

    // initialize scroll pane for the albums details panel
    final JScrollPane scroll = new JScrollPane(new AlbumListContainer(
        albumModel).getContainer(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // initialize tab labels which imitate tabs
    final JLabel album = createTabLabel("Albums");
    // show albums detail panel when albums label is clicked
    album.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.first(cardPanel);
        scroll.getHorizontalScrollBar().setEnabled(true);
      }
    });
    final JLabel map = createTabLabel("Map");
    // show map panel when map label is clicked
    map.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.last(cardPanel);
        scroll.getHorizontalScrollBar().setEnabled(false);
      }

    });
    // set the first tab selected
    setCurrentTab(album);

    // initialize tabs panel
    final JPanel bouttonsPanel = new JPanel(new GridBagLayout());
    final GridBagConstraints gbc = new GridBagConstraints();
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

    cardPanel.add(scroll, "Albums");
    cardPanel.add(new MapContainer(albumModel).getContainer(), "Map");

    // initialize main container
    this.container = new JPanel(new BorderLayout());
    this.container.add(cardPanel, BorderLayout.CENTER);
    this.container.add(bouttonsPanel, BorderLayout.SOUTH);
  }

  /**
   * Creates and returns a label used as tab.
   * 
   * @param text
   *            the label of the tab.
   * @return the label created.
   */
  private JLabel createTabLabel(String text) {
    final JLabel l = new JLabel(text, IconFactory.getIcon("tab.png"),
        SwingConstants.CENTER);
    l.setBackground(GraphicalConstants.BLUE);
    l.setVerticalTextPosition(JLabel.CENTER);
    l.setHorizontalTextPosition(JLabel.CENTER);
    l.addMouseListener(new MouseAdapter() {

      // selection effect
      @Override
      public void mouseClicked(MouseEvent e) {
        setCurrentTab(l);
      }

      // roll over effect
      @Override
      public void mouseEntered(MouseEvent e) {
        l.setIcon(IconFactory.getIcon("tab-blue.png"));
      }

      // roll over effect
      @Override
      public void mouseExited(MouseEvent e) {
        if (!l.equals(currentTab)) {
          l.setIcon(IconFactory.getIcon("tab.png"));
        }
      }

    });
    tabs.add(l);
    return l;
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

  /**
   * Sets the label (used as tab) which is currently selected.
   * 
   * @param label
   *            the label selected.
   */
  private void setCurrentTab(JLabel label) {
    for (JLabel l : tabs) {
      l.setIcon(IconFactory.getIcon("tab.png"));
    }
    label.setIcon(IconFactory.getIcon("tab-blue.png"));
    currentTab = label;
  }
}
