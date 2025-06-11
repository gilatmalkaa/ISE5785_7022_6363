package geometries;

import static primitives.Util.alignZero;

import java.util.List;

import primitives.*;

/**
 * Sphere class represents a three-dimensional sphere in 3D space. A sphere is
 * defined by its center point and a radius. This class extends
 * {@link RadialGeometry} and supports calculating the normal at a given point
 * and finding intersection points with a ray.
 * 
 * The sphere is considered to be infinite in all directions (no bounding box).
 * 
 * @author Gilat Kedem and Shira Amar
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
	public Sphere(double radius, Point center) {
		super(radius);
		this._center = center;
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
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		Vector u;
		try {
			// u = vector from ray origin to sphere center
			u = _center.subtract(ray.getP0());
		} catch (IllegalArgumentException e) {
			// Ray starts exactly at the center of the sphere → return one point
			return List.of(new Intersection(this, ray.getPoint(_radius)));
		}

		double tm = ray.getDir().dotProduct(u);
		double dSquared = u.lengthSquared() - tm * tm;
		double thSquared = alignZero(_radiusSquared - dSquared);

		if (thSquared <= 0)
			return null; // no intersections

		double th = Math.sqrt(thSquared);
		double t1 = alignZero(tm - th);
		double t2 = alignZero(tm + th);

		return t1 > 0 && t2 > 0
				? List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)))
				: t1 > 0 ? List.of(new Intersection(this, ray.getPoint(t1)))
						: t2 > 0 ? List.of(new Intersection(this, ray.getPoint(t2))) : null;
	}
}