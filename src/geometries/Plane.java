package geometries;

import primitives.*;

/**
 * A class that represents a plane
 */
public class Plane extends Geometry {

	/**
	 * The point that lies on the plane
	 */
	private final Point q;
	/**
	 * The normal vector to the plane
	 */
	private final Vector normal;

	/**
	 * Constructor with parameters.
	 *
	 * @param q      the point on the plane
	 * @param normal the normal vector to the plane
	 */
	public Plane(Point q, Vector normal) {
		this.q = q;
		this.normal = normal.normalize();
	}

	/**
	 * Builder that gets points and calculates the normal vector.
	 *
	 * @param q1 the first point on the plane
	 * @param q2 the second point on the plane
	 * @param q3 the third point on the plane
	 */
	public Plane(Point q1, Point q2, Point q3) {
		q = q1;
		Vector v1 = q2.subtract(q1);
		Vector v2 = q3.subtract(q1);
		normal = v1.crossProduct(v2).normalize();
	}

	@Override
	public Vector getNormal(Point point) {
		return normal;
	}
}