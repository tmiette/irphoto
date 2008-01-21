package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoSelectionModel;

public class FeaturesContainer implements ContainerInitializer {

  private final JTextField latitudeField;
  private final JTextField longitudeField;
  private final JButton submit;
  private final JLabel nameLabel;
  private final JLabel formatLabel;
  private final JLabel dimensionsLabel;
  private final PhotoSelectionModel model;
  private Photo photo;
  private ImageScaledToPanel image;

  public FeaturesContainer(PhotoSelectionModel model) {

    this.latitudeField = createTextField();
    this.longitudeField = createTextField();
    this.submit = new JButton("OK");
    this.submit.setToolTipText("Validate the new coordinates.");
    this.submit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Photo photo = getPhoto();
        GeoPosition geo = photo.getGeoPosition();
        double latitude;
        double longitude;
        try {
          longitude = Double.parseDouble(longitudeField.getText());
          latitude = Double.parseDouble(latitudeField.getText());
          if (geo == null) {
            photo.setGeoPosition(new GeoPosition(latitude, longitude));
          } else {
            photo.getGeoPosition().setLatitude(latitude);
            photo.getGeoPosition().setLongitude(longitude);
          }
        } catch (NumberFormatException e1) {
          longitudeField.setText("");
          latitudeField.setText("");
        }
      }
    });

    this.nameLabel = new JLabel();
    this.formatLabel = new JLabel();
    this.dimensionsLabel = new JLabel();
    this.image = new ImageScaledToPanel(null);

    this.model = model;
    this.model.addPhotoSelectionListener(new PhotoSelectionListener() {

      @Override
      public void photoSelected(Photo photo) {
        setPhoto(photo);
        if (photo.getGeoPosition() != null) {
          latitudeField.setText(photo.getGeoPosition().getLatitude() + "");
          longitudeField.setText(photo.getGeoPosition().getLongitude() + "");
        }
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

    final JPanel coordinatesPanel = new JPanel(null);
    coordinatesPanel
        .setLayout(new BoxLayout(coordinatesPanel, BoxLayout.Y_AXIS));
    coordinatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.LOWERED), "Coordinates :"));
    coordinatesPanel.add(new JLabel("Latitude"));
    coordinatesPanel.add(this.latitudeField);
    coordinatesPanel.add(new JLabel("Longitude"));
    coordinatesPanel.add(this.longitudeField);
    coordinatesPanel.add(this.submit);

    mainPanel.add(this.image, BorderLayout.WEST);
    mainPanel.add(featuresPanel, BorderLayout.CENTER);
    mainPanel.add(coordinatesPanel, BorderLayout.EAST);

    mainPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        image.setPreferredSize(new Dimension((int) (mainPanel.getSize()
            .getWidth() / 3), (int) mainPanel.getSize().getHeight()));
      }
    });

    return mainPanel;
  }
}
