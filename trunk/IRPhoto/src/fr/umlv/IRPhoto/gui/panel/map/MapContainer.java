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
import java.util.HashSet;
import java.util.Set;

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
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;

public class MapContainer implements ContainerInitializer {

  private final JXMapViewer map;
  private final JLayeredPane panel;
  private final JPanel photoPanel;
  private GeoPosition currentPosition;
  private final AlbumSelectionModel albumSelectionModel;

  public MapContainer(AlbumSelectionModel albumSelectionModel) {
    this.albumSelectionModel = albumSelectionModel;
    this.panel = new JLayeredPane();
    this.panel.setLayout(createLayoutManger());
    this.photoPanel = new JPanel(new BorderLayout());
    this.photoPanel.setOpaque(false);
    
    this.photoPanel.setBackground(Color.white);
    this.photoPanel.add(new JButton("+"), BorderLayout.WEST);

    this.currentPosition = new GeoPosition(43.604503, 1.444026);

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

    this.albumSelectionModel
        .addAlbumSelectionListener(new AlbumSelectionListener() {
          @Override
          public void albumSelected(Album album) {
            addWaypoint(album);
          }
        });

    TileFactory tf = new DefaultTileFactory(info);
    this.map = new JXMapViewer();
    this.map.setTileFactory(tf);
    this.map.setZoom(6);
    this.map.setAddressLocation(this.currentPosition);

    Painter<JXMapViewer> textOverlay = new Painter<JXMapViewer>() {
      public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        g.setPaint(new Color(0, 0, 0, 150));
        g.fillRoundRect(50, 10, 182, 30, 10, 10);
        g.setPaint(Color.WHITE);
        g.drawString("Images provided by NASA", 50 + 10, 10 + 20);
      }
    };

    Painter<JXMapViewer> buttonOverlay = new Painter<JXMapViewer>() {
      public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        g.setPaint(new Color(0, 0, 0, 150));
        g.fillRoundRect(50, 10, 182, 30, 10, 10);
        g.setPaint(Color.WHITE);
        g.drawString("Images provided by NASA", 50 + 10, 10 + 20);
      }
    };

    // TEST
    this.addWaypoint(null);

    final JPanel panel = (JPanel) new PhotoWithoutGPListContainer(
        this.albumSelectionModel).getComponent();
    this.photoPanel.add(panel, BorderLayout.CENTER);

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
    this.photoPanel.setBounds(0, 0, this.panel.getWidth(), this.panel.getHeight());
    
    this.panel.add(this.photoPanel, Integer.valueOf(2));
    this.panel.add(this.map, Integer.valueOf(1));
    

  }

  public void addWaypoint(Album album) {
    // create a Set of Waypoints
    Set<Waypoint> waypoints = new HashSet<Waypoint>();

    // for (Photo photo : album.getPhotos()) {
    // waypoints.add(new Waypoint(photo.getLatitude(), photo.getLongitude()));
    // }

    // TEST
    waypoints.add(new Waypoint(currentPosition));

    // create a WaypointPainter to draw the points
    WaypointPainter<JXMapViewer> painter = new WaypointPainter<JXMapViewer>();
    painter.setWaypoints(waypoints);
    this.map.setOverlayPainter(painter);
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
