package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class MainTom2 {

  public static void main(String[] args) {

    JFrame mainFrame = setUpMainFrame();
    JPanel mainPanel = new JPanel(new BorderLayout()); // it contains sub panel

    JSplitPane spliter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    spliter.setLeftComponent(setUpLeftPanel());
    spliter.setRightComponent(setUpRightPanel());
    spliter.setDividerLocation(150);

    mainPanel.add(spliter);
    mainFrame.add(mainPanel);
    mainFrame.setVisible(true);
  }

  public static JFrame setUpMainFrame() {
    JFrame mainFrame = new JFrame();
    mainFrame.setTitle("Photo Manager");
    mainFrame.setSize(800, 600);
    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    return mainFrame;
  }

  public static JPanel setUpLeftPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    // panel.setBackground(new Color(10,30,30));

    return panel;
  }

  public static JPanel setUpRightPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JSplitPane spliter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        setUpTabbedPanel(), new JPanel());
    spliter.setOneTouchExpandable(true);
    spliter.setDividerLocation(400);
    panel.add(spliter);
    return panel;
  }

  public static JPanel setUpTabbedPanel() {
    // global panel which contains card panels and panel -> button+ scroll bar
    JPanel superPanel = new JPanel(new BorderLayout());
    final CardLayout cardLayout = new CardLayout();
    final JPanel globalPanel = new JPanel(cardLayout);
    // final JViewport view = new JViewport();

    superPanel.setPreferredSize(new Dimension(650, 400));

    final JPanel mapPanel = new JPanel(new BorderLayout());
    mapPanel.add(new JButton("TEST"));
    
    final JPanel photoPanel = new JPanel();
    final JPanel panel2 = new JPanel();
    for (int i = 0; i < 50; i++) {
      panel2.add(new JButton("button" + i));
    }
    panel2.setBackground(Color.BLUE);
    panel2.setPreferredSize(new Dimension(2000, 2000));
    photoPanel.add(panel2);

    final JScrollPane scroll = new JScrollPane(panel2,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    globalPanel.add(scroll, "photos");
    globalPanel.add(mapPanel, "map");

    superPanel.add(globalPanel, BorderLayout.CENTER);

    final JPanel bouttonsPanel = new JPanel(new GridBagLayout());
    final GridBagConstraints gbc = new GridBagConstraints();

    final JLabel map = new JLabel("Map");
    map.setForeground(Color.RED);
    final JLabel album = new JLabel("Album");
    album.setForeground(Color.BLUE);
    
    gbc.anchor = GridBagConstraints.EAST;

    bouttonsPanel.add(album, gbc);
    bouttonsPanel.add(map, gbc);

    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(0, 3, 0, 0);
    gbc.fill = GridBagConstraints.BOTH;

    bouttonsPanel.add(scroll.getHorizontalScrollBar(), gbc);

    superPanel.add(bouttonsPanel, BorderLayout.SOUTH);

    map.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.last(globalPanel);
        //FIXME enlever la scroll car elle ne correspond pas a l'onglet map
        scroll.getHorizontalScrollBar().setEnabled(false);
      }

    });
    
    album.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.first(globalPanel);
        // remettre la scroll bar
        scroll.getHorizontalScrollBar().setEnabled(true);
      }
    });
    
    return superPanel;
  }

}
