package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.Painter;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerFactory;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.PhotoUpdatedListener;
import fr.umlv.IRPhoto.gui.panel.album.PhotoUpdatedModel;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;

public class MapContainer implements ContainerInitializer {

  private final JXMapViewer map;
  private final JLayeredPane panel;
  private final JPanel photoPanel;
  private GeoPosition currentPosition;
  private final AlbumSelectionModel albumSelectionModel;
  private final PhotoUpdatedModel photoUpdatedModel;
  private final HashMap<Album, WaypointPainter<JXMapViewer>> albums;
  private Album currentAlbum;

  public MapContainer(AlbumSelectionModel albumSelectionModel,
      PhotoUpdatedModel photoUpdatedModel) {
    this.photoUpdatedModel = photoUpdatedModel;
    this.photoUpdatedModel.addPhotoUpdatedListener(new PhotoUpdatedListener() {

      @Override
      public void geoppositionUpdated(Photo photo) {
        addPhoto(photo);
      }

      @Override
      public void nameUpdated(Photo photo) {
        // TODO Auto-generated method stub

      }

    });

    this.albums = new HashMap<Album, WaypointPainter<JXMapViewer>>();

    this.panel = new JLayeredPane();
    this.panel.setLayout(createLayoutManger());

    this.photoPanel = new JPanel(new BorderLayout());
    this.photoPanel.setOpaque(false);
    this.photoPanel.add(new JButton("+"), BorderLayout.WEST);

    this.currentPosition = new GeoPosition(43.604503, 1.444026);

    this.albumSelectionModel = albumSelectionModel;
    this.albumSelectionModel
        .addAlbumSelectionListener(new AlbumSelectionListener() {
          @Override
          public void albumSelected(Album album) {
            addAlbum(album);
          }
        });

    final int max = 17;
    TileFactoryInfo info = new TileFactoryInfo(0, max, max, 256, true, true,
        "http://tile.openstreetmap.org",// 5/15/10.png",
        "x", "y", "z") {
      public String getTileUrl(int x, int y, int zoom) {
        zoom = max - zoom;
        String url = this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
        return url;
      }
    };
    this.map = new JXMapViewer();
    this.map.setTileFactory(new DefaultTileFactory(info));
    this.map.setZoom(6);
    this.map.setAddressLocation(this.currentPosition);

    // TEST
//    this.addAlbum(null);

    final JPanel panel = (JPanel) ContainerFactory
        .createPhotoWithoutGPListContainer();
    this.photoPanel.add(panel, BorderLayout.WEST);

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

    this.map.setBounds(0, 0, this.panel.getWidth(), this.panel.getHeight());
    this.photoPanel.setBounds(0, 0, this.panel.getWidth(), this.panel
        .getHeight());

    this.panel.add(this.photoPanel, Integer.valueOf(2));
    this.panel.add(this.map, Integer.valueOf(1));

  }

  public void addAlbum(Album album) {
    WaypointPainter<JXMapViewer> waypointPainter = this.albums.get(album);
    if (waypointPainter != null) {
      if (this.currentAlbum != null) {
        this.albums.get(this.currentAlbum).setVisible(false);
      }
      waypointPainter.setVisible(true);
    } else {
      // create a Set of Waypoints
      Set<Waypoint> waypoints = new HashSet<Waypoint>();

      // adding photo waypoint
       for (Photo photo : album.getPhotos()) {
       waypoints.add(new Waypoint(photo.getLatitude(), photo.getLongitude()));
       }

      // TEST
//      waypoints.add(new Waypoint(currentPosition));

      // create a WaypointPainter to draw the points
      WaypointPainter<JXMapViewer> painter = new WaypointPainter<JXMapViewer>();
      painter.setWaypoints(waypoints);
      this.albums.put(album, painter);
      this.map.setOverlayPainter(painter);
    }
    this.currentAlbum = album;
  }

  public void addPhoto(Photo photo) {
    for (Entry<Album, WaypointPainter<JXMapViewer>> albums : this.albums
        .entrySet()) {
      Album album = albums.getKey();
      for (Photo ph : album.getPhotos()) {
        if (photo.equals(ph)) {
          albums.getValue().getWaypoints().add(
              new Waypoint(photo.getLatitude(), photo.getLongitude()));
          if (this.currentAlbum.equals(album)) {
            albums.getValue().setVisible(true);
          }
          return;
        }
      }
    }
  }

  @Override
  public JComponent getComponent() {
    return this.panel;
  }

  public static LayoutManager createLayoutManger() {
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
