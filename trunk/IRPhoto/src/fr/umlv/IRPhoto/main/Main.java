package fr.umlv.IRPhoto.main;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
   * Logger name.
   */
  public static final String loggerName = "logger";

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

    // this block configure the logger with handler and formatter
    final Logger logger = Logger.getLogger(loggerName);
    final FileHandler fh;
    try {
      fh = new FileHandler("IrPhotoLogFile.log", true);
      logger.addHandler(fh);
      logger.setLevel(Level.ALL);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
      logger.log(Level.WARNING, "Logger started");
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

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
