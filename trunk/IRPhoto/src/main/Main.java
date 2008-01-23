package main;

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
import fr.umlv.IRPhoto.gui.panel.MainContainer;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModelImpl;

public class Main {

  public static final String loggerName = "logger";

  private static void createAndShowGUI(final AlbumModel model) {
    Logger logger = Logger.getLogger(loggerName);
    FileHandler fh;

    try {

      // This block configure the logger with handler and formatter
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

    final JFrame frame = new JFrame("IRPhoto");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800, 600));
    frame.setLocationRelativeTo(null);
    frame.setContentPane(new MainContainer(model).getContainer());
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
