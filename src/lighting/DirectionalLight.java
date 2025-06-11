package lighting;

import primitives.*;

/**
 * Represents a directional light source in the scene. Directional light
 * simulates a light source at infinite distance (such as sunlight), with
 * parallel rays in a fixed direction.
 */
public class DirectionalLight extends Light implements LightSource {

	/**
	 * The direction vector of the light (normalized).
	 */
	private final Vector direction;

	/**
	 * Constructs a directional light with specified intensity and direction.
	 *
	 * @param color     the intensity of the light
	 * @param direction the direction of the light
	 */
	public DirectionalLight(Color color, Vector direction) {
		super(color);
		this.direction = direction.normalize();
	}

	@Override
	public Vector getL(Point point) {
		return direction;
	}

	@Override
	public Color getIntensity(Point point) {
		double dotProduct = Math.max(0, direction.dotProduct(getL(point)));
		return intensity.scale(dotProduct);
	}

	@Override
	public double getDistance(Point point) {
		return Double.POSITIVE_INFINITY;
	}
}