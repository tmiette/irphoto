package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Component;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PhotoPreview {

	private final JLabel name; //without extension
	private final URL url;
	private final JPanel panel;
	
	// TODO constructor with photo in argument
	public PhotoPreview() {
		this.name = new JLabel("nom");
		this.name.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.url = PhotoPreview.class.getResource("logo.gif");
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel photo = new JLabel();
		photo.setIcon(new ImageIcon(this.url));
		photo.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panel.add(photo);
		this.panel.add(this.name);
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
}
