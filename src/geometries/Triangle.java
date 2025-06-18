package geometries;

import static primitives.Util.*;

import java.util.List;

import primitives.*;

/**
 * Represents a triangle in 3D space. A triangle is a special case of a polygon
 * with exactly three vertices. Inherits functionality from {@link Polygon}
 * including normal calculation.
 */
public class Triangle extends Polygon {

	/**
	 * Constructs a triangle from three vertices.
	 *
	 * @param p1 the first vertex of the triangle
	 * @param p2 the second vertex of the triangle
	 * @param p3 the third vertex of the triangle
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		List<Point> intersectionPoints = _plane.findIntersections(ray);
		if (intersectionPoints == null)
			return null;

		Point p = intersectionPoints.getFirst(); // Intersection point with the plane

		Point v1 = _vertices.get(0);
		Point v2 = _vertices.get(1);
		Point v3 = _vertices.get(2);

		Vector v = ray.getDir();

		// Vectors from triangle vertices to the intersection point
		Vector p1, p2, p3;
		try {
			p1 = p.subtract(v1);
			p2 = p.subtract(v2);
			p3 = p.subtract(v3);
		} catch (IllegalArgumentException e) {
			// Intersection is exactly at a vertex – not considered inside
			return null;
		}

		// Edge vectors of the triangle
		Vector v1v2 = v2.subtract(v1);
		Vector v2v3 = v3.subtract(v2);
		Vector v3v1 = v1.subtract(v3);

		try {
			double s1 = alignZero(v1v2.crossProduct(p1).dotProduct(v));
			if (isZero(s1))
				return null;
			double s2 = alignZero(v2v3.crossProduct(p2).dotProduct(v));
			if (s1 * s2 <= 0)
				return null;
			double s3 = alignZero(v3v1.crossProduct(p3).dotProduct(v));
			if (s1 * s3 <= 0)
				return null;

			// Point lies inside the triangle
			return List.of(new Intersection(this, p));
		} catch (IllegalArgumentException ignored) {
			// One of the cross products resulted in zero vector – point is on an edge
			return null;
		}
	}
}