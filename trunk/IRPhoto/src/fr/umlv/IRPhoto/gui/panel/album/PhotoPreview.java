package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PhotoPreview {

	private final JLabel name; // without extension
	private final URL url;
	private final JPanel panel;
	// private final JPanel miniature;
	public static final Dimension DEFAULT_MINIATURE_DIMENSION = new Dimension(
			96, 96);

	// TODO constructor with photo in argument
	public PhotoPreview() {
		this.name = new JLabel("nom");
		this.name.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.url = PhotoPreview.class.getResource("logo.gif");

		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ImageIcon icon = new ImageIcon(this.url);
		double ratio = icon.getIconWidth()
				/ DEFAULT_MINIATURE_DIMENSION.getWidth();
		int w = (int) (icon.getIconWidth() / ratio);
		int h = (int) (icon.getIconHeight() / ratio);
		ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(),
				w, h));

		JLabel photo = new JLabel();
		photo.setMaximumSize(DEFAULT_MINIATURE_DIMENSION);
		photo.setIcon(thumbnailIcon);
		photo.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.panel.add(photo);
		this.panel.add(this.name);
	}

	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * 
	 * @param srcImg -
	 *            source image to scale
	 * @param w -
	 *            desired width
	 * @param h -
	 *            desired height
	 * @return - the new resized image
	 */
	// private Image getScaledImage(Image srcImg, int w, int h) {
	// BufferedImage resizedImg = new BufferedImage(w, h,
	// BufferedImage.TYPE_INT_RGB);
	// Graphics2D g2 = resizedImg.createGraphics();
	// g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	// g2.drawImage(srcImg, 0, 0, w, h, null);
	// g2.dispose();
	// return resizedImg;
	// }
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(
				DEFAULT_MINIATURE_DIMENSION.width,
				DEFAULT_MINIATURE_DIMENSION.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		int x = DEFAULT_MINIATURE_DIMENSION.width - w;
		int y = DEFAULT_MINIATURE_DIMENSION.height - h;
		g2.drawImage(srcImg, x/2, y/2, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	public JPanel getPanel() {
		return this.panel;
	}
}
