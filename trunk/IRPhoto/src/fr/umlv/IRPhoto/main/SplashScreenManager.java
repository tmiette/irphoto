package fr.umlv.IRPhoto.main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SplashScreen;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;

/**
 * 
 * Class which manage the splash screen of this application.
 * 
 * @author MIETTE Tom
 * @author MOURET Sebastien
 * 
 */
public class SplashScreenManager {

  /**
   * 
   * This class represents a step of the loading of the application (e.g.
   * reading saved files, building graphical interface, ...).
   * 
   * @author MIETTE Tom
   * @author MOURET Sebastien
   * 
   */
  private static class LoadingStep {

    // the icon for this step
    private final Image image;

    // the label of this step
    private final String text;

    /**
     * 
     * Constructor of a step. The icon will be displayed in the splash screen
     * and the label will be written in the progress bar.
     * 
     * @param image
     *            the icon for this step.
     * @param text
     *            the label for this step.
     */
    public LoadingStep(Image image, String text) {
      this.image = image;
      this.text = text;
    }

    /**
     * Returns the icon for this step.
     * 
     * @return the icon for this step.
     */
    public Image getImage() {
      return this.image;
    }

    /**
     * Returns the label for this step.
     * 
     * @return the label for this step.
     */
    public String getText() {
      return this.text;
    }
  }

  // the progress bar
  private static JProgressBar bar;

  // current step
  private static int currentStep;

  // dimension of the splash screen
  private static Dimension size;

  // the splash screen
  private static SplashScreen splash;

  // steps of the loading process
  private static final LoadingStep[] steps = {
      new LoadingStep(new ImageIcon(SplashScreenManager.class
          .getResource("/icons/pictures-32x32.png")).getImage(),
          "Loading saved albums ..."),
      new LoadingStep(new ImageIcon(SplashScreenManager.class
          .getResource("/icons/paint-32x32.png")).getImage(),
          "Building graphical interface ...") };

  /**
   * 
   * Paints the a new icon on the splash screen with the specified parameters.
   * 
   * @param image
   *            the new image icon.
   * @param strut
   *            the horizontal strut where drawing the image.
   */
  private static void drawImage(Image image, int strut) {
    Graphics2D graphics = splash.createGraphics();
    int baseline = splash.getSize().height - 61;
    graphics.drawImage(image, 20 + strut * 70, baseline, 32, 32, null);
    splash.update();
    graphics.dispose();

  }

  /**
   * Ends the current step of the loading process.
   */
  public static void endStep() {
    currentStep++;
    // paint the new value of the progress bar
    paintBarValue();
    if (currentStep < steps.length) {
      nextStep();
    }
  }

  /**
   * Launches the next step of the loading process.
   */
  private static void nextStep() {
    drawImage(steps[currentStep].getImage(), currentStep);
    paintBarText();
  }

  /**
   * Paints a new label on the progress bar depending on the current step.
   */
  private static void paintBarText() {
    Graphics2D graphics = splash.createGraphics();
    graphics.translate(10, size.height - 30);
    bar.setString(steps[currentStep].getText());
    bar.paint(graphics);
    splash.update();
    graphics.dispose();
  }

  /**
   * Paints the new value of the progress bar.
   */
  private static void paintBarValue() {
    Graphics2D graphics = splash.createGraphics();
    graphics.translate(10, size.height - 30);
    bar.setValue(bar.getValue() + bar.getMaximum() / steps.length);
    bar.paint(graphics);
    splash.update();
    graphics.dispose();
  }

  /**
   * Method which start the splash screen manager. The splash screen is got and
   * the first step is launched.
   */
  public static void start() {
    splash = SplashScreen.getSplashScreen();
    size = splash.getSize();
    // paints the progress bar
    bar = new JProgressBar(0, 100);
    bar.setOpaque(true);
    bar.setStringPainted(true);
    bar.setSize(size.width - 20, bar.getPreferredSize().height);
    bar.setString("Starting ...");
    bar.setVisible(true);
    Graphics2D graphics = splash.createGraphics();
    graphics.translate(10, size.height - 30);
    bar.paint(graphics);
    graphics.dispose();
    nextStep();
  }

}
