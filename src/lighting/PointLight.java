package lighting;

import primitives.*;

public class PointLight extends Light implements LightSource {

	/** The position of the light source in 3D space. */
	protected Point position;

	/** Constant attenuation coefficient (default = 1.0). */
	private double kC = 1d;

	/** Linear attenuation coefficient (default = 0.0). */
	private double kL = 0d;

	/** Quadratic attenuation coefficient (default = 0.0). */
	private double kQ = 0d;

	/**
	 * Constructs a new {@code PointLight} with a given color intensity and
	 * position.
	 *
	 * @param color    the base intensity of the light
	 * @param position the location of the light source
	 */
	public PointLight(Color color, Point position) {
		super(color);
		this.position = position;
	}

	/**
	 * Sets the constant attenuation coefficient {@code kC}.
	 *
	 * @param kC the constant attenuation factor (non-negative)
	 * @return the current {@code PointLight} instance (for method chaining)
	 */
	public PointLight setKc(double kC) {
		this.kC = Math.max(0, kC); // Ensure non-negative values
		return this;
	}

	/**
	 * Sets the linear attenuation coefficient {@code kL}.
	 *
	 * @param kL the linear attenuation factor (non-negative)
	 * @return the current {@code PointLight} instance (for method chaining)
	 */
	public PointLight setKl(double kL) {
		this.kL = Math.max(0, kL);
		return this;
	}

	/**
	 * Sets the quadratic attenuation coefficient {@code kQ}.
	 *
	 * @param kQ the quadratic attenuation factor (non-negative)
	 * @return the current {@code PointLight} instance (for method chaining)
	 */
	public PointLight setKq(double kQ) {
		this.kQ = Math.max(0, kQ);
		return this;
	}

	/**
	 * Computes the light intensity at a given point, considering the distance and
	 * attenuation.
	 *
	 * @param point the point in space to evaluate the light intensity at
	 * @return the color representing the attenuated light intensity at the given
	 *         point
	 */
	@Override
	public Color getIntensity(Point point) {
		double d = position.distance(point);
		double factor = kC + kL * d + kQ * d * d;
		if (Util.isZero(factor)) // Prevent division by zero
			return Color.BLACK;
		return intensity.scale(1d / factor);
	}

	/**
	 * Returns the normalized vector from the light source to a given point.
	 *
	 * @param point the point to compute the direction toward
	 * @return a normalized {@code Vector} from the light source to the point
	 */
	@Override
	public Vector getL(Point point) {
		return point.subtract(position).normalize();
	}

	/**
	 * Returns the distance from the light source to a given point.
	 *
	 * @param point the point to measure distance to
	 * @return the Euclidean distance from the light position to the point
	 */
	@Override
	public double getDistance(Point point) {
		return position.distance(point);
	}
}
