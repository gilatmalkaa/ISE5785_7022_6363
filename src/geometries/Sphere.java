package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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

	@Override

	public List<Point> findIntersections(Ray ray) {
		Vector u;
		try {
			u = _center.subtract(ray.getP0());
		} catch (IllegalArgumentException e) {
			// Ray starts exactly at the center of the sphere – return one point
			return List.of(ray.getPoint(_radius));
		}

		double tm = ray.getDir().dotProduct(u);
		double dSquared = u.lengthSquared() - tm * tm;
		double thSquared = _radiusSquared - dSquared;

		if (thSquared <= 0) {
			return null; // No intersections: ray misses the sphere
		}

		double th = Math.sqrt(thSquared);
		double t1 = tm - th;
		double t2 = tm + th;

		if (t1 > 0 && t2 > 0) {
			return List.of(ray.getPoint(t1), ray.getPoint(t2)); // two intersections
		}
		if (t1 > 0) {
			return List.of(ray.getPoint(t1)); // only t1 is valid
		}
		if (t2 > 0) {
			return List.of(ray.getPoint(t2)); // only t2 is valid
		}

		return null; // both are behind the ray's origin
	}
}