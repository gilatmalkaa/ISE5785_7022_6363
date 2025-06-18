package geometries;

import static primitives.Util.*;

import java.util.List;

import primitives.*;

/**
 * Class representing an infinite plane in 3D space, defined by a point and a
 * normal vector.
 */
public class Plane extends Geometry {

	/**
	 * A point that lies on the plane.
	 */
	private final Point _point;

	/**
	 * The normalized normal vector to the plane.
	 */
	private final Vector _normal;

	/**
	 * Constructs a plane from a point and a normal vector.
	 *
	 * @param point  a point on the plane
	 * @param normal the normal vector to the plane
	 */
	public Plane(Point point, Vector normal) {
		this._point = point;
		this._normal = normal.normalize();
	}

	/**
	 * Constructs a plane from three non-collinear points. The normal vector is
	 * calculated using the cross product of two edges on the plane.
	 *
	 * @param p1 first point on the plane
	 * @param p2 second point on the plane
	 * @param p3 third point on the plane
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
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		double nDotV = _normal.dotProduct(ray.getDir());
		if (isZero(nDotV))
			return null;

		Vector vector;
		try {
			vector = _point.subtract(ray.getP0());
		} catch (IllegalArgumentException ignored) {
			return null;
		}

		double nQMinusP0 = _normal.dotProduct(vector);
		double t = alignZero(nQMinusP0 / nDotV);

		return t <= 0 ? null : List.of(new Intersection(this, ray.getPoint(t)));
	}
}