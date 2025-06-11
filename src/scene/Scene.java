package scene;

import java.util.LinkedList;
import java.util.List;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource; // Added import
import primitives.Color;

/**
 * The Scene class represents a 3D scene for rendering. It is a Plain Data
 * Structure (PDS), containing public fields and fluent-style setters.
 * 
 * Default values: - background: Color.BLACK - ambientLight: AmbientLight.NONE -
 * geometries: new Geometries()
 * 
 * @author Gilat Kedem and Shira Amar
 */
public class Scene {
	/** The name of the scene (cannot be changed after construction) */
	public final String name;

	/** The background color of the scene */
	public Color background = Color.BLACK;

	/** The ambient light in the scene */
	public AmbientLight ambientLight = AmbientLight.NONE;

	/** The collection of geometries in the scene */
	public Geometries geometries = new Geometries();

	public List<LightSource> lights = new LinkedList<>(); // Added lights list

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
	 * @return the current Scene instance (for chaining)
	 */
	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}

	/**
	 * Sets the ambient light of the scene.
	 * 
	 * @param ambientLight the new ambient light
	 * @return the current Scene instance (for chaining)
	 */
	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}

	/**
	 * Sets the geometries of the scene.
	 * 
	 * @param geometries the new set of geometries
	 * @return the current Scene instance (for chaining)
	 */
	public Scene setGeometries(Geometries geometries) {
		this.geometries = geometries;
		return this;
	}

	/**
	 * Adds a light source to the scene.
	 * 
	 * @param light The light source to add.
	 * @return The current Scene object (for method chaining).
	 */
	public Scene addLight(LightSource light) {
		this.lights.add(light);
		return this;
	}
}
