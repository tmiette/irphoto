package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Main;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.album.PhotoUpdatedListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoUpdatedModel;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;

public class MapContainer implements ContainerInitializer {

  private final JPanel mainPanel;
  private static final Logger logger = Logger.getLogger(Main.loggerName);
  private final JXMapViewer map;
  private final JPanel photoPanel;
  private GeoPosition currentPosition;
  private final AlbumSelectionModel albumSelectionModel;
  private final PhotoUpdatedModel photoUpdatedModel;
  private Album currentAlbum;
  private WaypointPainter<JXMapViewer> currentPainter;
  private final JComponent photoListContainer;

  public MapContainer(AlbumSelectionModel albumSelectionModel,
      PhotoUpdatedModel photoUpdatedModel) {
    this.photoUpdatedModel = photoUpdatedModel;
    this.photoUpdatedModel.addPhotoUpdatedListener(new PhotoUpdatedListener() {

      @Override
      public void geoppositionUpdated(Photo photo) {
        logger.info("Photo updated : new geoposition coordinate");
        addPhoto(photo);
        map.repaint();
      }

      @Override
      public void nameUpdated(Photo photo) {
        // nothing

      }

    });

    this.photoPanel = new JPanel(new BorderLayout());
//    this.photoPanel.setOpaque(false);

    this.currentPosition = new GeoPosition(43.604503, 1.444026);

    this.albumSelectionModel = albumSelectionModel;
    this.albumSelectionModel
        .addAlbumSelectionListener(new AlbumSelectionListener() {
          @Override
          public void albumSelected(Album album) {
            logger.info("Album selected");
            if (currentAlbum != null && !currentAlbum.equals(album)) {
              currentPainter.setVisible(false);
              currentAlbum = null;
              currentPainter = null;
            }
            addAlbum(album);
            map.repaint();
          }
        });

    MyJXMapKit kit = new MyJXMapKit();
    this.map = kit.getMainMap();

//    final JPanel leftPanel = new JPanel(new BorderLayout());
//    leftPanel.add(createCollapseButton(), BorderLayout.WEST);
    this.photoListContainer = ContainerFactory
        .createPhotoWithoutGPListContainer();
//    leftPanel.add(this.photoListContainer, BorderLayout.CENTER);
//    this.photoPanel.add(leftPanel, BorderLayout.WEST);

    final JLabel hoverLabel = new JLabel("Java");
    hoverLabel.setVisible(false);
    this.map.add(hoverLabel);

    this.map.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
      }

      public void mouseMoved(MouseEvent e) {
        // location of Java
        GeoPosition gp = currentPosition;
        // convert to world bitmap
        Point2D gp_pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
        // convert to screen
        Rectangle rect = map.getViewportBounds();
        Point converted_gp_pt = new Point((int) gp_pt.getX() - rect.x,
            (int) gp_pt.getY() - rect.y);
        // check if near the mouse
        if (converted_gp_pt.distance(e.getPoint()) < 10) {
          hoverLabel.setLocation(converted_gp_pt);
          hoverLabel.setVisible(true);
        } else {
          hoverLabel.setVisible(false);
        }
      }
    });
    
    // ///////////
     JPanel jp = new JPanel(new GridBagLayout());
     JButton b = new JButton(">");
     b.setPreferredSize(new Dimension(10, 50));
    
     GridBagConstraints gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
     // gridBagConstraints.weightx = 1.0;
     // gridBagConstraints.weighty = 1.0;
     // gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
     this.map.add(b, gridBagConstraints);
    
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
     gridBagConstraints.weightx = 1.0;
     gridBagConstraints.weighty = 1.0;
     gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
     this.map.add(jp, gridBagConstraints);
     
     gridBagConstraints = new java.awt.GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 0;
//     gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
     gridBagConstraints.weightx = 1.0;
     gridBagConstraints.weighty = 1.0;
     gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
     this.map.add(this.photoListContainer, gridBagConstraints);

    // ////////

    this.mainPanel = new JPanel(new BorderLayout());
    this.mainPanel.add(this.map, BorderLayout.CENTER);
  }

  /**
   * Creates a button which sets visible or not the photos list on map.
   * 
   * @return button
   */
  private JComponent createCollapseButton() {
    final JButton b = new JButton();
    b.setIcon(IconFactory.getIcon("arrow-right-12x12.gif"));
    b.setPreferredSize(new Dimension(12, 0));

    b.addActionListener(new ActionListener() {
      /*
       * (non-Javadoc)
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean bool = photoListContainer.isVisible();
        if (bool) {
          b.setIcon(IconFactory.getIcon("arrow-right-12x12.gif"));
        } else {
          b.setIcon(IconFactory.getIcon("arrow-left-12x12.gif"));
        }
        photoListContainer.setVisible(!bool);
      }
    });

    return b;
  }

  /**
   * Adds photos belonging to album on map.
   * 
   * @param album album containing photos to add on map
   */
  public void addAlbum(Album album) {
    if (this.currentAlbum != null && this.currentAlbum.equals(album)) {
      // displaying waypoints on map
      currentPainter.setVisible(true);
    } else {
      // create a Set of Waypoints
      Set<Waypoint> waypoints = new HashSet<Waypoint>();

      // adding photo waypoint
      for (Photo photo : album.getPhotos()) {
        if (photo.getGeoPosition() != null) {
          // Adding valid geoposition corrdinates
          waypoints.add(new Waypoint(photo.getGeoPosition().getLatitude(),
              photo.getGeoPosition().getLongitude()));
        }

      }

      // create a WaypointPainter to draw the points
      WaypointPainter<JXMapViewer> painter = new WaypointPainter<JXMapViewer>();
      painter.setWaypoints(waypoints);

      // Save current album to future display
      this.currentAlbum = album;

      // Save current painter to future waypoints display
      this.currentPainter = painter;

      // Display waypoints
      this.map.setOverlayPainter(painter);
    }
  }

  /**
   * Adds photo geoposition coordinates to current painter to display the new
   * photo on the map.
   * 
   * @param photo photo to add on map
   */
  public void addPhoto(Photo photo) {
    for (Photo ph : this.currentAlbum.getPhotos()) {
      if (photo.equals(ph) && photo.getGeoPosition() != null) {
        this.currentPainter.getWaypoints().add(
            new Waypoint(photo.getGeoPosition().getLatitude(), photo
                .getGeoPosition().getLongitude()));
        return;
      }
    }
  }

  @Override
  public JComponent getComponent() {
    // return this.panel;
    return this.mainPanel;
  }

}
