package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.album.Photo.GeoPosition;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.GraphicalConstants;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.PhotoSelectionListener;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.PhotoUpdateListener;

/**
 * 
 * This class represents the panel with display photos features (dimension,
 * miniature, name, type, ...). This panel enable to change the coordinates of
 * the photo.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class FeaturesContainer implements ContainerInitializer {

  /**
   * Creates and returns a title label for this panel.
   * 
   * @param text
   *            the text of the label.
   * @return the label created.
   */
  private static JLabel createInfosLabel(String text) {
    final JLabel label = new JLabel(text);
    label.setFont(GraphicalConstants.BOLD_FONT);
    label.setForeground(GraphicalConstants.DEFAULT_INFOS_LABEL_COLOR);
    return label;
  }

  /**
   * Creates and returns a value label for this panel.
   * 
   * @param text
   *            the text of the label.
   * @return the label created.
   */
  private static JLabel createInfosValueLabel(String text) {
    final JLabel label = new JLabel(text);
    label.setFont(GraphicalConstants.BOLD_FONT);
    return label;
  }

  /**
   * Creates and returns a text field for this panel.
   * 
   * @return the text field created.
   */
  private static JTextField createTextField() {
    final JTextField field = new JTextField();
    field.setPreferredSize(new Dimension(100, 20));
    return field;
  }

  // main container
  private final JComponent container;

  // image dimensions field
  private final JLabel dimensionsLabel;

  // file type field
  private final JLabel formatLabel;

  // scaled image
  private ImageScaledPanel image;

  // latitude field
  private final JTextField latitudeField;

  // longitude field
  private final JTextField longitudeField;

  // file name field
  private final JLabel nameLabel;

  /**
   * Constructor of the container.
   * 
   * @param albumModel
   *            the album model.
   */
  public FeaturesContainer(final AlbumModel albumModel) {

    // initialize coordinates text fields
    this.latitudeField = createTextField();
    this.longitudeField = createTextField();
    final JButton submit = new JButton("OK", IconFactory
        .getIcon("globe-12x12.png"));
    submit.setToolTipText("Validate the new coordinates.");
    submit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (albumModel.getSelectedPhoto() != null) {
          // check values typed
          GeoPosition g = GeoPosition.validateCoordinates(latitudeField
              .getText(), longitudeField.getText());
          if (g != null) {
            // informs model of the coordinates change
            albumModel.updateGeoPosition(albumModel.getSelectedPhoto(), g);
          } else {
            longitudeField.setText("");
            latitudeField.setText("");
          }
        }
      }
    });
    // initialize coordinates panel
    final JPanel coordinatesPanel = new JPanel(new GridLayout(5, 1));
    coordinatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.LOWERED), "Coordinates :"));
    coordinatesPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    coordinatesPanel.add(new JLabel("Latitude"));
    coordinatesPanel.add(this.latitudeField);
    coordinatesPanel.add(new JLabel("Longitude"));
    coordinatesPanel.add(this.longitudeField);
    coordinatesPanel.add(submit);
    final JPanel northCoordinates = new JPanel(new BorderLayout());
    northCoordinates.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    northCoordinates.add(coordinatesPanel, BorderLayout.NORTH);

    // initialize panel with scaled image (null at first)
    this.image = new ImageScaledPanel(null);
    this.image.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);

    // initialize features fields
    this.nameLabel = createInfosValueLabel(null);
    this.formatLabel = createInfosValueLabel(null);
    this.dimensionsLabel = createInfosValueLabel(null);

    // initialize the features panel
    final JPanel featuresNamesPanel = new JPanel(null);
    featuresNamesPanel
        .setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    featuresNamesPanel.setMinimumSize(new Dimension(0, 0));
    featuresNamesPanel.setLayout(new BoxLayout(featuresNamesPanel,
        BoxLayout.Y_AXIS));
    featuresNamesPanel.add(createInfosLabel("Name :"));
    featuresNamesPanel.add(Box.createVerticalStrut(5));
    featuresNamesPanel.add(createInfosLabel("Type :"));
    featuresNamesPanel.add(Box.createVerticalStrut(5));
    featuresNamesPanel.add(createInfosLabel("Dimensions :"));

    final JPanel featuresPanel = new JPanel(null);
    featuresPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    featuresPanel.setMinimumSize(new Dimension(0, 0));
    featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
    featuresPanel.add(this.nameLabel);
    featuresPanel.add(Box.createVerticalStrut(5));
    featuresPanel.add(this.formatLabel);
    featuresPanel.add(Box.createVerticalStrut(5));
    featuresPanel.add(this.dimensionsLabel);

    final JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    gridPanel.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    gridPanel.add(featuresNamesPanel);
    gridPanel.add(featuresPanel);

    final JScrollPane scrollGridPane = new JScrollPane(gridPanel,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollGridPane.setBackground(GraphicalConstants.DEFAULT_BACKGROUND_COLOR);
    scrollGridPane.setBorder(BorderFactory.createTitledBorder(BorderFactory
        .createEtchedBorder(EtchedBorder.LOWERED), "Infos :"));

    // initialize main container
    this.container = new JPanel(new BorderLayout());
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

    // listen to photos selections
    albumModel.addPhotoSelectionListener(new PhotoSelectionListener() {
      @Override
      public void photoSelected(Photo photo) {
        // update all features fields with the new photo features
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

    // listen to photos modifications
    albumModel.addPhotoUpdatedListener(new PhotoUpdateListener() {
      @Override
      public void geopPositionUpdated(Photo photo, GeoPosition geo) {
        // update the coordinates fields with the new values
        latitudeField.setText(geo.getLatitude() + "");
        longitudeField.setText(geo.getLongitude() + "");
      }
    });

    // listen to albums modifications
    albumModel.addAlbumListener(new AlbumListener() {
      @Override
      public void albumAdded(Album album) {
        // do nothing
      }

      @Override
      public void albumRemoved(Album album) {
        // if the album removed contains the current displayed photo, the photo
        // is erased too
        if (albumModel.getSelectedPhoto() != null
            && albumModel.getSelectedPhoto().getAlbum().equals(album)) {
          eraseFields();
        }
      }
    });

  }

  /**
   * Erases all values currently displayed in the different fields when none
   * photo is selected.
   */
  private void eraseFields() {
    longitudeField.setText("");
    latitudeField.setText("");
    nameLabel.setText("");
    formatLabel.setText("");
    dimensionsLabel.setText("");
    image.setImage(null);
    image.repaint();
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }
}
