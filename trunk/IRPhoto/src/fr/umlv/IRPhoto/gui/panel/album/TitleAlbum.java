package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	public TitleAlbum(Album album) {
		this.albumNamePanel = createTitlePanel(album.getName());
		this.albumNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.photoListView = createPhotoListPanel(album);
		this.photoListView.getPanel().setVisible(false);
		this.photoListView.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.add(this.albumNamePanel);
		this.panel.add(this.photoListView.getPanel());
		this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	private PhotoListView createPhotoListPanel(Album album) {
		PhotoListModel model = new PhotoListModel(album);
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

	public JPanel getPanel() {
		return this.panel;
	}

}
