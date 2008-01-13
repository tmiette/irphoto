package fr.umlv.IRPhoto.gui.panel.explorer2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class AlbumTree {

  private final JPanel mainPanel;
  private final AlbumTreeModel model;
  private final JTree tree;

  public AlbumTree() {
    this.mainPanel = new JPanel(new BorderLayout());
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    this.model = new AlbumTreeModel();
    this.tree = new JTree(this.model);
    this.tree.setEditable(false);
    this.tree.setScrollsOnExpand(true);
    this.tree.setSelectionRow(0);
    this.tree.putClientProperty("JTree.lineStyle", "None");
    final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setLeafIcon(new ImageIcon(AlbumTree.class
        .getResource("/icons/picture.png")));
    renderer.setOpenIcon(new ImageIcon(AlbumTree.class
        .getResource("/icons/picture.png")));
    renderer.setClosedIcon(new ImageIcon(AlbumTree.class
        .getResource("/icons/picture.png")));
    renderer.setBackgroundSelectionColor(Color.RED);
    this.tree.setCellRenderer(renderer);
    this.tree.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
              .getLastSelectedPathComponent();
          if (!node.isRoot()) {
            model.linkAlbum(node, "test");
          }
        }
      }
    });
    final JButton addButton = new JButton("+");
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        model.addAlbum();
        tree.expandRow(0);
        renderer.setLeafIcon(new ImageIcon(AlbumTree.class
            .getResource("/icons/arrow.gif")));
      }
    });
    final JButton removeButton = new JButton("-");
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
            .getLastSelectedPathComponent();
        if (node != null && !node.isRoot()) {
          model.removeAlbum(node);
          if (tree.getRowCount() == 1) {
            renderer.setLeafIcon(new ImageIcon(AlbumTree.class
                .getResource("/icons/picture.png")));
          }
        }
      }
    });
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    this.mainPanel.add(buttonPanel, BorderLayout.NORTH);
    this.mainPanel.add(this.tree, BorderLayout.CENTER);
  }

  public JPanel getPanel() {
    return this.mainPanel;
  }

}
