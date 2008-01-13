package fr.umlv.IRPhoto.gui.panel.album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public class PhotoListModel {

	private final ArrayList<Photo> photos;
	
	public PhotoListModel(Album album) {
		this.photos = new ArrayList<Photo>();
		this.photos.addAll(album.getPhotos());
	}
	
	public List<Photo> getPhotosSortByName() {
		
		
		
		Collections.sort(this.photos, new Comparator() {
		    public int compare(Object o1, Object o2){
			      if(!(o1 instanceof Photo))
			        throw new ClassCastException();
			      return (new Integer(((Photo)o1).getName())).compareTo(
			                        new Integer(((Photo)o2).getName()));
			    }
			});
		return Collections.unmodifiableList(this.photos);
	}
	
}
