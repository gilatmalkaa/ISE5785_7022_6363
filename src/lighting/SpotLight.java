package lighting;

import primitives.*;

public class SpotLight extends PointLight {
	private final Vector direction;
	private double narrowBeam = 1d;

	@Override
	public SpotLight setKc(double kC) {
		super.setKc(kC);
		return this;
	}

	@Override
	public SpotLight setKl(double kL) {
		super.setKl(kL);
		return this;
	}

	@Override
	public SpotLight setKq(double kQ) {
		super.setKq(kQ);
		return this;
	}

	/**
	 * Constructor for SpotLight.
	 * 
	 * @param color     The intensity of the light.
	 * @param direction The direction of the spotlight beam.
	 * @param position  The position of the light source.
	 */
	public SpotLight(Color color, Vector direction, Point position) {
		super(color, position);
		this.direction = direction.normalize();
	}

	/**
	 * Set the narrow beam factor of the spotlight.
	 * 
	 * @param narrowBeam The narrow beam factor.
	 * @return The SpotLight instance (for method chaining).
	 */
	public SpotLight setNarrowBeam(double narrowBeam) {
		this.narrowBeam = narrowBeam;
		return this;
	}

	@Override
	public Color getIntensity(Point point) {
		Color oldColor = super.getIntensity(point);
		double dotProduct = Math.max(0d, direction.dotProduct(getL(point)));
		return oldColor.scale(Math.pow(dotProduct, narrowBeam)); // Apply narrowBeam attenuation
	}

	/**
	 * Returns the distance between the light source and a given point.
	 * 
	 * @param point The point to calculate the distance to.
	 * @return The distance between the light source and the given point.
	 */
	@Override
	public double getDistance(Point point) {
		return position.distance(point); // Added getDistance method
	}
}
