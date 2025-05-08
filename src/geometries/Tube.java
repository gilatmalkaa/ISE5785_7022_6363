package geometries;

import java.util.List;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;

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
	public Vector getNormal(Point point) {
		Point p0 = _ray.getP0();// start point
		Vector dir = _ray.getDir();// direction vector
		Vector p0ToPoint = point.subtract(p0);
		double t = dir.dotProduct(p0ToPoint);
		Point o = p0.add(dir.scale(t));
		Vector normal = point.subtract(o);
		if (normal.lengthSquared() == 0)
			throw new IllegalArgumentException("Point lies on the axis of the tube – normal is undefined");
		return normal.normalize();
	}

	@Override
	public List<Point> findIntersections(Ray ray) {
		// Not implemented
		return null;
	}
}