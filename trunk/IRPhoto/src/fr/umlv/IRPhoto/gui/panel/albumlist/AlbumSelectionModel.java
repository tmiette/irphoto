package fr.umlv.IRPhoto.gui.panel.albumlist;

import fr.umlv.IRPhoto.album.Album;

public interface AlbumSelectionModel {

   public void addAlbumSelectionListener(AlbumSelectionListener albumSelectionListener);
   
   public void selectAlbum(Album album);
}
