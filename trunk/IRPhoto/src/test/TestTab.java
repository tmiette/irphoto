package test;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;

public class TestTab {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame jf = new JFrame();
		jf.setTitle("Test Projet");
		jf.setSize(640, 480);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints gbcTab = new GridBagConstraints();
		GridBagConstraints gbcScroll = new GridBagConstraints();

		gbcTab.gridwidth = 200;
		gbcTab.gridheight = 0;
		gbcTab.insets = new Insets(0,0,0,0);
		gbcTab.anchor = GridBagConstraints.NORTHWEST;
		gbcTab.fill = GridBagConstraints.NONE;
		gbcTab.weightx = 0;
		gbcTab.weighty = 0;
		gbcTab.ipadx = 100;
		gbcTab.ipady = 100;

		gbcScroll.gridwidth = 200;
		gbcScroll.gridheight = 400;
		gbcScroll.insets = new Insets(0, 0, 0, 0);
		gbcScroll.anchor = GridBagConstraints.SOUTHWEST;
		gbcScroll.fill = GridBagConstraints.HORIZONTAL;
		gbcScroll.weightx = 100;
		gbcScroll.weighty = 0;
		gbcScroll.ipadx = 100;
		gbcScroll.ipady = 0;

		JPanel collections = new JPanel();
		addButtons(2, collections);
		JPanel map = new JPanel();
		addButtons(2, map);

		JTabbedPane tab = new JTabbedPane(JTabbedPane.BOTTOM,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		tab.setSize(new Dimension(100,100));
		tab.setBackground(Color.BLACK);
		tab.addTab("Collections", collections);
		tab.addTab("Map", map);
		// jf.getContentPane().add(tab);

		JScrollBar jb = new JScrollBar();
		jb.setOrientation(JScrollBar.HORIZONTAL);
		// jf.getContentPane().add(jb);

		jf.getContentPane().add(tab, gbcTab);
		jf.getContentPane().add(jb, gbcScroll);

		jf.setVisible(true);
	}

	public static void addButtons(int n, Container c) {

		for (int i = 1; i <= n; i++) {
			JButton jb = new JButton("Button" + i);
			c.add(jb);
		}
	}
}
