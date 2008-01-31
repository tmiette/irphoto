package fr.umlv.IRPhoto.gui.view.map;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumListener;
import fr.umlv.IRPhoto.gui.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.model.album.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.gui.view.ContainerInitializer;
import fr.umlv.IRPhoto.util.DefaultLayeredPaneLayoutManager;
import fr.umlv.IRPhoto.util.IconFactory;

/**
 * 
 * This class represents the container which manage the map viewer and photos
 * coordinates.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class MapViewerContainer implements ContainerInitializer {

  // default location of the roll over miniature
  private static final Point DEFAULT_THUMBNAIL_LOCATION = new Point(-100, -100);

  // over area rectangle of the waypoints
  private static final Dimension OVER_RECTANGLE_DIMENSION = new Dimension(20,
      20);

  // flag to show/hide transparent miniatures
  private static final boolean SHOW_TRANSPARENT_MINIATURES = true;

  // tear icon for waypoints
  private static final Image TEAR_ICON = ((ImageIcon) IconFactory
      .getIcon("tear.png")).getImage();

  /**
   * Converts a geo position to a point.
   * 
   * @param map
   *            the map viewer.
   * @param gp
   *            the geo position to convert.
   * @return the point.
   */
  private static Point convertGeoPositionToPoint(JXMapViewer map, GeoPosition gp) {
    // convert to world bitmap
    final Point2D p2D = map.getTileFactory().geoToPixel(gp, map.getZoom());
    // convert to screen
    final Rectangle rect = map.getViewportBounds();
    return new Point((int) p2D.getX() - rect.x, (int) p2D.getY() - rect.y);
  }

  /**
   * Returns if the mouse point is contained in the over area of a way point.
   * This method is called for the mouse events to know if the roll over event
   * has to be launch.
   * 
   * @param waypoint
   *            the way point point.
   * @param mouse
   *            the mouse point.
   * @return if the mouse point is contained in the over area.
   */
  private static boolean isOverWaypoint(Point waypoint, Point mouse) {
    // creates the rectangle of over area for this waypoint
    final Rectangle r = new Rectangle(new Point(waypoint.x - 10,
        waypoint.y - 30), OVER_RECTANGLE_DIMENSION);
    return r.contains(mouse);
  }

  // main container
  private final JLayeredPane container;

  // map viewer container
  private final JXMapViewer map;

  // waypoints painter
  private final WaypointPainter<JXMapViewer> painter;

  // set of all waypoints
  private final HashSet<Waypoint> waypoints;

  // map to store photos associated with a waypoint
  private final HashMap<Waypoint, Photo> waypointsMap;

  /**
   * Constructor of this container.
   * 
   * @param albumModel
   *            the album model.
   */
  public MapViewerContainer(final AlbumModel albumModel) {

    // sets to store waypoints
    this.waypointsMap = new HashMap<Waypoint, Photo>();
    this.waypoints = new HashSet<Waypoint>();

    // waypoints painter
    this.painter = new WaypointPainter<JXMapViewer>();
    // show transparent miniatures

    this.painter.setRenderer(new WaypointRenderer() {
      @Override
      public boolean paintWaypoint(Graphics2D g, JXMapViewer map,
          Waypoint waypoint) {
        // super.paintWaypoint(g, map, waypoint);
        Graphics2D g2D = null;
        try {
          // creates a graphics
          g2D = (Graphics2D) g.create();
          // get the photo associated to this photo
          final Photo photo = waypointsMap.get(waypoint);
          if (photo != null) {
            // draws tear icon
            g2D.drawImage(TEAR_ICON, -(TEAR_ICON.getWidth(null) / 2), -(TEAR_ICON
                .getHeight(null)), TEAR_ICON.getWidth(null), TEAR_ICON
                .getHeight(null), null);
            if (SHOW_TRANSPARENT_MINIATURES) {
              // draws the transparent image
              final Image i = photo.getScaledInstance();
              g2D = (Graphics2D) g.create();
              g2D.setComposite(AlphaComposite.getInstance(
                  AlphaComposite.SRC_ATOP, 0.5f));
              g2D.drawImage(i, -(i.getWidth(null) / 2),
                  -(i.getHeight(null) + 20), i.getWidth(null), i
                      .getHeight(null), null);
            }
          }
        } finally {
          g2D.dispose();
        }
        return true;
      }
    });

    // initialize thumb nail panel
    final JLabel thumbnailLabel = new JLabel();
    // thumbnailLabel.setVisible(false);
    thumbnailLabel.setLocation(DEFAULT_THUMBNAIL_LOCATION);
    final JPanel thumbnailPanel = new JPanel();
    thumbnailPanel.setOpaque(false);
    thumbnailPanel.add(thumbnailLabel);

    // initialize map viewer
    this.map = new IRPhotoJXMapKit().getMainMap();
    // add a mouse motion listener to show/hide photos miniatures
    this.map.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        // for each waypoint
        for (Entry<Waypoint, Photo> entry : waypointsMap.entrySet()) {
          final Point p = convertGeoPositionToPoint(map, entry.getKey()
              .getPosition());
          // if the mouse is in the over area of the waypoint
          if (isOverWaypoint(p, e.getPoint())) {
            // show the thumb nail panel
            final Icon icon = new ImageIcon(entry.getValue()
                .getScaledInstance());
            thumbnailLabel.setIcon(icon);
            thumbnailLabel.setLocation(p.x - (icon.getIconWidth() / 2), p.y
                - (icon.getIconHeight() + 20));
            break;
          } else {
            thumbnailLabel.setLocation(DEFAULT_THUMBNAIL_LOCATION);
          }
        }
      }
    });
    // add a mouse listener to select photos
    this.map.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // for each waypoint
        for (Entry<Waypoint, Photo> entry : waypointsMap.entrySet()) {
          final Point p = convertGeoPositionToPoint(map, entry.getKey()
              .getPosition());
          // select the photo if the click is in the over area of this waypoint
          if (isOverWaypoint(p, e.getPoint())) {
            albumModel.selectPhoto(entry.getValue());
          }
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // hide thumnail
        thumbnailLabel.setLocation(DEFAULT_THUMBNAIL_LOCATION);
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("OK");
        // hide thumnail
        thumbnailLabel.setLocation(DEFAULT_THUMBNAIL_LOCATION);
      }
    });

    // initialize photos list panel
    final JComponent photoListContainer = new PhotoWithoutGPListContainer(
        albumModel).getContainer();
    photoListContainer.setVisible(false);

    // initialize collapse button
    final JButton collapseButton = new JButton();
    collapseButton.setIcon(IconFactory.getIcon("arrow-right-12x12.gif"));
    collapseButton.setPreferredSize(new Dimension(12, 0));

    collapseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean bool = photoListContainer.isVisible();
        if (bool) {
          collapseButton.setIcon(IconFactory.getIcon("arrow-right-12x12.gif"));
        } else {
          collapseButton.setIcon(IconFactory.getIcon("arrow-left-12x12.gif"));
        }
        photoListContainer.setVisible(!bool);
      }
    });

    // initialize photos list panel container
    final JPanel innerPanel = new JPanel(new BorderLayout());
    innerPanel.setOpaque(false);
    innerPanel.add(collapseButton, BorderLayout.WEST);
    innerPanel.add(photoListContainer, BorderLayout.CENTER);
    final JPanel photosListPanel = new JPanel(new BorderLayout());
    photosListPanel.setOpaque(false);
    photosListPanel.add(innerPanel, BorderLayout.WEST);

    // initialize main container
    this.container = new JLayeredPane();
    this.container.setLayout(new DefaultLayeredPaneLayoutManager());
    this.container.add(thumbnailPanel, new Integer(2));
    this.container.add(photosListPanel, new Integer(1));
    this.container.add(this.map, new Integer(0));

    // listen to albums selection
    albumModel.addAlbumSelectionListener(new AlbumSelectionListener() {
      @Override
      public void albumSelected(Album album) {
        // add all photos with coordinates on the map
        drawAllPhotos(album.getPhotos());
      }
    });

    // listen to albums changes
    albumModel.addAlbumListener(new AlbumListener() {
      @Override
      public void albumAdded(Album album) {
        drawAllPhotos(album.getPhotos());
      }

      @Override
      public void albumRemoved(Album album) {
        if (album.equals(albumModel.getCurrentAlbum())) {
          eraseAllPhotos();
        }
      }
    });

    // listen to coordinates changes
    albumModel.addPhotoUpdatedListener(new PhotoUpdateListener() {
      @Override
      public void geopPositionUpdated(Photo photo,
          fr.umlv.IRPhoto.album.Photo.GeoPosition geo) {
        if (photo.getAlbum().equals(albumModel.getCurrentAlbum())) {
          addPhoto(photo);
        }
      }
    });

  }

  /**
   * 
   * Draws a photo on the map. The photo is drawn at the position describing by
   * its coordinates.
   * 
   * @param photo
   *            photo to draw.
   */
  public void addPhoto(Photo photo) {

    if (photo.getGeoPosition() != null) {
      final Waypoint wp = new Waypoint(photo.getGeoPosition().getLatitude(),
          photo.getGeoPosition().getLongitude());
      this.waypoints.add(wp);
      this.waypointsMap.put(wp, photo);
    }

    // Display waypoints
    this.submitWaypointSet();

  }

  /**
   * Draws all photos with coordinates belonging to an album on the map.
   * 
   * @param photos
   *            list of photos to draw.
   */
  public void drawAllPhotos(List<Photo> photos) {

    // for each photos
    for (Photo photo : photos) {
      // if the photo has coordinates
      if (photo.getGeoPosition() != null) {
        // add it to the waypoint set
        final Waypoint wp = new Waypoint(photo.getGeoPosition().getLatitude(),
            photo.getGeoPosition().getLongitude());
        this.waypoints.add(wp);
        this.waypointsMap.put(wp, photo);
      }
    }

    // Display waypoints
    this.submitWaypointSet();
  }

  /**
   * Erases all photos miniatures already drawn on the map.
   */
  private void eraseAllPhotos() {
    // clears store sets
    this.waypointsMap.clear();
    this.waypoints.clear();
    // erases waypoints
    this.submitWaypointSet();
  }

  @Override
  public JComponent getContainer() {
    return this.container;
  }

  /**
   * Submit the waypoints set to the map to draw them.
   */
  private void submitWaypointSet() {
    this.painter.setWaypoints(this.waypoints);
    this.map.setOverlayPainter(this.painter);
  }

}
