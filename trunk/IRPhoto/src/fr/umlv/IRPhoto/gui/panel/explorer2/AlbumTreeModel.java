package fr.umlv.IRPhoto.gui.panel.explorer2;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import fr.umlv.IRPhoto.album.Album;

public class AlbumTreeModel extends DefaultTreeModel {

  private static final long serialVersionUID = 3282725357639187525L;

  public AlbumTreeModel() {
    this(new DefaultMutableTreeNode("Albums"));
  }

  private AlbumTreeModel(TreeNode root) {
    this(root, false);
  }

  private AlbumTreeModel(TreeNode root, boolean asksAllowsChildren) {
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
  
  public void linkAlbum(DefaultMutableTreeNode node, String newName){
    ((Album) node.getUserObject()).setName(newName);
    this.fireTreeNodesChanged(node, node.getPath(), null, null);
  }

}
