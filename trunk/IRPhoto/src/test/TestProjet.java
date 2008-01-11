package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;

public class TestProjet {

	public static void main(String[] args) {

		JFrame jf = new JFrame();
		jf.setTitle("Test Projet");
		jf.setSize(640, 480);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(new BorderLayout());
		
		JTabbedPane tabbPane = new JTabbedPane();
		tabbPane.add("onglet 1", null);
		tabbPane.add("onglet 2", null);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		
		JPanel tabPanel = new JPanel();
		tabPanel.setPreferredSize(new Dimension(200,200));
		tabPanel.add(tabbPane);
		
		JPanel scrollPanel = new JPanel();
		scrollPanel.add(scrollBar);
		
		jf.getContentPane().add(tabPanel, BorderLayout.WEST);
		jf.getContentPane().add(scrollPanel, BorderLayout.EAST);
		
		jf.setVisible(true);
	}
	
}
