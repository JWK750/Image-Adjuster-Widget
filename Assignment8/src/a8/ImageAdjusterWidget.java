package a8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageAdjusterWidget extends JPanel implements ChangeListener {
	
	private Picture original_picture;
	private PictureView picture_view;
	private JPanel slider_panel;
	private JSlider blur_slider;
	private JSlider saturation_slider;
	private JSlider brightness_slider;
	
	public ImageAdjusterWidget(Picture picture) {
		setLayout(new BorderLayout());
		
		original_picture = picture;
		picture_view = new PictureView(picture.createObservable());
		add(picture_view, BorderLayout.CENTER);
		
		slider_panel = new JPanel();
		slider_panel.setLayout(new GridLayout(3,1));
		
		// Creates blur slider and label
		JPanel blur_panel = new JPanel(new BorderLayout());
		blur_slider = new JSlider(0, 5, 0);
		blur_slider.setPaintTicks(true);
		blur_slider.setSnapToTicks(true);
		blur_slider.setPaintLabels(true);
		blur_slider.setMajorTickSpacing(1);
		blur_slider.addChangeListener(this);
		blur_panel.add(new JLabel("Blur: "), BorderLayout.WEST);
		blur_panel.add(blur_slider, BorderLayout.CENTER);
		slider_panel.add(blur_panel);
		
		// Creates saturation slider and label
		JPanel saturation_panel = new JPanel(new BorderLayout());
		saturation_slider = new JSlider(-100, 100, 0);
		saturation_slider.setPaintTicks(true);
		saturation_slider.setPaintLabels(true);
		saturation_slider.setMajorTickSpacing(25);
		saturation_slider.addChangeListener(this);
		saturation_panel.add(new JLabel("Saturation: "), BorderLayout.WEST);
		saturation_panel.add(saturation_slider, BorderLayout.CENTER);
		slider_panel.add(saturation_panel);
		
		// Creates brightness slider and label
		JPanel brightness_panel = new JPanel(new BorderLayout());
		brightness_slider = new JSlider(-100, 100, 0);
		brightness_slider.setPaintTicks(true);
		brightness_slider.setPaintLabels(true);
		brightness_slider.setMajorTickSpacing(25);
		brightness_slider.addChangeListener(this);
		brightness_panel.add(new JLabel("Brightness: "), BorderLayout.WEST);
		brightness_panel.add(brightness_slider, BorderLayout.CENTER);
		slider_panel.add(brightness_panel);
		
		add(slider_panel, BorderLayout.SOUTH);
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		int blur = blur_slider.getValue();
		int saturation = saturation_slider.getValue();
		int brightness = brightness_slider.getValue();
		
		ObservablePicture new_pic = new ObservablePictureImpl(new PictureImpl(
				original_picture.getWidth(),original_picture.getHeight()));
		// Iterates through all pixels and blurs, saturates, and brightens each
		for (int i=0; i<new_pic.getWidth(); i++) {
			for (int j=0; j<new_pic.getHeight(); j++) {
				Pixel new_pixel;
				new_pixel = blurPixel(original_picture, i, j, blur);
				new_pixel = saturate(new_pixel, saturation);
				new_pixel = brighten(new_pixel, brightness);
				new_pic.setPixel(i, j, new_pixel);
			}
		} 
		picture_view.setPicture(new_pic);
	}
	
	private Pixel blurPixel(Picture p, int x, int y, int square_size) {
		/* Blurs a pixel with coordinates x and y by averaging it with all pixels in a square of
		 * size square_size around it */
		int left = Math.max(x-square_size, 0);
		int right = Math.min(x+square_size, p.getWidth()-1);
		int top = Math.max(y-square_size, 0);
		int bottom = Math.min(y+square_size, p.getHeight()-1);
		
		double red_total = 0;
		double green_total = 0;
		double blue_total = 0;
		int pixel_count = 0;
		
		for (int i=left; i<=right; i++) {
			for (int j=top; j<=bottom; j++) {
				red_total += p.getPixel(i, j).getRed();
				green_total += p.getPixel(i, j).getGreen();
				blue_total += p.getPixel(i, j).getBlue();
				pixel_count++;
			}
		}
		
		return new ColorPixel(red_total/pixel_count, green_total/pixel_count, blue_total/pixel_count);
	}
	
	private Pixel saturate(Pixel p, int saturation) {
		double new_red = p.getRed();
		double new_green = p.getGreen();
		double new_blue = p.getBlue();
		if (saturation < 0) {
			new_red = p.getRed() * (1.0 + (saturation / 100.0)) - 
					(p.getIntensity() * saturation / 100.0);
			new_green = p.getGreen() * (1.0 + (saturation / 100.0)) - 
					(p.getIntensity() * saturation / 100.0);
			new_blue = p.getBlue() * (1.0 + (saturation / 100.0)) - 
					(p.getIntensity() * saturation / 100.0);
		} else if (saturation > 0) {
			double a = Math.max(Math.max(p.getRed(),p.getGreen()), p.getBlue());
			if (a == 0) {
				// Prevents division by 0
				new_red = 0;
				new_green = 0;
				new_blue = 0;
			} else {
				new_red = p.getRed() * ((a + ((1.0 - a) * (saturation / 100.0))) / a);
				new_green = p.getGreen() * ((a + ((1.0 - a) * (saturation / 100.0))) / a);
				new_blue = p.getBlue() * ((a + ((1.0 - a) * (saturation / 100.0))) / a);
			}
		} 
		return new ColorPixel(new_red,new_green,new_blue);
	}
	
	private Pixel brighten(Pixel p, int brightness) {
		Pixel new_pixel = p;
		double factor = Math.abs(brightness*1.0/100);
		if (brightness > 0) {
			new_pixel = p.lighten(factor);
		} else if (brightness < 0) {
			new_pixel = p.darken(factor);
		}
		return new_pixel;
	}

}
