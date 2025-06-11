package lighting;

import primitives.*;

/**
 * Class representing a point light source in 3D space.
 * <p>
 * A point light emits light equally in all directions from a specific position.
 * The light intensity is affected by distance using an attenuation formula.
 * </p>
 */
public class PointLight extends Light implements LightSource {

	/**
	 * The position of the light source in space.
	 */
	protected Point position;

	/**
	 * Constant attenuation coefficient (default = 1.0).
	 */
	private double kC = 1d;

	/**
	 * Linear attenuation coefficient (default = 0.0).
	 */
	private double kL = 0d;

	/**
	 * Quadratic attenuation coefficient (default = 0.0).
	 */
	private double kQ = 0d;

	/**
	 * Constructs a new point light with the specified intensity and position.
	 *
	 * @param color    the base intensity of the light
	 * @param position the location of the point light in the scene
	 */
	public PointLight(Color color, Point position) {
		super(color);
		this.position = position;
	}

	/**
	 * Sets the constant attenuation coefficient.
	 *
	 * @param kC the constant attenuation factor (non-negative)
	 * @return this {@code PointLight} instance (for chaining)
	 */
	public PointLight setKc(double kC) {
		this.kC = Math.max(0, kC);
		return this;
	}

	/**
	 * Sets the linear attenuation coefficient.
	 *
	 * @param kL the linear attenuation factor (non-negative)
	 * @return this {@code PointLight} instance (for chaining)
	 */
	public PointLight setKl(double kL) {
		this.kL = Math.max(0, kL);
		return this;
	}

	/**
	 * Sets the quadratic attenuation coefficient.
	 *
	 * @param kQ the quadratic attenuation factor (non-negative)
	 * @return this {@code PointLight} instance (for chaining)
	 */
	public PointLight setKq(double kQ) {
		this.kQ = Math.max(0, kQ);
		return this;
	}

	@Override
	public Color getIntensity(Point point) {
		double d = position.distance(point);
		double factor = kC + kL * d + kQ * d * d;
		if (Util.isZero(factor))
			return Color.BLACK;
		return intensity.scale(1d / factor);
	}

	@Override
	public Vector getL(Point point) {
		return point.subtract(position).normalize();
	}

	@Override
	public double getDistance(Point point) {
		return position.distance(point);
	}
}
