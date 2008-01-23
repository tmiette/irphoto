package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.listener.PhotoSelectionListener;

public class FeaturesContainer implements ContainerInitializer {

  private static final Font boldFont = new Font(null, Font.BOLD, 12);
  private final JComponent container;
  private final JTextField latitudeField;
  private final JTextField longitudeField;
  private final JButton submit;
  private final JLabel nameLabel;
  private final JLabel formatLabel;
  private final JLabel dimensionsLabel;
  private final PhotoModel model;
  private Photo photo;
  private ImageScaledToPanel image;

  public FeaturesContainer(final PhotoModel model) {

    this.latitudeField = createTextField();
    this.longitudeField = createTextField();
    this.submit = new JButton("OK", IconFactory.getIcon("globe-32x32.png"));
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
            model.geopositionUpdated(photo);
          } else {
            photo.getGeoPosition().setLatitude(latitude);
            photo.getGeoPosition().setLongitude(longitude);
            model.geopositionUpdated(photo);
          }
        } catch (NumberFormatException e1) {
          longitudeField.setText("");
          latitudeField.setText("");
        }
      }
    });

    this.nameLabel = createInfosValueLabel(null);
    this.formatLabel = createInfosValueLabel(null);
    this.dimensionsLabel = createInfosValueLabel(null);
    this.image = new ImageScaledToPanel(null);
    this.image.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);

    this.model = model;
    this.model.addPhotoSelectionListener(new PhotoSelectionListener() {

      @Override
      public void photoSelected(Photo photo) {
        setPhoto(photo);
        if (photo.getGeoPosition() != null) {
          latitudeField.setText(photo.getGeoPosition().getLatitude() + "");
          longitudeField.setText(photo.getGeoPosition().getLongitude() + "");
        } else {
          longitudeField.setText("");
          latitudeField.setText("");
        }
        nameLabel.setText(photo.getName());
        formatLabel.setText(photo.getType());
        dimensionsLabel.setText(photo.getDimension().getWidth() + "px * "
            + photo.getDimension().getHeight() + "px");
        image.setImage(photo.getImageIcon().getImage());
        image.repaint();
      }

    });

    this.container = new JPanel(new BorderLayout());

    final JPanel featuresNamesPanel = new JPanel(null);
    featuresNamesPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    featuresNamesPanel.setMinimumSize(new Dimension(0, 0));
    featuresNamesPanel.setLayout(new BoxLayout(featuresNamesPanel,
        BoxLayout.Y_AXIS));
    featuresNamesPanel.add(createInfosLabel("Name :"));
    featuresNamesPanel.add(Box.createVerticalStrut(5));
    featuresNamesPanel.add(createInfosLabel("Type :"));
    featuresNamesPanel.add(Box.createVerticalStrut(5));
    featuresNamesPanel.add(createInfosLabel("Dimensions :"));

    final JPanel featuresPanel = new JPanel(null);
    featuresPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    featuresPanel.setMinimumSize(new Dimension(0, 0));
    featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
    featuresPanel.add(this.nameLabel);
    featuresPanel.add(Box.createVerticalStrut(5));
    featuresPanel.add(this.formatLabel);
    featuresPanel.add(Box.createVerticalStrut(5));
    featuresPanel.add(this.dimensionsLabel);

    final JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    gridPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    gridPanel.add(featuresNamesPanel);
    gridPanel.add(featuresPanel);

    final JScrollPane scrollGridPane = new JScrollPane(gridPanel,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollGridPane.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    scrollGridPane.setBorder(BorderFactory.createTitledBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.LOWERED), "Infos :"));

    final JPanel coordinatesPanel = new JPanel(new GridLayout(5, 1));
    coordinatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.LOWERED), "Coordinates :"));
    coordinatesPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    coordinatesPanel.add(new JLabel("Latitude"));
    coordinatesPanel.add(this.latitudeField);
    coordinatesPanel.add(new JLabel("Longitude"));
    coordinatesPanel.add(this.longitudeField);
    coordinatesPanel.add(this.submit);
    final JPanel northCoordinates = new JPanel(new BorderLayout());
    northCoordinates.setBackground(GraphicalConstants.DEFAULT_BACKGROUND);
    northCoordinates.add(coordinatesPanel, BorderLayout.NORTH);

    this.container.add(this.image, BorderLayout.WEST);
    this.container.add(scrollGridPane, BorderLayout.CENTER);
    this.container.add(northCoordinates, BorderLayout.EAST);

    this.container.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        image.setPreferredSize(new Dimension((int) (container.getSize()
            .getWidth() / 3), (int) container.getSize().getHeight()));
        image.revalidate();
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

  private static JLabel createInfosLabel(String text) {
    final JLabel label = new JLabel(text);
    label.setFont(boldFont);
    label.setForeground(GraphicalConstants.DEFAULT_INFOS_LABEL_COLOR);
    return label;
  }

  private static JLabel createInfosValueLabel(String text) {
    final JLabel label = new JLabel(text);
    label.setFont(boldFont);
    return label;
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }
}
