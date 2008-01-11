package test;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class TestIsBack {

	public static void main(String[] args) {

		JPanel test = new JPanel();
		test.setLayout(new BoxLayout(test, BoxLayout.Y_AXIS));
		test.add(new JLabel("bozzo"));
		test.add(new JLabel("le clown"));
		test.add(new JLabel("est"));
		test.add(new JLabel("vraiment"));
		test.add(new JLabel("tres"));
		test.add(new JLabel("drole"));

		JScrollPane jtest = new JScrollPane(test);

		JPanel test2 = new JPanel();
		test2.add(new JLabel("Et mon cul, c'est du poulet?"));

		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab("buzz", jtest);

		tabbedPane.addTab("zavata", test2);

		System.out
				.println("tabbedPane.getTabCount() = " + tabbedPane.getTabCount());

		JFrame frame = new JFrame("test");
		frame.getContentPane().add(tabbedPane);
		frame.pack();
		frame.show();
	}
}
