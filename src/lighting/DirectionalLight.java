package lighting;

import primitives.*;

/**
 * Class for directional light.
 */
public class DirectionalLight extends Light implements LightSource {

	private final Vector direction;

	/**
	 * Constructor for directional light.
	 * 
	 * @param color     The intensity of the light.
	 * @param direction The direction of the light.
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
		double dotProduct = Math.max(0, direction.dotProduct(getL(point))); // Consider the angle
		return intensity.scale(dotProduct); // Apply attenuation
	}

	@Override
	public double getDistance(Point point) {
		return Double.POSITIVE_INFINITY;
	}
}