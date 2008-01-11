package test;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class TestTab2 extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
   * Constructeur
   * Mise en place de la fenêtre
   */
  public TestTab2(){
    super("Test JTabbedPane");
    //Création du classeur
    JTabbedPane classeur = new JTabbedPane();
    //Créer le panel
    JPanel onglet = new JPanel();
    onglet.setPreferredSize(new Dimension(400,400));
    //Creer le scrollpane
    JScrollPane scrollpane = new JScrollPane(onglet);
    scrollpane.setPreferredSize(new Dimension(200,200));
    //Mettre l'onglet dans le classeur
    classeur.addTab("Mon onglet",scrollpane);
    //Mettre le classeur dans la fenêtre
    this.getContentPane().add(classeur);
  }
  
  /**
   * Main pour lancer la fenêtre de test
   */
  public static void main(String[] args) {
    //Créer et lancer la fenêtre
    TestTab2 testeur = new TestTab2();
    testeur.pack();
    testeur.setVisible(true);
  }
}