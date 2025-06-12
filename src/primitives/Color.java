package primitives;

/**
 * Wrapper class for java.awt.Color.
 * <p>
 * The constructors operate with any non-negative RGB values. The color is
 * stored without an upper limit (unlike standard 255). Supports arithmetic
 * operations useful for manipulating light colors.
 * </p>
 * 
 * @author Dan Zilberstein
 */
public class Color {

	/** The internal fields maintain RGB components as double numbers. */
	private final Double3 rgb;

	/** Black color = (0,0,0) */
	public static final Color BLACK = new Color();

	/** Default constructor - to generate Black Color (privately) */
	private Color() {
		rgb = Double3.ZERO;
	}

	/**
	 * Constructor to generate a color using explicit RGB values.
	 *
	 * @param r Red component (non-negative)
	 * @param g Green component (non-negative)
	 * @param b Blue component (non-negative)
	 * @throws IllegalArgumentException if any component is negative
	 */
	public Color(double r, double g, double b) {
		if (r < 0 || g < 0 || b < 0)
			throw new IllegalArgumentException("Negative color component is illegal");
		rgb = new Double3(r, g, b);
	}

	/**
	 * Constructor to generate a color from a Double3 RGB triple.
	 *
	 * @param rgb triad of Red/Green/Blue components
	 * @throws IllegalArgumentException if any component is negative
	 */
	private Color(Double3 rgb) {
		if (rgb.d1() < 0 || rgb.d2() < 0 || rgb.d3() < 0)
			throw new IllegalArgumentException("Negative color component is illegal");
		this.rgb = rgb;
	}

	/**
	 * Constructor based on java.awt.Color object.
	 *
	 * @param other java.awt.Color instance
	 */
	public Color(java.awt.Color other) {
		rgb = new Double3(other.getRed(), other.getGreen(), other.getBlue());
	}

	/**
	 * Converts this {@code Color} into a {@code java.awt.Color}. Any component
	 * above 255 will be clamped to 255.
	 *
	 * @return a java.awt.Color object representing this color
	 */
	public java.awt.Color getColor() {
		int ir = (int) rgb.d1();
		int ig = (int) rgb.d2();
		int ib = (int) rgb.d3();
		return new java.awt.Color(ir > 255 ? 255 : ir, ig > 255 ? 255 : ig, ib > 255 ? 255 : ib);
	}

	/**
	 * Adds one or more colors to this color (component-wise).
	 *
	 * @param colors other colors to add
	 * @return new {@code Color} which is the result of the addition
	 */
	public Color add(Color... colors) {
		double rr = rgb.d1();
		double rg = rgb.d2();
		double rb = rgb.d3();
		for (Color c : colors) {
			rr += c.rgb.d1();
			rg += c.rgb.d2();
			rb += c.rgb.d3();
		}
		return new Color(rr, rg, rb);
	}

	/**
	 * Scales this color by a scalar vector per RGB component.
	 *
	 * @param k scaling factors per RGB channel (must be non-negative)
	 * @return new {@code Color} scaled accordingly
	 * @throws IllegalArgumentException if any component of k is negative
	 */
	public Color scale(Double3 k) {
		if (k.d1() < 0.0 || k.d2() < 0.0 || k.d3() < 0.0)
			throw new IllegalArgumentException("Can't scale a color by a negative number");
		return new Color(rgb.product(k));
	}

	/**
	 * Scales this color by a uniform scalar.
	 *
	 * @param k scale factor (must be non-negative)
	 * @return new {@code Color} scaled accordingly
	 * @throws IllegalArgumentException if k is negative
	 */
	public Color scale(double k) {
		if (k < 0.0)
			throw new IllegalArgumentException("Can't scale a color by a negative number");
		return new Color(rgb.scale(k));
	}

	/**
	 * Reduces this color by dividing each component by a factor.
	 *
	 * @param k reduction factor (must be ≥ 1)
	 * @return new {@code Color} with reduced intensity
	 * @throws IllegalArgumentException if k &lt; 1
	 */
	public Color reduce(int k) {
		if (k < 1)
			throw new IllegalArgumentException("Can't scale a color by a number lower than 1");
		return new Color(rgb.reduce(k));
	}

	@Override
	public String toString() {
		return "rgb:" + rgb;
	}
}