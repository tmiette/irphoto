

import javax.swing.JFrame;

import fr.umlv.IRPhoto.gui.panel.explorer2.AlbumTree;

public class TreeMain {

  public static void main(String[] args) {

    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(800, 600);

    f.setContentPane(new AlbumTree().getPanel());
    f.setVisible(true);

  }

}
