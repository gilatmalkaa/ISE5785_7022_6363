package geometries;

import static primitives.Util.alignZero;

import java.util.List;

import primitives.*;

/**
 * Sphere class represents a three-dimensional sphere in 3D space. A sphere is
 * defined by its center point and radius. This class extends
 * {@link RadialGeometry} and supports normal calculation and ray-sphere
 * intersection.
 * <p>
 * The sphere is considered to be infinite in all directions (no bounding box).
 * </p>
 * 
 * @author Gilat Kedem and Shira Amar
 */
public class Sphere extends RadialGeometry {

	/**
	 * The center point of the sphere.
	 */
	private final Point _center;

	/**
	 * Constructs a sphere with the specified center and radius.
	 *
	 * @param radius the radius of the sphere
	 * @param center the center point of the sphere
	 */
	public Sphere(double radius, Point center) {
		super(radius);
		this._center = center;
	}

	/**
	 * Returns the normal vector to the sphere at the given point on its surface.
	 *
	 * @param point the point on the sphere
	 * @return the normalized vector from the center to the point
	 */
	public Vector getNormal(Point point) {
		return point.subtract(_center).normalize();
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		Vector u;
		try {
			// Vector from ray origin to sphere center
			u = _center.subtract(ray.getP0());
		} catch (IllegalArgumentException e) {
			// Ray starts at the center of the sphere
			return List.of(new Intersection(this, ray.getPoint(_radius)));
		}

		double tm = ray.getDir().dotProduct(u);
		double dSquared = u.lengthSquared() - tm * tm;
		double thSquared = alignZero(_radiusSquared - dSquared);

		if (thSquared <= 0)
			return null; // No intersections

		double th = Math.sqrt(thSquared);
		double t1 = alignZero(tm - th);
		double t2 = alignZero(tm + th);

		return t1 > 0 && t2 > 0
				? List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)))
				: t1 > 0 ? List.of(new Intersection(this, ray.getPoint(t1)))
						: t2 > 0 ? List.of(new Intersection(this, ray.getPoint(t2))) : null;
	}
}