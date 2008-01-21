package fr.umlv.IRPhoto.gui.panel.albumtree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeModel.AlbumTreeNode;

public class AlbumTreeContainer implements ContainerInitializer {

  private final JTree tree;
  private final AlbumModel albumModel;
  private final AlbumSelectionModel selectionModel;
  private final DefaultTreeCellRenderer renderer;
  private final Icon rootIcon;
  private final Icon leafIcon;
  private JFileChooser fileChooser;

  public AlbumTreeContainer(AlbumModel model, AlbumSelectionModel selectionModel) {

    // create icons
    this.rootIcon = IconFactory.getIcon("album-24x24.png");
    this.leafIcon = IconFactory.getIcon("album3-24x24.png");

    // initialize tree model
    this.albumModel = model;

    // initialize jtree
    this.renderer = this.initializeRenderer();
    this.tree = this.initializeTree(new AlbumTreeModel(this.albumModel));
    this.selectionModel = selectionModel;
  }

  @Override
  public JComponent getComponent() {
    final JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    panel.add(this.initializeButtonsPanel(), BorderLayout.NORTH);
    final JScrollPane scrollPane = new JScrollPane(this.tree,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(null);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(this.intializeLogo(), BorderLayout.SOUTH);
    return panel;
  }

  private JTree initializeTree(TreeModel model) {

    // initialize jtree
    final JTree tree = new JTree(model);
    tree.setEditable(true);
    tree.setScrollsOnExpand(true);
    tree.setSelectionRow(0);
    tree.putClientProperty("JTree.lineStyle", "None");
    ToolTipManager.sharedInstance().registerComponent(tree);
    tree.setCellRenderer(this.renderer);

    // add mouse listener to set up albums
    tree.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        // get the node clicked
        AlbumTreeNode node = (AlbumTreeNode) tree
            .getLastSelectedPathComponent();
        // ignore root node
        if (tree.getModel().isLeaf(node) && tree.getRowCount() != 1) {

          selectionModel.selectAlbum(node.getAlbum());

          if (e.getClickCount() == 2) {
            // inform the model of changes
            File albumFile = selectNewAlbum();
            if (albumFile != null) {
              albumModel.linkAlbum(node.getAlbum(), albumFile);
            }
          }
        }
      }
    });
    
    if (tree.getRowCount() > 1) {
      renderer.setLeafIcon(leafIcon);
    }

    return tree;
  }

  private DefaultTreeCellRenderer initializeRenderer() {

    // initialize cell renderer
    final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

      private static final long serialVersionUID = -2488272319873469559L;

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value,
          boolean selected, boolean expanded, boolean leaf, int row,
          boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded,
            leaf, row, hasFocus);
        AlbumTreeNode node = (AlbumTreeNode) value;
        Album a = node.getAlbum();
        if (a != null) {
          setText(a.getName());
          setToolTipText(a.getName());
        } else {
          setText("Albums");
          setToolTipText("Albums");
        }
        return this;
      }

    };

    renderer.setLeafIcon(this.rootIcon);
    renderer.setOpenIcon(this.rootIcon);
    renderer.setClosedIcon(this.rootIcon);
    return renderer;
  }

  private JPanel initializeButtonsPanel() {

    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setBackground(Color.WHITE);

    // create the add button
    final JButton addButton = new JButton(IconFactory.getIcon("add-32x32.png"));
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // add a new album
        albumModel.addAlbum();
        tree.expandRow(0);
        renderer.setLeafIcon(leafIcon);
      }
    });
    addButton.setToolTipText("Add a new album.");

    // create the remove button
    final JButton removeButton = new JButton(IconFactory
        .getIcon("remove-32x32.png"));
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        // remove albums associated to clicked nodes
        ArrayList<Album> list = new ArrayList<Album>();
        for (TreePath path : tree.getSelectionPaths()) {
          AlbumTreeNode node = (AlbumTreeNode) path.getLastPathComponent();
          Album a = node.getAlbum();
          if (a != null) {
            list.add(a);
          }
          albumModel.removeAlbum(list);
          tree.setSelectionRow(0);
          if (tree.getRowCount() == 1) {
            renderer.setLeafIcon(rootIcon);
          }
        }
      }
    });
    removeButton.setToolTipText("Remove all selected albums.");

    // add buttons
    panel.add(addButton);
    panel.add(removeButton);

    return panel;
  }

  private JLabel intializeLogo() {
    final JLabel l = new JLabel(IconFactory.getIcon("logo.png"));
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
