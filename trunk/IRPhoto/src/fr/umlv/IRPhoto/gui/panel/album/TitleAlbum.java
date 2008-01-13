package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TitleAlbum {

  private final JPanel panel;
  private final JPanel title;
  private final JPanel list;

  public TitleAlbum(String title) {
    this.title = createTitlePanel(title);
    this.title.setAlignmentX(Component.LEFT_ALIGNMENT);

    this.list = createListPanel();
    this.list.setVisible(false);

    this.panel = new JPanel();
    this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
    this.panel.add(this.title);
    this.panel.add(this.list);
    this.panel.add(Box.createVerticalGlue());
  }

  private JPanel createListPanel() {
    final AlbumList list = new AlbumList();
    return list.getPanel();
  }

  private JPanel createTitlePanel(String title) {
    final JPanel jp = new JPanel();
    jp.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

    jp.add(new JLabel(title));
    jp.add(Box.createHorizontalGlue());
    final JTextField textField = new JTextField(2);
    textField.setHorizontalAlignment(JTextField.LEADING);
    textField.setColumns(20);
    jp.add(textField);

    final JButton alphaSortButton = createAlphaSortButton();
    jp.add(alphaSortButton);

    final JButton typeSortButton = createTypeSortButton();
    jp.add(typeSortButton);

    final JButton dateSortButton = createDateSortButton();
    jp.add(dateSortButton);

    final JButton button = createShowListButton();
    jp.add(button);
    return jp;
  }

  private JButton createDateSortButton() {
    final JButton b = new JButton("date");
    return b;
  }

  private JButton createTypeSortButton() {
    final JButton b = new JButton("type");
    return b;
  }

  private JButton createAlphaSortButton() {
    final JButton b = new JButton("alpha");
    return b;
  }

  private JButton createShowListButton() {
    final JButton b = new JButton("->");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        TitleAlbum.this.list.setVisible(!TitleAlbum.this.list.isVisible());
      }
    });
    return b;
  }

  public JPanel getPanel() {
    return this.panel;
  }

}
