package lighting;

import primitives.Color;

/**
 * Abstract class representing a generic light source. This class serves as a
 * base for different types of lights, such as directional light, point light,
 * or spot light. Each light has an intensity represented by a {@link Color}.
 */
abstract class Light {

	/**
	 * The intensity (color and brightness) of the light.
	 */
	protected final Color intensity;

	/**
	 * Constructs a light source with the given intensity.
	 *
	 * @param intensity the intensity (color) of the light
	 */
	protected Light(Color intensity) {
		this.intensity = intensity;
	}

	/**
	 * Returns the intensity (color) of the light.
	 *
	 * @return the light intensity
	 */
	public Color getIntensity() {
		return intensity;
	}
}
