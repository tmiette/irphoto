package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainTom {

  public static void main(String[] args) {

    JFrame frame = new JFrame("test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800, 600));

    JTabbedPane tab = new JTabbedPane(JTabbedPane.BOTTOM);
    JPanel panel = new JPanel();
    JPanel panel2 = new JPanel();
    for (int i = 0; i < 50; i++) {
      panel2.add(new JButton("button" + i));
    }
    panel2.setBackground(Color.BLUE);
    panel2.setPreferredSize(new Dimension(2000, 2000));
    panel.add(panel2);
    JScrollPane scroll = new JScrollPane(panel2,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tab.addTab("buttons", scroll);
    tab.addTab(null, null);

    JScrollBar bar = scroll.getHorizontalScrollBar();
    bar.setPreferredSize(new Dimension(300, 15));
    tab.setTabComponentAt(1, bar);

    frame.setContentPane(tab);
    frame.setVisible(true);
  }

}
