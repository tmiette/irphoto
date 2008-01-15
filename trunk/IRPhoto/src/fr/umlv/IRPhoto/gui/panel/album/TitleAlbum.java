package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;

public class TitleAlbum {

	private final JPanel panel;
	private final JPanel albumNamePanel;
	private final PhotoListView photoListView;
	private final Album album;

	// panels already created
	private static final ArrayList<TitleAlbum> titleAlbums = new ArrayList<TitleAlbum>();

	private TitleAlbum(Album album) {
		this.album = album;

		this.albumNamePanel = createTitlePanel(this.album.getName());
		this.albumNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.photoListView = createPhotoListPanel();
		this.photoListView.getPanel().setVisible(false);
		this.photoListView.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.add(this.albumNamePanel);
		this.panel.add(this.photoListView.getPanel());
		this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	private PhotoListView createPhotoListPanel() {
		PhotoListModel model = new PhotoListModel(this.album);
		PhotoListView view = new PhotoListView(model);
		return view;
	}

	private JPanel createTitlePanel(String title) {
		final JPanel jp = new JPanel(new BorderLayout());

		jp.add(new JLabel(title), BorderLayout.WEST);

		final JButton showPhotoList = createShowPhotoListButton();
		jp.add(showPhotoList, BorderLayout.EAST);

		return jp;
	}

	private JButton createShowPhotoListButton() {
		final JButton b = new JButton("->");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				TitleAlbum.this.photoListView.getPanel().setVisible(
						!TitleAlbum.this.photoListView.getPanel().isVisible());
			}
		});
		return b;
	}

	public static JPanel getPanel(Album album) {
		for (TitleAlbum titleAlbum : titleAlbums) {
			if (titleAlbum.album.equals(album)) {
				return titleAlbum.panel;
			}
		}

		TitleAlbum titleAlbum = new TitleAlbum(album);
		titleAlbums.add(titleAlbum);
		return titleAlbum.panel;
	}

}
