package geometries;

import java.util.List;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A class that represents a tube. It extends the RadialGeometry class and
 * includes a ray defining the tube's axis.
 */
public class Tube extends RadialGeometry {

	/**
	 * The ray that defines the axis of the tube.
	 */
	protected final Ray _ray;

	/**
	 * Constructor to create a tube with a specified axis and radius.
	 *
	 * @param ray    the ray defining the axis of the tube
	 * @param radius the radius of the tube
	 */
	public Tube(Ray ray, double radius) {
		super(radius); // Calling the constructor of RadialGeometry to set the radius
		_ray = ray;
	}

	/**
	 * Calculates the normal vector at a given point on the tube's surface. The
	 * normal vector is perpendicular to the surface of the tube at the specified
	 * point.
	 *
	 * @param point the point on the surface of the tube
	 * @return the normal vector at the given point
	 */
	@Override
	public Vector getNormal(Point point) {
		// Get the base point and direction of the tube's axis ray
		Point _p0 = _ray.getP0();
		Vector _dir = _ray.getDir();

		// Vector from the axis base point to the given point
		Vector _p0ToPoint = point.subtract(_p0);

		// Project the vector onto the axis direction to find parameter t
		double _t = alignZero(_dir.dotProduct(_p0ToPoint));

		// Compute the closest point on the axis to the given point
		Point _o = isZero(_t) ? _p0 : _p0.add(_dir.scale(_t));

		// Compute the normal vector from the axis to the point
		Vector _normal = point.subtract(_o);

		// If the point lies exactly on the axis (normal vector is zero), throw
		// exception
		if (isZero(_normal.lengthSquared()))
			throw new IllegalArgumentException("Point lies on the axis of the tube – normal is undefined");

		// Return the normalized normal vector
		return _normal.normalize();
	}

	@Override
	public List<Point> findIntersections(Ray ray) {
		// Not implemented
		return null;
	}
}