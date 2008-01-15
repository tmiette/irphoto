package fr.umlv.IRPhoto.gui.panel.albumslist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.umlv.IRPhoto.album.Album;

public class AlbumTreeModel extends DefaultTreeModel {

  private static final long serialVersionUID = 4221800848377560902L;

  private final AlbumModel model;

  public AlbumTreeModel(AlbumModel model) {
    super(null);
    setRoot(new AlbumTreeNode(null, null));
    this.model = model;
    this.model.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        getRootTreeNode().add(album);
      }

      @Override
      public void albumRemoved(Album album) {
        getRootTreeNode().remove(album);
      }

      @Override
      public void albumUpdated(Album album) {
        getRootTreeNode().update(album);
      }

    });
  }

  public AlbumTreeNode getRootTreeNode() {
    return (AlbumTreeNode) getRoot();
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    AlbumTreeNode node = (AlbumTreeNode) path.getLastPathComponent();
    this.model.nameAlbum(node.getAlbum(), (String) newValue);
  }

  public class AlbumTreeNode implements TreeNode {

    private final Album album;
    private final AlbumTreeNode parent;
    private final ArrayList<AlbumTreeNode> children;

    public AlbumTreeNode(AlbumTreeNode parent, Album album) {
      this.parent = parent;
      this.album = album;
      this.children = new ArrayList<AlbumTreeNode>();
    }

    public Album getAlbum() {
      return this.album;
    }

    public AlbumTreeNode add(Album album) {
      AlbumTreeNode child = new AlbumTreeNode(this, album);
      this.children.add(child);
      nodesWereInserted(this, new int[] { this.children.size() - 1 });
      return child;
    }

    public void remove(Album album) {
      int i = 0;
      for (AlbumTreeNode child : this.children) {
        if (child.album.equals(album)) {
          this.children.remove(i);
          nodesWereRemoved(this, new int[] { i }, new AlbumTreeNode[] { child });
          break;
        }
        i++;
      }
    }

    public void update(Album album) {
      for (AlbumTreeNode child : this.children) {
        if (child.album.equals(album)) {
          nodeChanged(child);
          break;
        }
      }
    }

    @Override
    public Enumeration<?> children() {
      return Collections.enumeration(this.children);
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

    @Override
    public String toString() {
      if (this.album != null) {
        return this.album.getName();
      } else {
        return "Albums";
      }
    }

  }

}
