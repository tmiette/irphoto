package fr.umlv.IRPhoto.gui.panel.explorer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.Controller;

public class PanelMenu {

	private final JPanel panel;

	private final JButton addButton;

	private final JButton delButton;

	private static PanelMenu instance;

	private static Object o = new Object();

	public static PanelMenu getInstance() {

		// multithreading safe
		synchronized (o) {
			if (instance == null) {
				instance =  new PanelMenu();
			}
		}
		return instance;
	}

	private PanelMenu() {

		this.panel = new JPanel(null);
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.X_AXIS));
		this.addButton = createAddButton();
		this.delButton = createDelButton();
		this.panel.setBackground(Color.BLACK);
		this.panel.add(this.addButton);
		this.panel.add(this.delButton);

	}

	private static JButton createAddButton() {

		JButton jb = new JButton("+");
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Controller.addCollection();
			}
		});

		return jb;
	}


	private static JButton createDelButton() {

		JButton jb = new JButton("x");
		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Controller.deleteCollection();
			}
		});

		return jb;
	}

	public JPanel getPanel() {

		return panel;
	}
}
