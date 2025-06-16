package lighting;

import primitives.Color;
import primitives.Double3;

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
	public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

	/**
	 * Constructs ambient light with given intensity and a reflection coefficient.
	 * The final intensity is calculated by scaling {@code ia} with {@code ka}.
	 *
	 * @param ia the base intensity of the ambient light
	 * @param ka the ambient reflection coefficient (as {@link Double3})
	 */
	public AmbientLight(Color ia, Double3 ka) {
		super(ia.scale(ka));
	}

	/**
	 * Constructs ambient light with given intensity and a scalar reflection
	 * coefficient. The final intensity is calculated by scaling {@code ia} with
	 * {@code ka}.
	 *
	 * @param ia the base intensity of the ambient light
	 * @param ka the ambient reflection coefficient (as {@code double})
	 */
	public AmbientLight(Color ia, double ka) {
		super(ia.scale(ka));
	}
}