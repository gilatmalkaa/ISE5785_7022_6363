package lighting;

import primitives.Color;

/**
 * Abstract base class representing a generic light source.
 * <p>
 * This class serves as a parent for specific types of lights, such as
 * {@link DirectionalLight}, {@link PointLight}, and {@link SpotLight}. Each
 * light has an intensity represented by {@link Color}.
 * </p>
 */
abstract class Light {

	/**
	 * The intensity (color and brightness) of the light.
	 */
	protected final Color intensity;

	/**
	 * Constructs a light source with the specified intensity.
	 *
	 * @param intensity the color and brightness of the light
	 */
	protected Light(Color intensity) {
		this.intensity = intensity;
	}

	/**
	 * Returns the intensity of the light.
	 *
	 * @return the color and brightness of the light
	 */
	public Color getIntensity() {
		return intensity;
	}
}
