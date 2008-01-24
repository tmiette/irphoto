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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.IconFactory;
import fr.umlv.IRPhoto.gui.panel.model.album.AlbumModel;
import fr.umlv.IRPhoto.gui.panel.model.album.listener.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.model.photo.PhotoModel;
import fr.umlv.IRPhoto.gui.panel.model.photo.listener.PhotoUpdateListener;
import fr.umlv.IRPhoto.util.ImageUtil;

public class MapContainer implements ContainerInitializer {

  private final JLayeredPane mainPanel;
  private static final Logger logger = Logger.getLogger(Main.loggerName);
  private final JXMapViewer map;
  private GeoPosition currentPosition;
  private final AlbumModel albumModel;
  private final PhotoModel photoModel;
  private final JComponent photoListContainer;
  private HashMap<Waypoint, Photo> waypoints;
  private final JLabel thumbnail;

  public MapContainer(AlbumModel albumModel, PhotoModel photoModel) {
    MyJXMapKit kit = new MyJXMapKit();
    this.map = kit.getMainMap();

    this.photoModel = photoModel;
    this.photoModel.addPhotoUpdatedListener(new PhotoUpdateListener() {

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

    this.waypoints = new HashMap<Waypoint, Photo>();
    this.thumbnail = new JLabel();
    this.thumbnail.setVisible(false);
    this.map.add(this.thumbnail);

    this.currentPosition = new GeoPosition(43.604503, 1.444026);

    this.albumModel = albumModel;
    this.albumModel.addAlbumSelectionListener(new AlbumSelectionListener() {
      @Override
      public void albumSelected(Album album) {
        logger.info("Album selected");
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

        // // location of Java
        // GeoPosition gp = currentPosition;
        // // convert to world bitmap
        // Point2D gp_pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
        // // convert to screen
        // Rectangle rect = map.getViewportBounds();
        // Point converted_gp_pt = new Point((int) gp_pt.getX() - rect.x,
        // (int) gp_pt.getY() - rect.y);
        // System.out.println(new Point((int) gp_pt.getX() -
        // map.getViewportBounds().x,
        // (int) gp_pt.getY() - map.getViewportBounds().y));
        // // check if near the mouse
        Point currentPoint;
        for (Entry<Waypoint, Photo> wp : waypoints.entrySet()) {
          if (convertGP2Point(wp.getKey().getPosition()).distance(
              currentPoint = e.getPoint()) < 10) {
            // hoverLabel.setLocation(converted_gp_pt);
            thumbnail.setIcon(getImageFromWP(wp.getKey()));
            thumbnail.setLocation(e.getPoint());
            thumbnail.setVisible(true);
          } else {
            // thumbnail.setVisible(false);
          }
        }
        // if (converted_gp_pt.distance(e.getPoint()) < 10) {
        // hoverLabel.setLocation(converted_gp_pt);
        // hoverLabel.setVisible(true);
        // } else {
        // hoverLabel.setVisible(false);
        // }
      }

      private Icon getImageFromWP(Waypoint wp) {
        return new ImageIcon(ImageUtil.getScaledImage(waypoints.get(wp)
            .getImageIcon().getImage(), 50, 50));
      }

      private Point2D convertGP2Point(GeoPosition position) {
        // location of Java
        GeoPosition gp = position;
        // convert to world bitmap
        Point2D gp_pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
        // convert to screen
        Rectangle rect = map.getViewportBounds();
        Point converted_gp_pt = new Point((int) gp_pt.getX() - rect.x,
            (int) gp_pt.getY() - rect.y);
        System.out.println(new Point((int) gp_pt.getX()
            - map.getViewportBounds().x, (int) gp_pt.getY()
            - map.getViewportBounds().y));
        // check if near the mouse
        return converted_gp_pt;
      }
    });

    this.map.setAddressLocation(this.currentPosition);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    panel.add(createCollapseButton(), BorderLayout.WEST);
    this.photoListContainer = new PhotoWithoutGPListContainer(albumModel,
        photoModel).getContainer();
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
   * @param album
   *            album containing photos to add on map
   */
  public void addAlbum(Album album) {
    Set<Waypoint> waypoints = new HashSet<Waypoint>();

    Waypoint wp;
    // adding photo waypoint
    for (Photo photo : album.getPhotos()) {
      if (photo.getGeoPosition() != null) {
        // Adding valid geoposition corrdinates
        waypoints.add(wp = new Waypoint(photo.getGeoPosition().getLatitude(),
            photo.getGeoPosition().getLongitude()));
        this.waypoints.put(wp, photo);
      }

    }

    // create a WaypointPainter to draw the points
    WaypointPainter<JXMapViewer> painter = new WaypointPainter<JXMapViewer>();
    painter.setWaypoints(waypoints);

    // Display waypoints
    this.map.setOverlayPainter(painter);

  }

  /**
   * 
   * @param photo
   *            photo to add on map
   */
  public void addPhoto(Photo photo) {

    if (photo.getAlbum().equals(this.albumModel.getCurrentAlbum())) {

      Album album = photo.getAlbum();
      Set<Waypoint> waypoints = new HashSet<Waypoint>();

      Waypoint wp;
      // adding photo waypoint
      for (Photo p : album.getPhotos()) {
        if (p.getGeoPosition() != null) {
          // Adding valid geoposition corrdinates
          waypoints.add(wp = new Waypoint(p.getGeoPosition().getLatitude(), p
              .getGeoPosition().getLongitude()));
          this.waypoints.put(wp, p);
        }

      }

      // create a WaypointPainter to draw the points
      WaypointPainter<JXMapViewer> painter = new WaypointPainter<JXMapViewer>();
      painter.setWaypoints(waypoints);

      // Display waypoints
      this.map.setOverlayPainter(painter);
    }

  }

  @Override
  public JComponent getContainer() {
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
          component.setBounds(0, 0, parent.getWidth(), parent.getHeight());
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
