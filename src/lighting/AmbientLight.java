package lighting;

import primitives.Color;

/**
 * Class representing ambient light in a scene. This light illuminates all
 * objects uniformly.
 */
public class AmbientLight {
	/** Final field holding the intensity of the ambient light (IA) */
	private final Color intensity;

	/** Public static constant representing absence of ambient light (BLACK) */
	public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

	/**
	 * Constructs an AmbientLight with the given base color.
	 *
	 * @param iA The intensity of the ambient light
	 */
	public AmbientLight(Color iA) {
		this.intensity = iA;
	}

	/**
	 * Getter for the intensity of the ambient light.
	 *
	 * @return the final intensity (color)
	 */
	public Color getIntensity() {
		return intensity;
	}
}
