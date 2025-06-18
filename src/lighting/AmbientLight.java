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
	 * Constructs ambient light with given intensity and a reflection coefficient.
	 * The final intensity is calculated by scaling {@code ia} with {@code ka}.
	 *
	 * @param ia the base intensity of the ambient light
	 */
	public AmbientLight(Color ia) {
		super(ia);
	}

}
