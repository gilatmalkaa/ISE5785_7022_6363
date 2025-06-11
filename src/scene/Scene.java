package scene;

import java.util.LinkedList;
import java.util.List;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

/**
 * Represents a 3D scene for rendering. This is a Plain Data Structure (PDS),
 * meaning it contains public fields and fluent-style setters for configuration.
 * <p>
 * Default values:
 * <ul>
 * <li>{@code background}: {@link Color#BLACK}</li>
 * <li>{@code ambientLight}: {@link AmbientLight#NONE}</li>
 * <li>{@code geometries}: new {@link Geometries}()</li>
 * <li>{@code lights}: empty list</li>
 * </ul>
 * 
 * @author Gilat Kedem and Shira Amar
 */
public class Scene {

	/** The name of the scene (cannot be changed after construction). */
	public final String name;

	/** The background color of the scene. */
	public Color background = Color.BLACK;

	/** The ambient light of the scene. */
	public AmbientLight ambientLight = AmbientLight.NONE;

	/** The collection of geometries contained in the scene. */
	public Geometries geometries = new Geometries();

	/** The list of light sources in the scene. */
	public List<LightSource> lights = new LinkedList<>();

	/**
	 * Constructs a new Scene with the given name.
	 *
	 * @param name the name of the scene
	 */
	public Scene(String name) {
		this.name = name;
	}

	/**
	 * Sets the background color of the scene.
	 *
	 * @param background the new background color
	 * @return this Scene instance (for method chaining)
	 */
	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}

	/**
	 * Sets the ambient light of the scene.
	 *
	 * @param ambientLight the new ambient light
	 * @return this Scene instance (for method chaining)
	 */
	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}

	/**
	 * Sets the geometries collection for the scene.
	 *
	 * @param geometries the new set of geometries
	 * @return this Scene instance (for method chaining)
	 */
	public Scene setGeometries(Geometries geometries) {
		this.geometries = geometries;
		return this;
	}

	/**
	 * Adds a light source to the scene.
	 *
	 * @param light the light source to add
	 * @return this Scene instance (for method chaining)
	 */
	public Scene addLight(LightSource light) {
		this.lights.add(light);
		return this;
	}
}