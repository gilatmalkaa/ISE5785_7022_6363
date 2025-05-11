package geometries;

import static primitives.Util.alignZero;

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
	public List<Point> findIntersections(Ray _ray) {
		Vector _u;
		try {
			_u = _center.subtract(_ray.getP0());
		} catch (IllegalArgumentException ignored) {
			return List.of(_center.add(_ray.getDir().scale(_radius)));
		}

		double _tm = alignZero(_ray.getDir().dotProduct(_u));
		double _dSquared = _u.lengthSquared() - _tm * _tm;
		double thSquared = alignZero(_radiusSquared - _dSquared);
		if (thSquared <= 0)
			return null;
		double _th = alignZero(Math.sqrt(thSquared));

		double _t2 = alignZero(_tm + _th);
		if (_t2 <= 0)
			return null; // both t1 and t2 are not positive – no intersections

		double _t1 = alignZero(_tm - _th);

		// return the points in the correct order – the first point is closest to ray's
		// head
		return _t1 <= 0 ? List.of(_ray.getPoint(_t2)) : List.of(_ray.getPoint(_t1), _ray.getPoint(_t2));
	}
}
