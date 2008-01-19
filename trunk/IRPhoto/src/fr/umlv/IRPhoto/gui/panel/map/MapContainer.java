package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.album.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.album.AlbumModel;

public class MapContainer implements ContainerInitializer {

   private final JXMapKit map;
   private final JPanel panel;
   private GeoPosition currentPosition;
   private final AlbumModel model;

   public MapContainer(AlbumModel model) {
      this.model = model;
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
      
      this.model.addAlbumListener(new MyAlbumListener());

      TileFactory tf = new DefaultTileFactory(info);
      this.map = new JXMapKit();
      this.map.setTileFactory(tf);
      this.map.setZoom(4);
       this.map.setAddressLocation(this.currentPosition);
      this.panel.setLayout(new BorderLayout());
      this.panel.add(this.map, BorderLayout.CENTER);
   }
   
   public void addWaypoint() {
       //create a Set of waypoints
       Set<Waypoint> waypoints = new HashSet<Waypoint>();
       waypoints.add(new Waypoint(41.881944,-87.627778));
       waypoints.add(new Waypoint(40.716667,-74));
       
       //crate a WaypointPainter to draw the points
       WaypointPainter painter = new WaypointPainter();
       painter.setWaypoints(waypoints);
       this.map.getMainMap().setOverlayPainter(painter);
   }


   @Override
   public JComponent getComponent() {
      return this.panel;
   }
   
   private static class MyAlbumListener implements AlbumListener {

      @Override
      public void albumAdded(Album album) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void albumRemoved(Album album) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void albumRenamed(Album album, String newName) {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void photoAdded(Album album, Photo photo) {
         // TODO Auto-generated method stub
         
      }
      
   }

}
