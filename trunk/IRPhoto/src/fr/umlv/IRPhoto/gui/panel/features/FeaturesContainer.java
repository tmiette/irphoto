package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModel;

public class FeaturesContainer implements ContainerInitializer {

   private final JTextField latitudeField;
   private final JTextField longitudeField;
   private final JLabel nameLabel;
   private final JLabel formatLabel;
   private final JLabel dimensionsLabel;
   private final PhotoSelectionModel model;
   private Photo photo;
   private ImageScaledToPanel image;
   public static final Image DEFAULT_IMAGE = new BufferedImage(50, 50,
         BufferedImage.TYPE_INT_RGB);

   public FeaturesContainer(PhotoSelectionModel model) {

    this.latitudeField = createTextField();
    this.latitudeField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final Photo photo = getPhoto();
        try {
          photo.setLatitude(Double.parseDouble(latitudeField.getText()));
        } catch (NumberFormatException e1) {
          latitudeField.setText(photo.getLatitude() + "");
        }
      }
    });
    this.longitudeField = createTextField();
    this.longitudeField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final Photo photo = getPhoto();
        try {
          photo.setLongitude(Double.parseDouble(longitudeField.getText()));
        } catch (NumberFormatException e1) {
          longitudeField.setText(photo.getLatitude() + "");
        }
      }
    });
    this.nameLabel = new JLabel();
    this.formatLabel = new JLabel();
    this.dimensionsLabel = new JLabel();
    this.image = new ImageScaledToPanel(DEFAULT_IMAGE);

    this.model = model;
    this.model.addPhotoSelectionListener(new PhotoSelectionListener() {

      @Override
      public void photoSelected(Photo photo) {
        setPhoto(photo);
        latitudeField.setText(photo.getLatitude() + "");
        longitudeField.setText(photo.getLongitude() + "");
        nameLabel.setText(photo.getName());
        formatLabel.setText(photo.getType());
        dimensionsLabel.setText(photo.getDimension().getWidth() + "px * "
            + photo.getDimension().getHeight() + "px");
        image.setImage(photo.getImageIcon().getImage());
        image.repaint();
      }

    });
  }

   public Photo getPhoto() {
      return this.photo;
   }

   private void setPhoto(Photo photo) {
      this.photo = photo;
   }

   private static JTextField createTextField() {
      final JTextField field = new JTextField();
      field.setPreferredSize(new Dimension(100, 20));
      return field;
   }

   @Override
   public JComponent getComponent() {
      final JPanel mainPanel = new JPanel(new BorderLayout());

      final JPanel featuresPanel = new JPanel(null);
      featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
      featuresPanel.add(this.nameLabel);
      featuresPanel.add(this.formatLabel);
      featuresPanel.add(this.dimensionsLabel);

      final JPanel coordinatesPanel = new JPanel(new GridLayout(4, 1));
      coordinatesPanel.add(new JLabel("Latitude"));
      coordinatesPanel.add(this.latitudeField);
      coordinatesPanel.add(new JLabel("Longitude"));
      coordinatesPanel.add(this.longitudeField);

      mainPanel.add(image, BorderLayout.WEST);
      mainPanel.add(featuresPanel, BorderLayout.CENTER);
      mainPanel.add(coordinatesPanel, BorderLayout.EAST);

      mainPanel.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            // TODO resize icon
         }
         /*
          * ImageIcon icon = photo.getImageIcon(); double ratioHeight =
          * icon.getIconHeight() / mainPanel.getHeight(); int w = (int)
          * (icon.getIconWidth() / ratioHeight); int h = (int)
          * (icon.getIconHeight() / ratioHeight); iconLabel.setIcon(new
          * ImageIcon(icon.getImage().getScaledInstance(w, h,
          * Image.SCALE_DEFAULT))); }
          */
      });

      return mainPanel;
   }

}
