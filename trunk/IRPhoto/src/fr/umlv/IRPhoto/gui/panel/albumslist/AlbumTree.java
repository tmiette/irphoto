package fr.umlv.IRPhoto.gui.panel.albumslist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class AlbumTree implements ContainerInitializer {

  @Override
  public JComponent initialize() {
    // main panel
    final JPanel mainPanel = new JPanel(new BorderLayout());
    final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final AlbumTreeModel model = new AlbumTreeModel();
    final JTree tree = new JTree(model);
    tree.setEditable(false);
    tree.setScrollsOnExpand(true);
    tree.setSelectionRow(0);
    tree.putClientProperty("JTree.lineStyle", "None");
    final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setLeafIcon(new ImageIcon(AlbumTree.class
        .getResource("/icons/picture12x12.gif")));
    renderer.setOpenIcon(new ImageIcon(AlbumTree.class
        .getResource("/icons//picture12x12.gif")));
    renderer.setClosedIcon(new ImageIcon(AlbumTree.class
        .getResource("/icons//picture12x12.gif")));
    renderer.setBackgroundSelectionColor(Color.RED);
    tree.setCellRenderer(renderer);
    tree.addMouseListener(new MouseAdapter() {
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
            .getResource("/icons/arrow12x12.gif")));
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
                .getResource("/icons//picture12x12.gif")));
          }
        }
      }
    });
    buttonPanel.add(addButton);
    buttonPanel.add(removeButton);
    mainPanel.add(buttonPanel, BorderLayout.NORTH);
    mainPanel.add(tree, BorderLayout.CENTER);
    return mainPanel;
  }

}
