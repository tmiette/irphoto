package fr.umlv.IRPhoto.gui.panel.explorer;

import javax.swing.JOptionPane;

public class DialogAskCollectionName {

	private String collectionName;

	public DialogAskCollectionName() {

		String name = this.askForCollectionName();
		while (!isValidCollectionName(name)) {
			name = this.askForCollectionName();
		}
		this.collectionName = name;
	}

	public String getCollectionName() {

		return this.collectionName;
	}

	private boolean isValidCollectionName(String name) {

		return true;
	}

	private String askForCollectionName() {

		return JOptionPane.showInputDialog(null, "Entrer un nom de collection",
				"Nouvelle collection", JOptionPane.PLAIN_MESSAGE);
	}

}
