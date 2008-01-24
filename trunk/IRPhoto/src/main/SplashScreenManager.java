package main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SplashScreen;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;

public class SplashScreenManager {

  private static final LoadingStep[] steps = {
      new LoadingStep(new ImageIcon(SplashScreenManager.class
          .getResource("/icons/pictures-32x32.png")).getImage(),
          "Loading saved albums ..."),
      new LoadingStep(new ImageIcon(SplashScreenManager.class
          .getResource("/icons/paint-32x32.png")).getImage(),
          "Building graphical interface ...") };
  private static int currentStep;

  private static Dimension size;
  private static SplashScreen splash;
  private static JProgressBar bar;

  public static void start() {
    splash = SplashScreen.getSplashScreen();
    size = splash.getSize();
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

  private static void nextStep() {
    drawImage(steps[currentStep].getImage(), currentStep);
    paintBarText();
  }

  public static void endStep() {
    currentStep++;
    paintBarValue();
    if (currentStep < steps.length) {
      nextStep();
    }
  }

  private static void paintBarText() {
    Graphics2D graphics = splash.createGraphics();
    graphics.translate(10, size.height - 30);
    bar.setString(steps[currentStep].getText());
    bar.paint(graphics);
    splash.update();
    graphics.dispose();
  }

  private static void paintBarValue() {
    Graphics2D graphics = splash.createGraphics();
    graphics.translate(10, size.height - 30);
    bar.setValue(bar.getValue() + bar.getMaximum() / steps.length);
    bar.paint(graphics);
    splash.update();
    graphics.dispose();
  }

  private static void drawImage(Image image, int strut) {
    Graphics2D graphics = splash.createGraphics();
    int baseline = splash.getSize().height - 70;
    graphics.drawImage(image, 20 + strut * 70, baseline, 32, 32, null);
    splash.update();
    graphics.dispose();

  }

  private static class LoadingStep {

    private final Image image;
    private final String text;

    public LoadingStep(Image image, String text) {
      this.image = image;
      this.text = text;
    }

    public Image getImage() {
      return this.image;
    }

    public String getText() {
      return this.text;
    }
  }

}
