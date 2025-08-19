package geometries;

import static primitives.Util.isZero;

import java.util.List;

import primitives.*;

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
	protected final List<Point> _vertices;
	/**
	 * Associated plane in which the polygon lays
	 */
	protected final Plane _plane;
	/**
	 * The size of the polygon - the amount of the vertices in the polygon
	 */
	private final int _size;

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
		this._vertices = List.of(vertices);
		_size = vertices.length;

		// Generate the plane according to the first three vertices and associate the
		// polygon with this plane.
		// The plane holds the invariant normal (orthogonal unit) vector to the polygon
		_plane = new Plane(vertices[0], vertices[1], vertices[2]);
		if (_size == 3)
			return; // no need for more tests for a Triangle

		Vector n = _plane.getNormal(vertices[0]);
		// Subtracting any subsequent points will throw an IllegalArgumentException
		// because of Zero Vector if they are in the same point
		Vector edge1 = vertices[_size - 1].subtract(vertices[_size - 2]);
		Vector edge2 = vertices[0].subtract(vertices[_size - 1]);

		// Cross Product of any subsequent edges will throw an IllegalArgumentException
		// because of Zero Vector if they connect three vertices that lay in the same
		// line.
		// Generate the direction of the polygon according to the angle between last and
		// first edge being less than 180deg. It is hold by the sign of its dot product
		// with the normal. If all the rest consequent edges will generate the same sign
		// - the polygon is convex ("kamur" in Hebrew).
		boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
		for (var i = 1; i < _size; ++i) {
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
		return _plane.getNormal(point);
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		List<Point> intersections = _plane.findIntersections(ray);
		if (intersections == null)
			return null;

		Point p0 = ray.getP0();
		Vector v = ray.getDir();

		Vector v1 = _vertices.getLast().subtract(p0);
		double sign = 0;

		for (Point vertex : _vertices) {
			Vector v2 = vertex.subtract(p0);

			Vector cross = v1.crossProduct(v2);
			double currentSign = v.dotProduct(cross);
			if (isZero(currentSign))
				return null; // on edge or vertex → not inside polygon

			if (sign == 0)
				sign = currentSign > 0 ? 1 : -1;
			else if (sign * currentSign < 0)
				return null; // point is outside

			v1 = v2;
		}

		return List.of(new Intersection(this, intersections.getFirst()));
	}

	@Override
	protected primitives.AABB computeBoundingBox() {
		// Project each vertex onto axes to extract coordinates
		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

		for (primitives.Point p : _vertices) {
			// v = p - (0,0,0)
			primitives.Vector v = p.subtract(primitives.Point.ZERO);

			double x = v.dotProduct(primitives.Vector.AXIS_X);
			double y = v.dotProduct(primitives.Vector.AXIS_Y);
			double z = v.dotProduct(primitives.Vector.AXIS_Z);

			if (x < minX)
				minX = x;
			if (x > maxX)
				maxX = x;
			if (y < minY)
				minY = y;
			if (y > maxY)
				maxY = y;
			if (z < minZ)
				minZ = z;
			if (z > maxZ)
				maxZ = z;
		}

		return new primitives.AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}
}