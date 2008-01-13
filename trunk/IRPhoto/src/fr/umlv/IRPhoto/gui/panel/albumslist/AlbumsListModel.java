package fr.umlv.IRPhoto.gui.panel.albumslist;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import fr.umlv.IRPhoto.album.Album;

public class AlbumsListModel extends DefaultTreeModel {

  private static final long serialVersionUID = 3282725357639187525L;

  public AlbumsListModel() {
    this(new DefaultMutableTreeNode("Albums"));
  }

  private AlbumsListModel(TreeNode root) {
    this(root, false);
  }

  private AlbumsListModel(TreeNode root, boolean asksAllowsChildren) {
    super(root, asksAllowsChildren);
  }

  public void addAlbum() {
    this.insertNodeInto(new DefaultMutableTreeNode(new Album()),
        (DefaultMutableTreeNode) this.getRoot(), this.getChildCount(this
            .getRoot()));
  }

  public void removeAlbum(DefaultMutableTreeNode node) {
    this.removeNodeFromParent(node);
  }
  
  public void linkAlbum(DefaultMutableTreeNode node, File albumFile){
    ((Album) node.getUserObject()).setName(albumFile.getName());
    this.fireTreeNodesChanged(node, node.getPath(), null, null);
  }

}
