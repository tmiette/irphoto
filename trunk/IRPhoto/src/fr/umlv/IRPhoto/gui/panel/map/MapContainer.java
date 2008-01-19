package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionListener;
import fr.umlv.IRPhoto.gui.panel.albumlist.AlbumSelectionModel;

public class MapContainer implements ContainerInitializer {

   private final JXMapKit map;
   private final JPanel panel;
   private GeoPosition currentPosition;
   private final AlbumSelectionModel albumSelectionModel;

   public MapContainer(AlbumSelectionModel albumSelectionModel) {
      this.albumSelectionModel = albumSelectionModel;
      this.panel = new JPanel();
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
      this.map = new JXMapKit();
      this.map.setTileFactory(tf);
      this.map.setZoom(6);
      this.map.setAddressLocation(this.currentPosition);
      this.panel.setLayout(new BorderLayout());
      this.panel.add(this.map, BorderLayout.CENTER);
   }

   public void addWaypoint(Album album) {
      // create a Set of Waypoints
      Set<Waypoint> waypoints = new HashSet<Waypoint>();

      for (Photo photo : album.getPhotos()) {
         waypoints.add(new Waypoint(photo.getLatitude(), photo.getLongitude()));
      }

      // create a WaypointPainter to draw the points
      WaypointPainter<JXMapViewer> painter = new WaypointPainter<JXMapViewer>();
      painter.setWaypoints(waypoints);
      this.map.getMainMap().setOverlayPainter(painter);
   }

   @Override
   public JComponent getComponent() {
      return this.panel;
   }

}
