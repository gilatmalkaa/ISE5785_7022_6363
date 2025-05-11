package geometries;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
	/**
	 * List of polygon's vertices
	 */
	protected final List<Point> vertices;
	/**
	 * Associated plane in which the polygon lays
	 */
	protected final Plane plane;
	/**
	 * The size of the polygon - the amount of the vertices in the polygon
	 */
	private final int size;

	/**
	 * Polygon constructor based on vertices list. The list must be ordered by edge
	 * path. The polygon must be convex.
	 *
	 * @param vertices list of vertices according to their order by edge path
	 * @throws IllegalArgumentException in any case of illegal combination of
	 *                                  vertices:
	 *                                  <ul>
	 *                                  <li>Less than 3 vertices</li>
	 *                                  <li>Consequent vertices are in the same
	 *                                  point
	 *                                  <li>The vertices are not in the same
	 *                                  plane</li>
	 *                                  <li>The order of vertices is not according
	 *                                  to edge path</li>
	 *                                  <li>Three consequent vertices lay in the
	 *                                  same line (180&#176; angle between two
	 *                                  consequent edges)
	 *                                  <li>The polygon is concave (not convex)</li>
	 *                                  </ul>
	 */
	public Polygon(Point... vertices) {
		if (vertices.length < 3)
			throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
		this.vertices = List.of(vertices);
		size = vertices.length;

		// Generate the plane according to the first three vertices and associate the
		// polygon with this plane.
		// The plane holds the invariant normal (orthogonal unit) vector to the polygon
		plane = new Plane(vertices[0], vertices[1], vertices[2]);
		if (size == 3)
			return; // no need for more tests for a Triangle

		Vector n = plane.getNormal(vertices[0]);
		// Subtracting any subsequent points will throw an IllegalArgumentException
		// because of Zero Vector if they are in the same point
		Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
		Vector edge2 = vertices[0].subtract(vertices[size - 1]);

		// Cross Product of any subsequent edges will throw an IllegalArgumentException
		// because of Zero Vector if they connect three vertices that lay in the same
		// line.
		// Generate the direction of the polygon according to the angle between last and
		// first edge being less than 180deg. It is hold by the sign of its dot product
		// with the normal. If all the rest consequent edges will generate the same sign
		// - the polygon is convex ("kamur" in Hebrew).
		boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
		for (var i = 1; i < size; ++i) {
			// Test that the point is in the same plane as calculated originally
			if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
				throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
			// Test the consequent edges have
			edge1 = edge2;
			edge2 = vertices[i].subtract(vertices[i - 1]);
			if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
				throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
		}
	}

	@Override
	public Vector getNormal(Point point) {
		return plane.getNormal(point);
	}

	@Override
	public List<Point> findIntersections(Ray _ray) {
		List<Point> _intersections = plane.findIntersections(_ray);
		if (_intersections == null)
			return null;

		Point _p0 = _ray.getP0();
		Vector _v = _ray.getDir();

		Vector _v1 = vertices.getLast().subtract(_p0);
		double _sign = 0;

		for (Point _vertex : vertices) {
			Vector _v2 = _vertex.subtract(_p0);

			Vector _cross = _v1.crossProduct(_v2);
			double _currentSign = alignZero(_v.dotProduct(_cross));

			if (isZero(_currentSign)) {
				return null; // on edge or vertex → not inside polygon
			}

			if (_sign == 0) {
				_sign = _currentSign > 0 ? 1 : -1;
			} else if (_sign * _currentSign < 0) {
				return null; // point is outside
			}

			_v1 = _v2;
		}

		Point _intersectionPoint = _intersections.getFirst();
		return List.of(_intersectionPoint);
	}

}