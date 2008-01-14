package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.umlv.IRPhoto.album.Album;

public class TitleAlbum {

	private final JPanel panel;
	private final JPanel albumNamePanel;
	private final PhotoListView photoListView;

	public TitleAlbum(Album album) {
		this.albumNamePanel = createTitlePanel(album.getName());
		this.albumNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.albumNamePanel.setPreferredSize(new Dimension(800, 10));

		this.photoListView = createPhotoListPanel(album);
		this.photoListView.getPanel().setVisible(false);
		this.photoListView.getPanel().setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.add(this.albumNamePanel);
		this.panel.add(this.photoListView.getPanel());
//		this.panel.add(Box.createVerticalGlue());
	}

	private PhotoListView createPhotoListPanel(Album album) {
		PhotoListModel model = new PhotoListModel(album);
		PhotoListView view = new PhotoListView(model);
		return view;
	}

	private JPanel createTitlePanel(String title) {
		final JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

		jp.add(new JLabel(title));
		jp.add(Box.createHorizontalGlue());
		final JTextField textField = new JTextField(2);
		textField.setHorizontalAlignment(JTextField.LEADING);
		textField.setColumns(20);
		jp.add(textField);

		final JButton alphaSortButton = createAlphaSortButton();
		jp.add(alphaSortButton);

		final JButton typeSortButton = createTypeSortButton();
		jp.add(typeSortButton);

		final JButton dateSortButton = createDateSortButton();
		jp.add(dateSortButton);

		final JButton showPhotoList = createShowPhotoListButton();
		jp.add(showPhotoList);
		return jp;
	}

	private JButton createDateSortButton() {
		final JButton b = new JButton("date");
		return b;
	}

	private JButton createTypeSortButton() {
		final JButton b = new JButton("type");
		return b;
	}

	private JButton createAlphaSortButton() {
		final JButton b = new JButton("alpha");
		return b;
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
