package fr.umlv.IRPhoto.gui.panel.album;

import javax.swing.JFrame;

public class TestAlbum {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		TitleAlbum ta = new TitleAlbum("titre");
//		PhotoPreview pp = new PhotoPreview();
//		AlbumList al = new AlbumList();
		frame.getContentPane().add(ta.getPanel());
		
		frame.pack();
		frame.setVisible(true);
	}
}
