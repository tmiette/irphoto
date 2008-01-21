/**
 * 
 */
package fr.umlv.IRPhoto.gui.panel.map;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

/**
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class MyJXMapKit {

  private JPanel jPanel1;
  private JXMapViewer mainMap;
  private JSlider zoomSlider;
  private boolean zoomChanging = false;
  private final boolean sliderReversed = false;
  public static final int MAP_THREAD_NB = 5;

  private enum MyDefaultMaps {
    OpenStreetMap
  };

  /**
   * 
   */
  public MyJXMapKit() {
    initComponents();
    zoomSlider.setOpaque(false);
    setDefaultMap(MyDefaultMaps.OpenStreetMap);

    mainMap.setCenterPosition(new GeoPosition(0, 0));
    mainMap.setRestrictOutsidePanning(true);

    ((DefaultTileFactory) this.mainMap.getTileFactory())
        .setThreadPoolSize(MAP_THREAD_NB);
  }

  public void setDefaultMap(MyDefaultMaps myDefaultMap) {
    if (myDefaultMap == MyDefaultMaps.OpenStreetMap) {
      final int max = 17;
      TileFactoryInfo info = new TileFactoryInfo(1, max - 2, max, 256, true,
          true, // tile size is 256 and x/y orientation is normal
          "http://tile.openstreetmap.org",// 5/15/10.png",
          "x", "y", "z") {
        public String getTileUrl(int x, int y, int zoom) {
          zoom = max - zoom;
          String url = this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
          return url;
        }

      };
      TileFactory tf = new DefaultTileFactory(info);
      setTileFactory(tf);
      setZoom(11);
    }
  }

  /**
   * Sets the tile factory for both embedded JXMapViewer components. Calling
   * this method will also reset the center and zoom levels of both maps, as
   * well as the bounds of the zoom slider.
   * 
   * @param fact the new TileFactory
   */
  public void setTileFactory(TileFactory fact) {
    mainMap.setTileFactory(fact);
    mainMap.setZoom(fact.getInfo().getDefaultZoomLevel());
    mainMap.setCenterPosition(new GeoPosition(0, 0));
    zoomSlider.setMinimum(fact.getInfo().getMinimumZoomLevel());
    zoomSlider.setMaximum(fact.getInfo().getMaximumZoomLevel());
  }

  /**
   * Set the current zoomlevel for the main map.
   * 
   * @param zoom the new zoom level
   */
  public void setZoom(int zoom) {
    zoomChanging = true;
    mainMap.setZoom(zoom);
    if (sliderReversed) {
      zoomSlider.setValue(zoomSlider.getMaximum() - zoom);
    } else {
      zoomSlider.setValue(zoom);
    }
    zoomChanging = false;
  }

  private void zoomSliderStateChanged(ChangeEvent evt) {
    if (!zoomChanging) {
      setZoom(zoomSlider.getValue());
    }
  }

  /**
   * This method is called from within the constructor to initialize the form.
   */
  private void initComponents() {
    GridBagConstraints gridBagConstraints;

    this.mainMap = new JXMapViewer();
    this.jPanel1 = new JPanel();
    this.zoomSlider = new JSlider();

    this.mainMap.setLayout(new GridBagLayout());

    this.jPanel1.setOpaque(false);
    this.jPanel1.setLayout(new java.awt.GridBagLayout());

    this.zoomSlider.setMajorTickSpacing(1);
    this.zoomSlider.setMaximum(15);
    this.zoomSlider.setMinimum(10);
    this.zoomSlider.setMinorTickSpacing(1);
    this.zoomSlider.setOrientation(javax.swing.JSlider.VERTICAL);
    this.zoomSlider.setPaintTicks(true);
    this.zoomSlider.setSnapToTicks(true);
    this.zoomSlider.setMinimumSize(new java.awt.Dimension(35, 100));
    this.zoomSlider.setPreferredSize(new java.awt.Dimension(35, 190));
    this.zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        zoomSliderStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    jPanel1.add(zoomSlider, gridBagConstraints);

    // ///////////
    // JPanel jp = new JPanel(new GridBagLayout());
    // JButton b = new JButton(">");
    // b.setPreferredSize(new Dimension(10, 50));
    //
    // gridBagConstraints = new java.awt.GridBagConstraints();
    // gridBagConstraints.gridx = 0;
    // gridBagConstraints.gridy = 0;
    // gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    // // gridBagConstraints.weightx = 1.0;
    // // gridBagConstraints.weighty = 1.0;
    // // gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    // mainMap.add(b, gridBagConstraints);
    //
    // gridBagConstraints = new java.awt.GridBagConstraints();
    // gridBagConstraints.gridx = 0;
    // gridBagConstraints.gridy = 0;
    // gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
    // gridBagConstraints.weightx = 1.0;
    // gridBagConstraints.weighty = 1.0;
    // gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    // mainMap.add(jp, gridBagConstraints);

    // ////////

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    mainMap.add(jPanel1, gridBagConstraints);

    // gridBagConstraints = new java.awt.GridBagConstraints();
    // gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    // gridBagConstraints.weightx = 1.0;
    // gridBagConstraints.weighty = 1.0;
    // this.mainPanel.add(mainMap, gridBagConstraints);
  }

  /**
   * @return the mainMap
   */
  public JXMapViewer getMainMap() {
    return this.mainMap;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        MyJXMapKit map = new MyJXMapKit();
        JFrame frame = new JFrame("JXMapKit test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(map.getMainMap());
        frame.pack();
        frame.setSize(500, 300);
        frame.setVisible(true);
      }
    });
  }

}
