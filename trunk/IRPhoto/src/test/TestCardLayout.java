package test;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

class TestCardLayout extends JPanel implements ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton precedent = new JButton("precedent");
  JButton suivant = new JButton("suivant");
  JScrollBar jcb = new JScrollBar();
  CardLayout gestionnaireDesCartes = new CardLayout();
  JPanel jeuCartes = new JPanel();

  TestCardLayout()
  {
    JPanel p = new JPanel();
    JTextArea zone = new JTextArea("Un bel essai");
    JLabel message = new JLabel("Bonjour", SwingConstants.CENTER);
    VoirCercle voirCercle = new VoirCercle();

    setLayout(new BorderLayout(5,5));
    jeuCartes.setLayout(gestionnaireDesCartes);    
    jeuCartes.setPreferredSize(new Dimension(200, 200));
    jeuCartes.add(zone, "zone");
    jeuCartes.add(voirCercle, "voir un cercle");
    message.setOpaque(true);
    jeuCartes.add(message, "message");
    add(jeuCartes, BorderLayout.CENTER);
    precedent.addActionListener(this);
    suivant.addActionListener(this);
    p.add(precedent);
    p.add(suivant);
    
    p.add(jcb);
    jcb.setOrientation(JScrollBar.HORIZONTAL);
    
    add(p, BorderLayout.SOUTH);
  } 
  
  public void actionPerformed(ActionEvent e)
  {
    Object obj = e.getSource();

    if (obj == precedent)
      {
	gestionnaireDesCartes.previous(jeuCartes);
      }
    else if (obj == suivant)
      {
	gestionnaireDesCartes.next(jeuCartes);
      }
  }

  
  public static void main(String[] argv)
    {
      JFrame monCadre = new JFrame();

      monCadre.setContentPane(new TestCardLayout()); 
      monCadre.addWindowListener(new WindowAdapter()
	      {
		public void windowClosing(WindowEvent evt)
		  {
		    System.exit(0);
		  }
	      });
      monCadre.pack(); 
      monCadre.setVisible(true); 
    }
}

class VoirCercle extends JPanel implements ActionListener
{  
  JButton bouton1 = new JButton("trace");
  JButton bouton2 = new JButton("efface");
  boolean cercle = false;

  VoirCercle()
  {
    setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
    bouton1.setActionCommand("tracer");
    bouton2.setActionCommand("effacer");
    add(bouton1);
    add(bouton2);
    bouton1.addActionListener(this);
    bouton2.addActionListener(this);
    setBackground(Color.cyan);
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    if (cercle)  g.setColor(Color.red);
    else g.setColor(Color.cyan);
    g.drawOval(100, 50, 100, 100);
  }

  public void actionPerformed(ActionEvent e)
  {
    String commande = e.getActionCommand();

    if (commande.equals("tracer")) cercle = true;
    else if (commande.equals("effacer")) cercle = false;
    repaint();
  }
}