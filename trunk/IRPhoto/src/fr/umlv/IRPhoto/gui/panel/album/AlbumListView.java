package fr.umlv.IRPhoto.gui.panel.album;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.umlv.IRPhoto.album.Album;
import fr.umlv.IRPhoto.gui.ContainerInitializer;
import fr.umlv.IRPhoto.gui.panel.albumslist.AlbumListener;
import fr.umlv.IRPhoto.gui.panel.albumslist.AlbumModel;

public class AlbumListView implements ContainerInitializer {

	private final JPanel panel;
	private final AlbumModel model;
	private final GridBagConstraints constraints;
	private final JPanel endPanel;

	public AlbumListView(AlbumModel model) {
		this.model = model;
		this.model.addAlbumListener(new AlbumListener() {

			@Override
			public void albumAdded(Album album) {
				AlbumListView.this.addAlbum(album);
				
			}

			@Override
			public void albumRemoved(Album album) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void albumUpdated(Album album) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.constraints = new GridBagConstraints();
		this.constraints.fill = GridBagConstraints.HORIZONTAL;
		this.constraints.anchor = GridBagConstraints.PAGE_START; 
		this.constraints.gridy = 0;      
		this.constraints.weightx = 0.5;
		this.constraints.gridx = 0;
		
		this.endPanel = new JPanel();

		this.panel = createAlbumListPanel();
	}
	
	public void addAlbum(Album album) {
		this.removeEndPanel();
		this.constraints.gridy++;
		JPanel newTitleAlbumPanel = new TitleAlbum(album).getPanel();
		panel.add(newTitleAlbumPanel, this.constraints);
		this.addEndPanel(this.panel);
		this.panel.revalidate();
	}

	private JPanel createAlbumListPanel() {
		JPanel panel = new JPanel(null);
		panel.setLayout(new GridBagLayout());

		panel.setBorder(BorderFactory.createMatteBorder(1, 1,
				1, 1, Color.MAGENTA));
		
		List<? extends Album> albums = this.model.getAlbums();
		for (Album album : albums) {
			this.constraints.gridy++;
			panel.add(new TitleAlbum(album).getPanel(), this.constraints);
		}
		
		this.addEndPanel(panel);
		return panel;
	}
	
	private void removeEndPanel() {
		this.panel.remove(this.endPanel);
	}
	
	private void  addEndPanel(JPanel panel) {
		this.constraints.weighty = 1;
		panel.add(this.endPanel, this.constraints);
		this.constraints.weighty = 0;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	@Override
	public JComponent initialize() {
		return this.panel;
	}
	
}
