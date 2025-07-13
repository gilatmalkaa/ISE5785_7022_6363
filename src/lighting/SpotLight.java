package lighting;

import static primitives.Util.alignZero;

import primitives.*;

/**
 * Represents a spotlight in a 3D scene.
 * <p>
 * A spotlight is a point light with a focused beam in a specific direction. The
 * intensity is influenced by the direction and a "narrow beam" factor that
 * controls the sharpness of the beam.
 * </p>
 */
public class SpotLight extends PointLight {

	/**
	 * The normalized direction vector of the spotlight beam.
	 */
	private final Vector direction;

	/**
	 * The narrow beam factor (default = 1). Higher values result in a more focused
	 * light.
	 */
	private double narrowBeam = 1d;

	/**
	 * Constructs a spotlight with the given intensity, direction, and position.
	 *
	 * @param color     the base intensity of the spotlight
	 * @param direction the direction the spotlight is pointing (will be normalized)
	 * @param position  the position of the light source
	 */
	public SpotLight(Color color, Point position, Vector direction) {
		super(color, position);
		this.direction = direction.normalize();
	}

	/**
	 * Sets the narrow beam factor of the spotlight. Higher values produce a more
	 * focused and intense beam.
	 *
	 * @param narrowBeam the narrow beam factor (must be ≥ 1)
	 * @return this {@code SpotLight} instance (for chaining)
	 */
	public SpotLight setNarrowBeam(double narrowBeam) {
		this.narrowBeam = narrowBeam;
		return this;
	}

	@Override
	public SpotLight setKc(double kC) {
		return (SpotLight) super.setKc(kC);
	}

	@Override
	public SpotLight setKl(double kL) {
		return (SpotLight) super.setKl(kL);
	}

	@Override
	public SpotLight setKq(double kQ) {
		return (SpotLight) super.setKq(kQ);
	}

	@Override
	public Color getIntensity(Point point) {
		double dotProduct = alignZero(direction.dotProduct(getL(point)));
		return dotProduct <= 0 ? Color.BLACK //
				: super.getIntensity(point).scale(Math.pow(dotProduct, narrowBeam));
	}

}