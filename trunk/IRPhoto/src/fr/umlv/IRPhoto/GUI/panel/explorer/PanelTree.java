package fr.umlv.IRPhoto.GUI.panel.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.umlv.IRPhoto.PhotoCollection;

public class PanelTree {

	private final JPanel panel;

	// PanelTree is a singleton
	private static PanelTree instance;

	// Synchronised object
	private static Object o = new Object();

	private static JTree tree;

	private static DefaultTreeModel model;

	private static TreeNode rootNode;

	public static PanelTree getInstance() {

		// multithreading safe
		synchronized (o) {
			if (instance == null) {
				return new PanelTree();
			}
		}
		return instance;
	}

	private PanelTree() {

		// constructing tree
		rootNode = new DefaultMutableTreeNode("Collections");
		model = new DefaultTreeModel(rootNode);
		tree = new JTree(model);
		tree.setEditable(false);
		tree.setSelectionRow(0);
		tree.addMouseListener(new CollectionsMouseListener());

		JScrollPane scrollPane = new JScrollPane(tree);

		this.panel = new JPanel(new BorderLayout());
		this.panel.add(scrollPane, BorderLayout.CENTER);
		this.panel.setBackground(Color.BLUE);

	}

	public JPanel getPanel() {

		return panel;
	}

	public static JTree getM_tree() {

		return tree;
	}

	public static DefaultTreeModel getM_model() {

		return model;
	}

	public static TreeNode getRootNode() {

		return rootNode;
	}

	private static void addNode(String name) {

		// add new node as a child of a selected node at the end
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
		PanelTree.getM_model().insertNodeInto(newNode,
				(MutableTreeNode) PanelTree.getRootNode(),
				PanelTree.getRootNode().getChildCount());

		// make the node visible by scroll to it
		TreeNode[] nodes = PanelTree.getM_model().getPathToRoot(newNode);
		TreePath path = new TreePath(nodes);
		PanelTree.getM_tree().scrollPathToVisible(path);

		// select the newly added node
		PanelTree.getM_tree().setSelectionPath(path);

		// Make the newly added node editable
		// PanelTree.getM_tree().startEditingAtPath(path);

	}

	public static void addCollection(String name) {

		addNode(name);
		PhotoCollection.addCollection(name);
	}

	class CollectionsMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			// Get the last node clicked on
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			if (n.isRoot()) {
				System.out.println("It's root nothing to do");
				// Nothing to do
				return;
			}

			// Capture the double click
			if (e.getClickCount() == 2) {
				System.out.println("double clique");

				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(true);
				fc.setApproveButtonText("Importer");
				FileFilter fileExt = new FileNameExtensionFilter("Fichiers Images",
						"gif", "GIF", "JPG", "JPEG");
				fc.setFileFilter(fileExt);

				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					// Files choosen
					File[] files = fc.getSelectedFiles();

					for (int i = 0; i < files.length; ++i) {

						System.out.println(files[i].getName());
						System.out.println(files[i].getAbsolutePath());
					}
				}
				else
					; // Nothing selected

			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}
	}

	public static void deleteCollection() {

		MutableTreeNode selNode = (MutableTreeNode) tree
				.getSelectionPath()
				.getLastPathComponent();

		if (!selNode.equals(rootNode)) {
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(selNode);
			PhotoCollection.deleteCollection(selNode.toString());
		}

		
	}

}
