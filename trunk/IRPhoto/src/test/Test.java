package test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Test {

	private static void addForm(JPanel panel, String text, JComponent component,
			int gridwidth) {

		JLabel label = new JLabel(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(2, 7, 2, 7);
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(label, gbc);
		gbc.gridwidth = gridwidth;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		panel.add(component, gbc);
	}

	private static void addCombo(JPanel panel, String text, JComponent component) {

		JLabel label = new JLabel(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(2, 7, 2, 7);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weighty = 1.0;
		panel.add(label, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;
		panel.add(component, gbc);
	}

	private static JButton addButton(JPanel panel, String text) {

		JButton button = new JButton(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 0, 2, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(button, gbc);
		return button;
	}

	public static void main(String[] args) {

		JFrame jf = new JFrame();
		jf.pack();
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		addForm(panel, "login", new JTextField(10), 1);
		addForm(panel, "password", new JTextField(10), GridBagConstraints.REMAINDER);
		String[] groups = { "ens", "étudiant" };
		addCombo(panel, "groupe", new JComboBox(groups));
		addButton(panel, "Annuler");
		addButton(panel, "Ok");
		
		jf.add(panel);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

}
