package geometries;

import primitives.*;

/**
 * Abstract base class for all geometric shapes that can be intersected by rays.
 * Inherits from {@link Intersectable} and adds properties like emission color
 * and material.
 */
public abstract class Geometry extends Intersectable {

	/**
	 * Emission color of the geometry (default is black).
	 */
	protected Color emission = Color.BLACK;

	/**
	 * Material properties of the geometry.
	 */
	private Material material = new Material();

	/**
	 * Default empty constructor. Required to allow subclasses to initialize
	 * properly.
	 */
	public Geometry() {
	}

	/**
	 * Returns the emission color of the geometry.
	 *
	 * @return the emission color
	 */
	public Color getEmission() {
		return emission;
	}

	/**
	 * Sets the emission color of the geometry (builder pattern).
	 *
	 * @param emission the emission color
	 * @return the geometry instance (for chaining)
	 */
	public Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}

	/**
	 * Returns the material of the geometry.
	 *
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Sets the material of the geometry (builder pattern).
	 *
	 * @param material the material
	 * @return the geometry instance (for chaining)
	 */
	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}

	/**
	 * Returns the normal vector to the geometry surface at the given point.
	 *
	 * @param point a point on the geometry
	 * @return the normal vector at the point
	 */
	public abstract Vector getNormal(Point point);
}
