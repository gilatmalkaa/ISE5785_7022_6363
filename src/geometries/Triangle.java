package geometries;

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
		// Step 1: Intersect the ray with the triangle's plane
		List<Point> intersections = _plane.findIntersections(ray);
		if (intersections == null)
			return null;

		Point intersectionPoint = intersections.getFirst();

		// Step 2: Exclude the case where the ray starts at the intersection point
		if (intersectionPoint.equals(ray.getP0()))
			return null;

		// Step 3: Extract triangle vertices
		Point a = _vertices.get(0);
		Point b = _vertices.get(1);
		Point c = _vertices.get(2);

		// Step 4: Compute edge vectors from vertex A to B and C
		Vector u = b.subtract(a); // AB
		Vector v = c.subtract(a); // AC

		try {
			// Vector from A to the intersection point
			Vector w = intersectionPoint.subtract(a);

			// Step 5: First orientation test (based on cross products)
			Vector vCrossW = v.crossProduct(w);
			Vector vCrossU = v.crossProduct(u);
			if (vCrossW.dotProduct(vCrossU) < 0)
				return null;

			// Step 6: Second orientation test
			Vector uCrossW = u.crossProduct(w);
			Vector uCrossV = u.crossProduct(v);
			if (uCrossW.dotProduct(uCrossV) < 0)
				return null;

			// If both tests passed, the point lies inside the triangle
			return intersections;

		} catch (IllegalArgumentException e) {
			// One of the vectors was a zero vector — the point lies on an edge or vertex
			return null;
		}
	}
}