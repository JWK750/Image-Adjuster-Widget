package a8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorWidget extends JPanel implements MouseListener {
	
	private PictureView picture_view;
	private JPanel list_panel;
	
	public PixelInspectorWidget(Picture picture) {
		setLayout(new BorderLayout());
		
		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);
		
		list_panel = new JPanel();
		list_panel.setLayout(new GridLayout(6,1));
		list_panel.setPreferredSize(new Dimension(95, picture_view.getHeight()));
		
		list_panel.add(new JLabel("X:"));
		list_panel.add(new JLabel("Y:"));
		list_panel.add(new JLabel("Red:"));
		list_panel.add(new JLabel("Green:"));
		list_panel.add(new JLabel("Blue:"));
		list_panel.add(new JLabel("Brightness:"));
		add(list_panel, BorderLayout.WEST);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		// Assigns pixel that has been clicked to a variable
		PictureView p = (PictureView) arg0.getSource();
		Pixel pixel = p.getPicture().getPixel(arg0.getX(), arg0.getY());
		
		// Rounds Pixel characteristics to 2 decimal places
		double red = Math.round(100.0*pixel.getRed())/100.0;
		double green = Math.round(100.0*pixel.getGreen())/100.0;
		double blue = Math.round(100.0*pixel.getBlue())/100.0;
		double brightness = Math.round(100.0*pixel.getIntensity())/100.0;
		
		// Updates side panel
		list_panel.removeAll();
		
		list_panel.add(new JLabel("X: "+arg0.getX()));
		list_panel.add(new JLabel("Y: "+arg0.getY()));
		list_panel.add(new JLabel("Red: "+red));
		list_panel.add(new JLabel("Green: "+green));
		list_panel.add(new JLabel("Blue: "+blue));
		list_panel.add(new JLabel("Brightness: "+brightness));
		
		list_panel.revalidate();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
