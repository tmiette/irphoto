package fr.umlv.IRPhoto.gui.panel.features;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Photo;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.IconFactory;

public class FeaturesContainer implements ContainerInitializer {

  private final JLabel nameLabel;
  private final JLabel formatLabel;
  private final PhotoSelectionModel model;

  public FeaturesContainer(PhotoSelectionModel model) {
    this.nameLabel = new JLabel();
    this.formatLabel = new JLabel();
    this.model = model;
    this.model.addPhotoSelectionListener(new PhotoSelectionListener() {

      @Override
      public void photoSelected(Photo photo) {
        BufferedImage image;
        try {
          image = ImageIO.read(new File(photo.getPath()));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        nameLabel.setText(photo.getName());
        nameLabel.repaint();
        formatLabel.setText("JPEG");
        formatLabel.repaint();
      }

    });
  }

  @Override
  public JComponent initialize() {
    final JPanel mainPanel = new JPanel(new BorderLayout());

    final JPanel featuresPanel = new JPanel(null);
    featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
    featuresPanel.add(this.nameLabel);
    featuresPanel.add(this.formatLabel);

    mainPanel.add(new JLabel(IconFactory.getIcon("logo.gif")),
        BorderLayout.WEST);
    mainPanel.add(featuresPanel, BorderLayout.CENTER);

    return mainPanel;
  }

}
