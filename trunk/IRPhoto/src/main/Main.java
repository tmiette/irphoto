package main;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fr.umlv.IRPhoto.album.AlbumLoader;
import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModelImpl;

public class Main {

  private static void createAndShowGUI(final AlbumModel model) {
    final JFrame frame = new JFrame("IRPhoto");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800, 600));
    frame.setLocationRelativeTo(null);
    frame.setContentPane(ContainerFactory.createMainContainer(model));
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        // save
        AlbumLoader.save(model);
      }
    });
    frame.setVisible(true);
  }

  public static void main(String[] args) {

    // model
    final AlbumModel model = new AlbumModelImpl();

    // load
    AlbumLoader.load(model);

    // Schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI(model);
      }
    });
  }
}
