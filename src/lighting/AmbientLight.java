package lighting;

import primitives.Color;

/**
 * Represents ambient light in a 3D scene.
 * <p>
 * Ambient light is a basic type of light that affects all objects in the scene
 * equally, regardless of their position or orientation. It simulates indirect
 * scattered light.
 * </p>
 */
public class AmbientLight extends Light {
	/**
	 * Constant representing no ambient light.
	 */
	public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

	/**
	 * Constructs ambient light with a given color intensity.
	 * 
	 * @param intensity Color representing the ambient light intensity (IA)
	 */
	public AmbientLight(Color intensity) {
		super(intensity);
	}
}