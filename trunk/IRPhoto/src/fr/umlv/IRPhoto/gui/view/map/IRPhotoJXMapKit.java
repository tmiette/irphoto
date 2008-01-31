/**
 * 
 */
package fr.umlv.IRPhoto.gui.view.map;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

/**
 * 
 * This class represents the map viewer container.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class IRPhotoJXMapKit {

  /**
   * 
   * This enum defines all maps which can be displayed in this application.
   * 
   * @author MIETTE Tom
   * @author MOURET Sebastien
   * 
   */
  public static enum MyDefaultMaps {
    OPEN_STREET_MAP;
  }

  /**
   * Number of threads using in the viewer.
   */
  public static final int MAX_THREADS = 8;

  // the map viewer container
  private final JXMapViewer mapViewer;

  // panel for map components
  private final JPanel panel;

  // flag for zoom direction
  private final boolean sliderReversed = false;

  // flag of zoom changes
  private boolean zoomChanging;

  // zoom
  private final JSlider zoomSlider;

  /**
   * 
   * Default constructor of the map viewer container. this constructor used the
   * OPEN_STREET_MAP map.
   * 
   */
  public IRPhotoJXMapKit() {
    this(MyDefaultMaps.OPEN_STREET_MAP);
  }

  /**
   * 
   * Constructor of the map viewer container.
   * 
   * @param myDefaultMaps
   *            the map.
   */
  public IRPhotoJXMapKit(MyDefaultMaps myDefaultMaps) {

    // initialize components
    GridBagConstraints gridBagConstraints;

    this.mapViewer = new JXMapViewer();
    this.panel = new JPanel();
    this.zoomSlider = new JSlider();

    this.mapViewer.setLayout(new GridBagLayout());

    this.panel.setOpaque(false);
    this.panel.setLayout(new GridBagLayout());

    this.zoomSlider.setMajorTickSpacing(1);
    this.zoomSlider.setMaximum(15);
    this.zoomSlider.setMinimum(10);
    this.zoomSlider.setMinorTickSpacing(1);
    this.zoomSlider.setOrientation(javax.swing.JSlider.VERTICAL);
    this.zoomSlider.setPaintTicks(true);
    this.zoomSlider.setSnapToTicks(true);
    this.zoomSlider.setMinimumSize(new Dimension(35, 100));
    this.zoomSlider.setPreferredSize(new Dimension(35, 190));
    this.zoomSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent evt) {
        zoomSliderStateChanged(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.VERTICAL;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    this.panel.add(zoomSlider, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(4, 12, 4, 4);
    this.mapViewer.add(this.panel, gridBagConstraints);
    this.zoomSlider.setOpaque(false);
    setDefaultMap(myDefaultMaps);

    this.mapViewer.setCenterPosition(new GeoPosition(0, 0));
    this.mapViewer.setRestrictOutsidePanning(true);

    this.mapViewer.addPropertyChangeListener("zoom",
        new PropertyChangeListener() {
          public void propertyChange(PropertyChangeEvent evt) {
            zoomSlider.setValue(mapViewer.getZoom());
          }
        });

    ((DefaultTileFactory) this.mapViewer.getTileFactory())
        .setThreadPoolSize(MAX_THREADS);
  }

  /**
   * Returns the map.
   * 
   * @return the map.
   */
  public JXMapViewer getMainMap() {
    return this.mapViewer;
  }

  /**
   * 
   * Sets the map displayed in the map viewer.
   * 
   * @param myDefaultMap
   *            the new map.
   */
  public void setDefaultMap(MyDefaultMaps myDefaultMap) {

    if (myDefaultMap == MyDefaultMaps.OPEN_STREET_MAP) {
      final int max = 10;
      final TileFactoryInfo info = new TileFactoryInfo(1, max - 2, max, 256,
          true, true, "http://tile.openstreetmap.org", "x", "y", "z") {
        public String getTileUrl(int x, int y, int zoom) {
          zoom = max - zoom;
          return this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
        }

      };
      final TileFactory tf = new DefaultTileFactory(info);
      setTileFactory(tf);
      setZoom(11);
    }
  }

  /**
   * Sets the tile factory for both embedded JXMapViewer components. Calling
   * this method will also reset the center and zoom levels of both maps, as
   * well as the bounds of the zoom slider.
   * 
   * @param fact
   *            the new TileFactory
   */
  public void setTileFactory(TileFactory fact) {
    mapViewer.setTileFactory(fact);
    mapViewer.setZoom(fact.getInfo().getDefaultZoomLevel());
    mapViewer.setCenterPosition(new GeoPosition(0, 0));
    zoomSlider.setMinimum(fact.getInfo().getMinimumZoomLevel());
    zoomSlider.setMaximum(fact.getInfo().getMaximumZoomLevel());
  }

  /**
   * Set the current zoom level for the main map.
   * 
   * @param zoom
   *            the new zoom level
   */
  public void setZoom(int zoom) {
    zoomChanging = true;
    mapViewer.setZoom(zoom);
    if (sliderReversed) {
      zoomSlider.setValue(zoomSlider.getMaximum() - zoom);
    } else {
      zoomSlider.setValue(zoom);
    }
    zoomChanging = false;
  }

  /**
   * 
   * This method is called when the slider state changes.
   * 
   * @param evt
   *            the change event.
   */
  private void zoomSliderStateChanged(ChangeEvent evt) {
    if (!zoomChanging) {
      setZoom(zoomSlider.getValue());
    }
  }

}
