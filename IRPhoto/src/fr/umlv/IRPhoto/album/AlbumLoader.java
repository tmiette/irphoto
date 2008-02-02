package fr.umlv.IRPhoto.album;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.umlv.IRPhoto.gui.model.album.AlbumModel;

/**
 * 
 * This class defines static methods to load and save an album model. The model
 * can be reloaded after the application is closed.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class AlbumLoader {

  // default path of the saved file
  private static final String SAVED_FILE_PATH = "./albums.sav";

  /**
   * Load an album model. The saved file is read and each saved album is added
   * to the model.
   * 
   * @param model
   *            the album model to load.
   */
  public static void load(AlbumModel model) {

    try {

      // reader of the saved file
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(
          new File(SAVED_FILE_PATH)));

      while (true) {

        // try to read an album and add it to the model
        try {
          Album a = (Album) in.readObject();
          model.addSavedAlbum(a);
        } catch (EOFException e) {
          break;
        } catch (IOException e) {
          System.err.println("Cannot load album.");
        } catch (ClassNotFoundException e) {
          System.err.println("Cannot load album.");
        }

      }

      in.close();
    } catch (FileNotFoundException e) {
      System.err.println("No saved file to load.");
    } catch (IOException e) {
      System.err.println("Load failed.");
    }

  }

  /**
   * Save an album model. Each album contained in the model are written in the
   * saved file.
   * 
   * @param model
   *            the album model to save.
   */
  public static void save(AlbumModel model) {

    try {
      // writer of the saved file
      ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(
          new FileOutputStream(new File(SAVED_FILE_PATH))));

      // try to write each album
      for (Album album : model.getAlbums()) {
        try {
          out.writeObject(album);
        } catch (IOException e) {
          e.printStackTrace();
          System.err.println("Cannot save album " + album.getName() + ".");
        }
      }

      out.close();

    } catch (IOException e) {
      System.err.println("Save failed.");
    }

  }

}
