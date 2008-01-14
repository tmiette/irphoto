package fr.umlv.IRPhoto.gui.panel.albumslist;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import fr.umlv.IRPhoto.album.Album;

public class AlbumTreeModel extends DefaultTreeModel {

  private static final long serialVersionUID = 3282725357639187525L;

  private final AlbumModel model;

  public AlbumTreeModel(AlbumModel model) {
    this(model, new DefaultMutableTreeNode("Albums"));
  }

  private AlbumTreeModel(AlbumModel model, TreeNode root) {
    this(model, root, false);
  }

  private AlbumTreeModel(AlbumModel model, TreeNode root,
      boolean asksAllowsChildren) {
    super(root, asksAllowsChildren);
    this.model = model;
    this.model.addAlbumListener(new AlbumListener() {

      @Override
      public void albumAdded(Album album) {
        insertNodeInto(new DefaultMutableTreeNode(album),
            (DefaultMutableTreeNode) getRoot(), getChildCount(getRoot()));
      }

      @Override
      public void albumRemoved(Album album) {
        for (int i = 0; i < getChildCount(getRoot()); i++) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) getChild(
              getRoot(), i);
          if (node.getUserObject().equals(album)) {
            removeNodeFromParent(node);
          }
        }
      }

      @Override
      public void albumLinked(Album album) {
        for (int i = 0; i < getChildCount(getRoot()); i++) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) getChild(
              getRoot(), i);
          if (node.getUserObject().equals(album)) {
            fireTreeNodesChanged(node, node.getPath(), null, null);
          }
        }

      }

    });
  }

}
