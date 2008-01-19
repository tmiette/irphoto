package fr.umlv.IRPhoto.gui.panel.albumlist;

import java.util.ArrayList;

import fr.umlv.IRPhoto.album.Album;

public class AlbumSelectionModelImpl implements AlbumSelectionModel {
   
   private final ArrayList<AlbumSelectionListener> listeners;
   
   public AlbumSelectionModelImpl() {
      this.listeners = new ArrayList<AlbumSelectionListener>();
   }

   @Override
   public void selectAlbum(Album album) {
      this.fireAlbumSelected(album);
      
   }

   @Override
   public void addAlbumSelectionListener(
         AlbumSelectionListener albumSelectionListener) {
     this.listeners.add(albumSelectionListener);
      
   }
   
   protected void fireAlbumSelected(Album album) {
      for (AlbumSelectionListener albumSelectionListener : this.listeners) {
         albumSelectionListener.albumSelected(album);
      }
   }

}
