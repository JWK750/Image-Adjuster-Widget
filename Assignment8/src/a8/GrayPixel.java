package a8;


public class GrayPixel implements Pixel {

	private double intensity;

	private static final char[] PIXEL_CHAR_MAP = {'#', 'M', 'X', 'D', '<', '>', 's', ':', '-', ' '};


	public GrayPixel(double intensity) {
		if (intensity < 0.0 || intensity > 1.0) {
			throw new IllegalArgumentException("Intensity of gray pixel is out of bounds.");
		}
		this.intensity = intensity;
	}

	@Override
	public double getRed() {
		return getIntensity();
	}

	@Override
	public double getBlue() {
		return getIntensity();
	}

	@Override
	public double getGreen() {
		return getIntensity();
	}

	@Override
	public double getIntensity() {
		return intensity;
	}

	@Override
	public char getChar() {
		return PIXEL_CHAR_MAP[(int) (getIntensity()*10.0)];
	}
	
	public Pixel blend(Pixel p, double weight) {
		
		if (p == null) {
			throw new RuntimeException("Null pixel");
		}
		if (weight < 0 || weight > 1) {
			throw new RuntimeException("Invalid weight");
		}
		
		double new_red = this.getRed()*weight+p.getRed()*(1-weight);
		double new_green = this.getGreen()*weight+p.getGreen()*(1-weight);
		double new_blue = this.getBlue()*weight+p.getBlue()*(1-weight);
		
		return new ColorPixel(new_red, new_green, new_blue);
		
	}
	
	public Pixel lighten(double factor) {
		return blend(new GrayPixel(1), 1-factor);
	}
	
	public Pixel darken(double factor) {
		return blend(new GrayPixel(0), 1-factor);
	}
}
