package geometries;

import static primitives.Util.alignZero;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A class that represents a triangle, which is a specific type of polygon. The
 * triangle is defined by three points in 3D space.
 */
public class Triangle extends Polygon {

	/**
	 * Constructor to create a triangle from three points.
	 *
	 * @param p1 the first point of the triangle
	 * @param p2 the second point of the triangle
	 * @param p3 the third point of the triangle
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3); // Calls the constructor of Polygon to create the triangle
	}

	@Override
	public List<Point> findIntersections(Ray ray) {
		List<Point> intersectionPoints = _plane.findIntersections(ray);
		if (intersectionPoints == null)
			return null;

		Point p = intersectionPoints.get(0); // Intersection point with the plane

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
			// If the intersection point is exactly at one of the triangle's vertices,
			// the resulting vector will be zero → considered outside
			return null;
		}

		// Edge vectors of the triangle
		Vector v1v2 = v2.subtract(v1);
		Vector v2v3 = v3.subtract(v2);
		Vector v3v1 = v1.subtract(v3);

		try {
			double s1 = alignZero(v1v2.crossProduct(p1).dotProduct(v));
			double s2 = alignZero(v2v3.crossProduct(p2).dotProduct(v));
			double s3 = alignZero(v3v1.crossProduct(p3).dotProduct(v));

			// If all dot products have the same sign, the point is inside the triangle
			if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
				return List.of(p);
			}
		} catch (IllegalArgumentException e) {
			// One of the cross products resulted in a zero vector → point is on an edge
			// or vertex → not considered inside
			return null;
		}

		return null;
	}

}