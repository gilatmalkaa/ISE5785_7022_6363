package geometries;

import primitives.*;

/**
 * Department for representation Sphere
 */
public class Sphere extends RadialGeometry {

	/**
	 * field for the center point
	 */
	final private Point _center;

	/**
	 * Parameterized constructor for the sphere.
	 *
	 * @param center the center point of the sphere
	 * @param radius the radius of the sphere
	 */
	public Sphere(Point center, double radius) {
		super(radius);
		_center = center;
	}

	/**
	 * Implementation of the method getNormal
	 *
	 * @param point the point to calculate the normal vector for
	 * @return the normal vector at the specified point
	 */
	public Vector getNormal(Point point) {
		return point.subtract(_center).normalize();
	}
}
