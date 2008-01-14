package fr.umlv.IRPhoto.gui.panel.album;

import javax.swing.JFrame;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;

public class TestAlbum {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		AlbumListModel alm = new AlbumListModel();
		for (int j = 0; j < 5; j++) {
			Album album = new Album();
			album.setName("mes logos" + j);
			for (int i = 100; i > 0; i--) {
				Photo photo = new Photo();
				photo.setName("logo" + i);
				

				// photo
				// .setPath("/home/akiri/workspace/IRPhoto/classes/fr/umlv/IRPhoto/gui/panel/album/logo.gif");
				photo
				.setPath("/home/akiri/workspace/IRPhoto/src/icons/photo.jpg");
				album.addPhoto(photo);
			}
			alm.addAlbum(album);
		}
		
		PhotoListModel plm = new PhotoListModel(alm.getAlbums().get(0));
		System.out.println(plm.getPhotosSortByName());

		AlbumListView alv = new AlbumListView(alm);
		frame.getContentPane().add(alv.getPanel());
		
		frame.pack();
		frame.setVisible(true);
	}
}
