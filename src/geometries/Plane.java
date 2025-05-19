package geometries;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A class that represents a plane
 */
public class Plane extends Geometry {

	/**
	 * The point that lies on the plane
	 */
	private final Point _point;
	/**
	 * The normal vector to the plane
	 */
	private final Vector _normal;

	/**
	 * Constructor with parameters.
	 *
	 * @param point  the point on the plane
	 * @param normal the normal vector to the plane
	 */
	public Plane(Point point, Vector normal) {
		this._point = point;
		this._normal = normal.normalize();
	}

	/**
	 * Builder that gets points and calculates the normal vector.
	 *
	 * @param p1 the first point on the plane
	 * @param p2 the second point on the plane
	 * @param p3 the third point on the plane
	 */
	public Plane(Point p1, Point p2, Point p3) {
		_point = p1;
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		_normal = v1.crossProduct(v2).normalize();
	}

	@Override
	public Vector getNormal(Point point) {
		return _normal;
	}

	@Override
	public List<Point> findIntersections(Ray ray) {
		// if the ray is parallel to the plane, there is no intersection
		double nDotV = _normal.dotProduct(ray.getDir());
		if (isZero(nDotV))
			return null;

		// if the ray starts on the plane, there is no intersection
		Vector vector;
		try {
			vector = _point.subtract(ray.getP0());
		} catch (IllegalArgumentException ignored) {
			return null;
		}

		// if the ray lies in the plane, there is no intersection
		double nQMinusP0 = alignZero(_normal.dotProduct(vector));
		double t = alignZero(nQMinusP0 / nDotV);
		// if the intersection point is behind or at the ray's origin, return null
		return t <= 0 ? null : List.of(ray.getPoint(t));
	}
}