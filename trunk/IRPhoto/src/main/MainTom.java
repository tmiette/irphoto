package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class MainTom {

  public static void main(String[] args) {

    final JFrame frame = new JFrame("test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800, 600));

    final JTabbedPane tab = new JTabbedPane(JTabbedPane.BOTTOM);
    tab.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    // MyTabbedPaneUI.createUI(tab);
    final JPanel panel = new JPanel();
    final JPanel panel2 = new JPanel();
    for (int i = 0; i < 50; i++) {
      panel2.add(new JButton("button" + i));
    }
    panel2.setBackground(Color.BLUE);
    panel2.setPreferredSize(new Dimension(2000, 2000));
    panel.add(panel2);
    final JScrollPane scroll = new JScrollPane(panel2,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tab.addTab("buttons", scroll);
    tab.addTab(null, null);

    final JScrollBar bar = scroll.getHorizontalScrollBar();
    tab.setTabComponentAt(1, bar);

    scroll.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        System.err.println("Appel avec d="
            + new Dimension(scroll.getWidth() - 130, 17));
        bar.setPreferredSize(new Dimension(scroll.getWidth() - 130, 17));
        frame.repaint();

        System.err.println(bar.getPreferredSize());
      }

    });

    frame.setContentPane(tab);
    frame.setVisible(true);
  }

}
