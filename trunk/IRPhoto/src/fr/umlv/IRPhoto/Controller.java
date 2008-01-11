package fr.umlv.IRPhoto;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.umlv.IRPhoto.gui.panel.explorer.DialogAskCollectionName;
import fr.umlv.IRPhoto.gui.panel.explorer.PanelTree;

public class Controller {

	private static HashMap<Integer, DefaultMutableTreeNode> map = new HashMap<Integer, DefaultMutableTreeNode>();

	public static void addCollection() {

		DialogAskCollectionName dacn = new DialogAskCollectionName();
		String name = dacn.getCollectionName();
		if (name != null) {
			DefaultMutableTreeNode node = PanelTree.addCollection(name);
			int id = PhotoCollection.addCollection(name);
			map.put(id, node);
		}
	}

	public static void deleteCollection() {

		PanelTree.deleteCollection();
	}
}
