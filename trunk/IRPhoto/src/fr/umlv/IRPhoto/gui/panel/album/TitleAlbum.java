package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class TitleAlbum implements ContainerInitializer {

	private final JPanel panel;
	private final JPanel albumNamePanel;
	private final PhotoListView photoListView;
	private final Album album;
	private JLabel albumTitle;

	public TitleAlbum(Album album) {
		this.album = album;

		// Contains album name and button displaying photos
		this.albumNamePanel = createTitlePanel(this.album.getName());

		// Contains panel with photos and search & sort buttons
		this.photoListView = createPhotoListPanel();

		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.add(this.albumNamePanel);
		this.panel.add(this.photoListView.getPanel());
		this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void addPhoto(Photo photo) {
		this.photoListView.addPhoto(photo);
	}
	
	public void refreshView() {
		this.albumTitle.setText(this.album.getName());
		this.photoListView.refresh();
		this.panel.validate();
	}

	private PhotoListView createPhotoListPanel() {
		PhotoListView view = new PhotoListView(this.album);
		view.getPanel().setVisible(false);
		view.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
		return view;
	}

	private JPanel createTitlePanel(String title) {
		final JPanel jp = new JPanel(new BorderLayout());
		jp.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.albumTitle = new JLabel(title);
		jp.add(this.albumTitle, BorderLayout.WEST);

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
	
	public Album getAlbum() {
		return this.album;
	}

	@Override
	public JComponent initialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
