package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

import fr.umlv.IRPhoto.gui.ContainerInitializer;

public class MapContainer implements ContainerInitializer {

   private final JXMapKit map;
   private final JPanel panel;
   private GeoPosition currentPosition;

   public MapContainer() {
      this.panel = new JPanel();
      this.currentPosition = new GeoPosition(44.339722, 1.210278);

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

      TileFactory tf = new DefaultTileFactory(info);
      this.map = new JXMapKit();
      this.map.setTileFactory(tf);
      this.map.setZoom(4);
       this.map.setAddressLocation(this.currentPosition);
      this.panel.setLayout(new BorderLayout());
      this.panel.add(this.map, BorderLayout.CENTER);
   }

   @Override
   public JComponent getComponent() {
      return this.panel;
   }

}
