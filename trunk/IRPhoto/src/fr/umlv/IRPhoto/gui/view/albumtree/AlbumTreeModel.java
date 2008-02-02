package fr.umlv.IRPhoto.gui.view.albumtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumUpdateListener;

/**
 * 
 * This class represents the tree model for the lists of albums. This model is
 * built from an album model.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class AlbumTreeModel extends DefaultTreeModel {

  /**
   * 
   * This class defines a tree node for the album tree. These tree nodes contain
   * an album and enable to import photos.
   * 
   * @author MIETTE Tom
   * @author MOURET Sebastien
   * 
   */
  public class AlbumTreeNode implements TreeNode {

    // album contained
    private final Album album;

    // children nodes
    private final ArrayList<AlbumTreeNode> children;

    // parent node
    private final AlbumTreeNode parent;

    /**
     * Constructor of an album tree node.
     * 
     * @param parent
     *            the parent node.
     * @param album
     *            the album to contain.
     */
    public AlbumTreeNode(AlbumTreeNode parent, Album album) {
      this.parent = parent;
      this.album = album;
      this.children = new ArrayList<AlbumTreeNode>();
    }

    /**
     * Adds a new album tree node to the children of the current node with the
     * specified parameter.
     * 
     * @param album
     *            the new album.
     * 
     * @return the new album tree node inserted.
     */
    public AlbumTreeNode addAlbumTreeNode(Album album) {
      // creates the node
      AlbumTreeNode child = new AlbumTreeNode(this, album);
      // add it to the list of children
      this.children.add(child);
      // fire the insertion
      nodesWereInserted(this, new int[] { this.children.size() - 1 });
      return child;
    }

    @Override
    public Enumeration<?> children() {
      return Collections.enumeration(this.children);
    }

    /**
     * Returns the album contained in the node.
     * 
     * @return the album contained in the node.
     */
    public Album getAlbum() {
      return this.album;
    }

    @Override
    public boolean getAllowsChildren() {
      return true;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
      return this.children.get(childIndex);
    }

    @Override
    public int getChildCount() {
      return this.children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
      return this.children.indexOf(node);
    }

    @Override
    public TreeNode getParent() {
      return this.parent;
    }

    @Override
    public boolean isLeaf() {
      return this.children.isEmpty();
    }

    /**
     * Removes an album tree node from the children of the current which
     * contains the album specified.
     * 
     * @param album
     *            the album.
     */
    public void removeAlbumTreeNode(Album album) {
      int i = 0;
      // for each node
      for (AlbumTreeNode child : this.children) {
        // if the child node contains the album
        if (child.album.equals(album)) {
          // remove it
          this.children.remove(i);
          // fire the remove
          nodesWereRemoved(this, new int[] { i }, new AlbumTreeNode[] { child });
          return;
        }
        i++;
      }
    }

    /**
     * Changes the string displayed in the tree for an album tree node.
     * 
     * @param album
     *            the album to look for.
     * @param newName
     *            the new name of the album.
     */
    public void rename(Album album, String newName) {
      // for each node
      for (AlbumTreeNode child : this.children) {
        // if the child node contains the album
        if (child.album.equals(album)) {
          // fire the change (newName is useless, the name displayed is
          // obtained with getName method of album)
          nodeChanged(child);
          break;
        }
      }
    }

    @Override
    public String toString() {
      if (this.album != null) {
        return this.album.getName();
      } else {
        return "Albums";
      }
    }

  }

  private static final long serialVersionUID = 4221800848377560902L;

  // the album model
  private final AlbumModel model;

  /**
   * Constructor of this model from an album model.
   * 
   * @param model
   *            the album model.
   */
  public AlbumTreeModel(AlbumModel model) {
    super(null);
    // creates the root node
    setRoot(new AlbumTreeNode(null, null));
    this.model = model;
    // listen to the albums creations and suppressions
    this.model.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        getRootTreeNode().addAlbumTreeNode(album);
      }

      @Override
      public void albumRemoved(Album album) {
        getRootTreeNode().removeAlbumTreeNode(album);
      }
    });
    // listen to albums modifications
    this.model.addAlbumUpdateListener(new AlbumUpdateListener() {
      @Override
      public void albumCleared(Album album) {
        // do nothing
      }

      @Override
      public void albumRenamed(Album album, String newName) {
        getRootTreeNode().rename(album, newName);
      }

      @Override
      public void photoAdded(Album album, Photo photo) {
        // do nothing
      }
    });

    for (Album album : model.getAlbums()) {
      this.getRootTreeNode().addAlbumTreeNode(album);
    }

  }

  /**
   * Returns the root node of this album tree.
   * 
   * @return the root node.
   */
  public AlbumTreeNode getRootTreeNode() {
    return (AlbumTreeNode) getRoot();
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    AlbumTreeNode node = (AlbumTreeNode) path.getLastPathComponent();
    // rename an album with the name typed by user
    this.model.nameAlbum(node.getAlbum(), (String) newValue);
  }

}
