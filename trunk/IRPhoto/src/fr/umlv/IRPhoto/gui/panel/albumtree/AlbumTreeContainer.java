package fr.umlv.IRPhoto.gui.panel.albumtree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.albumtree.AlbumTreeModel.AlbumTreeNode;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;

/**
 * 
 * This class represent the tree component which contains the list of albums
 * created. This tree has just one node which can accepts children (the root
 * node).
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class AlbumTreeContainer implements ContainerInitializer {

  // container
  private final JComponent container;

  // file chooser
  private JFileChooser fileChooser;

  /**
   * Constructor of the album tree container using an album model.
   * 
   * @param albumModel
   *            the album model.
   */
  public AlbumTreeContainer(final AlbumModel albumModel) {

    // icon for root node
    final Icon rootIcon = IconFactory.getIcon("album-24x24.png");
    // icon for other nodes
    final Icon leafIcon = IconFactory.getIcon("album3-24x24.png");

    // initialize the tree renderer
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
          // display the name of album
          setText(a.getName());
          setToolTipText(a.getName());
        } else {
          // display the root node label
          setText("Albums");
          setToolTipText("Albums");
        }
        return this;
      }
    };
    // sets the default icons of the renderer
    renderer.setLeafIcon(rootIcon);
    renderer.setOpenIcon(rootIcon);
    renderer.setClosedIcon(rootIcon);

    // initialize the album tree
    final JTree tree = new JTree(new AlbumTreeModel(albumModel));
    tree.setEditable(true);
    tree.setScrollsOnExpand(true);
    tree.setSelectionRow(0);
    tree.putClientProperty("JTree.lineStyle", "None");
    ToolTipManager.sharedInstance().registerComponent(tree);
    tree.setCellRenderer(renderer);
    // add mouse listener to set up albums
    tree.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        // get the node clicked
        AlbumTreeNode node = (AlbumTreeNode) tree
            .getLastSelectedPathComponent();
        // ignore root node
        if (tree.getModel().isLeaf(node) && tree.getRowCount() != 1) {

          if (e.getClickCount() == 2) {
            // informs the model of changes
            File albumFile = selectNewAlbum();
            if (albumFile != null) {
              albumModel.linkAlbum(node.getAlbum(), albumFile);
            }
          } else {
            albumModel.selectAlbum(node.getAlbum());
          }
        }
      }
    });
    // modify root icon if there is more than one album
    if (tree.getRowCount() > 1) {
      renderer.setLeafIcon(leafIcon);
    }

    // initialize the buttons panel to add and remove albums
    final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonsPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    // create the add button
    final JLabel addButton = new JLabel(IconFactory.getIcon("add-32x32.png"));
    addButton.setToolTipText("Add a new album.");
    addButton.setBorder(BorderFactory.createRaisedBevelBorder());
    addButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // add a new album
        albumModel.addAlbum();
        tree.expandRow(0);
        renderer.setLeafIcon(leafIcon);
      }

      @Override
      public void mousePressed(MouseEvent e) {
        addButton.setBorder(BorderFactory.createLoweredBevelBorder());
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        addButton.setBorder(BorderFactory.createRaisedBevelBorder());
      }
    });
    // create the remove button
    final JLabel removeButton = new JLabel(IconFactory
        .getIcon("remove-32x32.png"));
    removeButton.setToolTipText("Remove all selected albums.");
    removeButton.setBorder(BorderFactory.createRaisedBevelBorder());
    removeButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // remove albums associated to selected nodes
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

      @Override
      public void mousePressed(MouseEvent e) {
        removeButton.setBorder(BorderFactory.createLoweredBevelBorder());
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        removeButton.setBorder(BorderFactory.createRaisedBevelBorder());
      }
    });
    // adds buttons to the buttons panel
    buttonsPanel.add(addButton);
    buttonsPanel.add(removeButton);

    // initialize the logo panel
    final JLabel logoPanel = new JLabel(IconFactory.getIcon("logo.png"));
    logoPanel.setMinimumSize(new Dimension(0, 0));

    // initialize the main container
    this.container = new JPanel(new BorderLayout());
    this.container.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    this.container.add(buttonsPanel, BorderLayout.NORTH);
    final JScrollPane scrollPane = new JScrollPane(tree,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(null);
    this.container.add(scrollPane, BorderLayout.CENTER);
    this.container.add(logoPanel, BorderLayout.SOUTH);
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

  /**
   * Returns the file selected (only directories allowed) by user in an file
   * chooser frame.
   * 
   * @return the file selected.
   */
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

    // restart file chooser with the same directory as previous
    this.fileChooser
        .setCurrentDirectory(this.fileChooser.getCurrentDirectory());

    // ask for a directory
    if (this.fileChooser.showDialog(null, "Import") == JFileChooser.APPROVE_OPTION) {
      return this.fileChooser.getSelectedFile();
    } else {
      return null;
    }

  }
}
