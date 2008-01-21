package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
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
import javax.swing.JLayeredPane;
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

  private final JLayeredPane mainPanel;
  private static final Logger logger = Logger.getLogger(Main.loggerName);
  private final JXMapViewer map;
  private GeoPosition currentPosition;
  private final AlbumSelectionModel albumSelectionModel;
  private final PhotoUpdatedModel photoUpdatedModel;
  private Album currentAlbum;
  private WaypointPainter<JXMapViewer> currentPainter;
  private final JComponent photoListContainer;

  public MapContainer(AlbumSelectionModel albumSelectionModel,
      PhotoUpdatedModel photoUpdatedModel) {
    MyJXMapKit kit = new MyJXMapKit();
    this.map = kit.getMainMap();
    
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

    this.currentPosition = new GeoPosition(43.604503, 1.444026);

    this.albumSelectionModel = albumSelectionModel;
    this.albumSelectionModel
        .addAlbumSelectionListener(new AlbumSelectionListener() {
          @Override
          public void albumSelected(Album album) {
            logger.info("Album selected");
            if (currentAlbum != null && !currentAlbum.equals(album)) {
              logger.info("Album differ from current album");
              currentPainter.setVisible(false);
              currentAlbum = null;
              currentPainter = null;
            }
            addAlbum(album);
            map.repaint();
          }
        });

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
    
    this.map.setAddressLocation(this.currentPosition);
    
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    panel.add(createCollapseButton(), BorderLayout.WEST);
    this.photoListContainer = ContainerFactory
    .createPhotoWithoutGPListContainer();
    this.photoListContainer.setVisible(false);
    panel.add(this.photoListContainer, BorderLayout.CENTER);
    
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setOpaque(false);
    leftPanel.add(panel, BorderLayout.WEST);
    
    this.mainPanel = new JLayeredPane();
    this.mainPanel.setLayout(createLayoutManager());
    this.mainPanel.add(leftPanel, new Integer(1));
    this.mainPanel.add(this.map, new Integer(0));
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
      logger.info("Album already created" + album.getId());
      currentPainter.setVisible(true);
    } else {
      logger.info("Album created");
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
  
  public static LayoutManager createLayoutManager() {
    return new LayoutManager() {
      @Override
      public void addLayoutComponent(String name, Component parent) {
        // nothing
      }

      @Override
      public void removeLayoutComponent(Component parent) {
        // nothing
      }

      @Override
      public void layoutContainer(Container parent) {
        int count = parent.getComponentCount();
        for (int i = 0; i < count; i++) {
          Component component = parent.getComponent(i);
          component.setBounds(0, 0, parent.getWidth(), parent
              .getHeight());
        }
      }

      @Override
      public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
      }

      @Override
      public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        int count = parent.getComponentCount();
        for (int i = 0; i < count; i++) {
          Component c = parent.getComponent(i);
          Dimension preferred = c.getPreferredSize();

          if (preferred.width > width) {
            width = preferred.width;
          }
          if (preferred.height > height) {
            height = preferred.height;
          }

        }
        return new Dimension(width, height);
      }

    };
  }


}
