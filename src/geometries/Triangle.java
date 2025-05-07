package geometries;

import java.util.List;
import primitives.Ray;
import primitives.Point;
import primitives.Vector;
import static primitives.Util.*;

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
	public List<Point> findIntersections(Ray _ray) {
		// Step 1: Check for intersection with the plane
		List<Point> _intersections = plane.findIntersections(_ray);
		if (_intersections == null) {
			return null;
		}

		// Step 2: Get intersection point with the plane
		Point _intersectionPoint = _intersections.getFirst();

		// Step 3: Get triangle vertices
		Point _a = vertices.get(0);
		Point _b = vertices.get(1);
		Point _c = vertices.get(2);

		// Step 4: Compute vectors
		Vector _u = _b.subtract(_a); // a → b
		Vector _v = _c.subtract(_a); // a → c
		Vector _w = _intersectionPoint.subtract(_a); // a → P

		try {
			// Step 5: First orientation test
			Vector _vCrossW = _v.crossProduct(_w);
			Vector _vCrossU = _v.crossProduct(_u);
			if (_vCrossW.dotProduct(_vCrossU) < 0) {
				return null;
			}

			// Step 6: Second orientation test
			Vector _uCrossW = _u.crossProduct(_w);
			Vector _uCrossV = _u.crossProduct(_v);
			if (_uCrossW.dotProduct(_uCrossV) < 0) {
				return null;
			}
			
			// Step 7: Compute barycentric coordinates (optional, for clarity)
			double _denominator = alignZero(_uCrossV.length());
			double _x = alignZero(_vCrossW.length() / _denominator);
			double _y = alignZero(_uCrossW.length() / _denominator);

			if (_x > 0 && _y > 0 && (_x + _y) < 1) {
				return _intersections;
			}

		} catch (IllegalArgumentException e) {
			// One of the vectors used in crossProduct was a ZERO vector – point lies on
			// edge or vertex
			return null;
		}

		return null;
	}
}