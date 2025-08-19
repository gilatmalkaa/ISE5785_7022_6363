package geometries;

import static primitives.Util.*;

import java.util.List;

import primitives.*;

/**
 * Represents an infinite tube in 3D space. A tube is defined by a central axis
 * (represented as a {@link Ray}) and a constant radius. Inherits the radius
 * field from {@link RadialGeometry}.
 */
public class Tube extends RadialGeometry {

	/**
	 * The axis ray that defines the direction and base point of the tube.
	 */
	protected final Ray _ray;

	/**
	 * Constructs a tube with the specified axis and radius.
	 *
	 * @param ray    the axis ray of the tube
	 * @param radius the radius of the tube
	 */
	public Tube(Ray ray, double radius) {
		super(radius);
		_ray = ray;
	}

	/**
	 * Calculates the normal vector to the tube at a given point on its surface. The
	 * normal is defined as the vector from the closest point on the tube's axis to
	 * the given point, and is perpendicular to the axis.
	 *
	 * @param point the point on the tube surface
	 * @return the normal vector at the point
	 * @throws IllegalArgumentException if the point lies exactly on the axis
	 */
	@Override
	public Vector getNormal(Point point) {
		Point p0 = _ray.getP0();
		Vector dir = _ray.getDir();
		Vector p0ToPoint = point.subtract(p0);
		double t = alignZero(dir.dotProduct(p0ToPoint));
		Point o = _ray.getPoint(t);
		Vector normal = point.subtract(o);
		if (isZero(normal.lengthSquared()))
			throw new IllegalArgumentException("Point lies on the axis of the tube – normal is undefined");
		return normal.normalize();
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		// Not implemented
		return null;
	}

	@Override
	protected primitives.AABB computeBoundingBox() {
		// Infinite tube along its axis -> no finite AABB (skip early-reject)
		return null;
	}
}