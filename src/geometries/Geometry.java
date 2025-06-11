package geometries;

import primitives.*;

/**
 * An interface that characterizes a geometric shape
 */
public abstract class Geometry extends Intersectable {
	/**
	 * Empty explicit default constructor to make javadoc generator happy
	 */
	public Geometry() {
	}

	protected Color emission = Color.BLACK;
	private Material material = new Material();

	/**
	 * Get the emission color of the geometry
	 * 
	 * @return emission color
	 */
	public Color getEmission() {
		return emission;
	}

	/**
	 * Set the emission color of the geometry (builder pattern)
	 * 
	 * @param emission color
	 * @return the geometry itself
	 */
	public Geometry setEmission(Color emission) {
		this.emission = emission;
		return this;
	}

	/**
	 * Get the material properties of the geometry
	 * 
	 * @return material properties
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Set the material properties of the geometry (builder pattern)
	 * 
	 * @param material the material properties
	 * @return the geometry itself
	 */
	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}

	/**
	 * Get the normal vector at a point on the geometry
	 * 
	 * @param point the point on the geometry
	 * @return normal vector
	 */
	public abstract Vector getNormal(Point point);
}
