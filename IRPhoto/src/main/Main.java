package main;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fr.umlv.IRPhoto.album.AlbumLoader;
import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.model.album.AlbumModelImpl;
import fr.umlv.IRPhoto.gui.view.MainContainer;

/**
 * 
 * Main class which launches the program.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class Main {

  /**
   * 
   * Creates and shows the application graphical interface.
   * 
   * @param model
   *            the album model.
   */
  private static void createAndShowGUI(final AlbumModel model) {

    // creates main frame
    final JFrame frame = new JFrame("IRPhoto");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800, 600));
    frame.setLocationRelativeTo(null);
    frame.setContentPane(new MainContainer(model).getContainer());
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        // performs save operation on close
        AlbumLoader.save(model);
      }
    });
    SplashScreenManager.endStep();
    frame.setVisible(true);
  }

  /**
   * 
   * Main method.
   * 
   * @param args
   *            line command arguments (useless).
   */
  public static void main(String[] args) {

    // manages splash screen
    SplashScreenManager.start();

    // creates album model
    final AlbumModel model = new AlbumModelImpl();

    // loads saved files
    AlbumLoader.load(model);
    SplashScreenManager.endStep();

    // schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI(model);
      }
    });

  }
}
