package geometries;

import java.util.List;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;
import static primitives.Util.*;

/**
 * A class that represents a plane
 */
public class Plane extends Geometry {

	/**
	 * The point that lies on the plane
	 */
	private final Point _q;
	/**
	 * The normal vector to the plane
	 */
	private final Vector _normal;

	/**
	 * Constructor with parameters.
	 *
	 * @param q      the point on the plane
	 * @param normal the normal vector to the plane
	 */
	public Plane(Point q, Vector normal) {
		this._q = q;
		this._normal = normal.normalize();
	}

	/**
	 * Builder that gets points and calculates the normal vector.
	 *
	 * @param q1 the first point on the plane
	 * @param q2 the second point on the plane
	 * @param q3 the third point on the plane
	 */
	public Plane(Point q1, Point q2, Point q3) {
		_q = q1;
		Vector v1 = q2.subtract(q1);
		Vector v2 = q3.subtract(q1);
		_normal = v1.crossProduct(v2).normalize();
	}

	@Override
	public Vector getNormal(Point point) {
		return _normal;
<<<<<<< HEAD
	}

	@Override
	public List<Point> findIntersections(Ray _ray) {
		// if the ray starts on the plane, there is no intersection
		if (_q.equals(_ray.getP0())) {
			return null;
		}

		// if the ray is parallel to the plane, there is no intersection
		double _nv = _normal.dotProduct(_ray.getDir());
		if (isZero(_nv)) {
			return null;
		}

		// if the ray lies in the plane, there is no intersection
		double _nQMinusP0 = alignZero(_normal.dotProduct(_q.subtract(_ray.getP0())));
		if (isZero(_nQMinusP0)) {
			return null;
		}

		double _t = alignZero(_nQMinusP0 / _nv);
		// if the intersection point is behind or at the ray's origin, return null
		if (_t <= 0) {
			return null;
		}

		return List.of(_ray.getPoint(_t));
=======
>>>>>>> branch 'main' of https://github.com/Gilat1/ISE5785_7022_6363.git
	}
}