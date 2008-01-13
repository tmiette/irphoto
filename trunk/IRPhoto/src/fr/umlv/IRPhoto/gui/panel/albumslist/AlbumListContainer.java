package fr.umlv.IRPhoto.gui.panel.albumslist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class AlbumListContainer implements ContainerInitializer {

  private final JTree tree;
  private final AlbumsListModel model;
  private final DefaultTreeCellRenderer renderer;
  private final ImageIcon rootIcon;
  private final ImageIcon leafIcon;
  private JFileChooser fileChooser;

  public AlbumListContainer() {

    // create icons
    this.rootIcon = new ImageIcon(AlbumListContainer.class
        .getResource("/icons/picture12x12.gif"));
    this.leafIcon = new ImageIcon(AlbumListContainer.class
        .getResource("/icons/arrow12x12.gif"));

    // initialize tree model
    this.model = new AlbumsListModel();

    // initialize jtree
    this.renderer = this.initializeRenderer();
    this.tree = this.initializeTree();

  }

  @Override
  public JComponent initialize() {
    final JPanel panel = new JPanel(new BorderLayout());
    panel.add(this.initializeButtonsPanel(), BorderLayout.NORTH);
    final JScrollPane scrollPane = new JScrollPane(this.tree,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(null);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(this.intializeLogo(), BorderLayout.SOUTH);
    return panel;
  }

  private JTree initializeTree() {

    // initialize jtree
    final JTree tree = new JTree(this.model);
    tree.setBackground(new Color(238, 238, 238));
    tree.setEditable(false);
    tree.setScrollsOnExpand(true);
    tree.setSelectionRow(0);
    tree.putClientProperty("JTree.lineStyle", "None");
    tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setCellRenderer(this.renderer);

    // add mouse listener to set up albums
    tree.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          // get the node clicked
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
              .getLastSelectedPathComponent();
          // ignore root node
          if (!node.isRoot()) {
            // inform the model of changes
            File albumFile = selectNewAlbum();
            if (albumFile != null) {
              model.linkAlbum(node, albumFile);
            }
          }
        }
      }
    });

    return tree;
  }

  private DefaultTreeCellRenderer initializeRenderer() {

    // initialize cell renderer
    final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setLeafIcon(this.rootIcon);
    renderer.setOpenIcon(this.rootIcon);
    renderer.setClosedIcon(this.rootIcon);
    renderer.setBackgroundNonSelectionColor(new Color(238, 238, 238));
    return renderer;
  }

  private JPanel initializeButtonsPanel() {

    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    // create the add button
    final JButton addButton = new JButton("+");
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // add a new album
        model.addAlbum();
        tree.expandRow(0);
        renderer.setLeafIcon(leafIcon);
      }
    });

    // create the remove button
    final JButton removeButton = new JButton("X");
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // remove the current album associated to the clicked node
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
            .getLastSelectedPathComponent();
        if (node != null && !node.isRoot()) {
          model.removeAlbum(node);
          if (tree.getRowCount() == 1) {
            renderer.setLeafIcon(rootIcon);
          }
        }
      }
    });

    // add buttons
    panel.add(addButton);
    panel.add(removeButton);

    return panel;
  }

  private JLabel intializeLogo() {
    final JLabel l = new JLabel(new ImageIcon(AlbumListContainer.class
        .getResource("/icons/logo.gif")));
    return l;
  }

  private File selectNewAlbum() {

    if (this.fileChooser == null) {
      // initialize first file chooser
      JFileChooser chooser = new JFileChooser();
      chooser.setMultiSelectionEnabled(false);
      chooser.setDialogTitle("Import a new album.");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      this.fileChooser = chooser;
    }

    // restart file chooser with the same directory
    this.fileChooser
        .setCurrentDirectory(this.fileChooser.getCurrentDirectory());

    // ask for a directory
    if (this.fileChooser.showDialog(tree, "Import") == JFileChooser.APPROVE_OPTION) {
      return this.fileChooser.getSelectedFile();
    } else {
      return null;
    }

  }
}
