package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import fr.umlv.IRPhoto.gui.ContainerFactory;

public class Main {

  public static void main(String[] args) {
    final JFrame frame = new JFrame("IRPhoto");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(800, 600));
    frame.setLocationRelativeTo(null);
    frame.setContentPane(ContainerFactory.createMainContainer());
    frame.setVisible(true);
  }
}
