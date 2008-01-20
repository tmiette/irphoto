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

import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;

public class AlbumLoader {

  private static final String SAVED_FILE_PATH = "./albums.sav";

  public static void load(AlbumModel model) {

    try {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(
          new File(SAVED_FILE_PATH)));

      while (true) {

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
    } 
    catch(FileNotFoundException e){
      System.err.println("No saved file to load.");
    }
    catch (IOException e) {
      System.err.println("Load failed.");
    }

  }

  public static void save(AlbumModel model) {

    try {
      ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(
          new FileOutputStream(new File(SAVED_FILE_PATH))));

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
