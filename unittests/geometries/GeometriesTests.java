package geometries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Geometries} class. This class tests the
 * ability of Geometries to find intersections with a collection of geometric
 * objects.
 */
class GeometriesTests {

	/**
	 * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
	 */
	@Test
	void testFindIntersections() {
		// Create a set of geometries: Plane, Triangle, Sphere, Polygon
		Plane _plane = new Plane(new Point(2, 1, 3), new Point(1, 3, 2), new Point(1, 1, 4));
		Triangle _triangle = new Triangle(new Point(1, 3, 4), new Point(4, 3, 1), new Point(2, 3, 1));
		Sphere _sphere = new Sphere(new Point(3, 3, 1), 1d);
		Polygon _polygon = new Polygon(new Point(0, 5, 2), new Point(2, 5, 1), new Point(4, 5, 1), new Point(2, 5, 5));
		Geometries _geometries = new Geometries(_plane, _triangle, _sphere, _polygon);

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray intersects 3 out of the 4 geometries
		assertEquals(3, _geometries.findIntersections(new Ray(new Point(1, 5, 1), new Vector(3, -3, 0.5))).size(),
				"TC01: Ray intersects 3 out of 4 geometries");

		// =============== Boundary Values Tests ==================

		// TC02: Empty geometries collection → should return null
		Geometries _emptyGeometries = new Geometries();
		assertNull(_emptyGeometries.findIntersections(new Ray(new Point(1, 5, 1), new Vector(3, -3, 0.5))),
				"TC02: Empty collection should return null");

		// TC03: Ray misses all geometries → should return null
		_geometries.add(new Sphere(new Point(4, 6, 7), 2d)); // Adding an extra geometry for variety
		assertNull(_geometries.findIntersections(new Ray(new Point(8, 9, 10), new Vector(1, 0, -1))),
				"TC03: Ray misses all geometries");

		// TC04: Ray intersects exactly one geometry → should return 1 point
		assertEquals(1, _geometries.findIntersections(new Ray(new Point(8, 9, 10), new Vector(0, -2, -3))).size(),
				"TC04: Ray intersects only one geometry");

		// TC05: Ray intersects all geometries → should return 5 points
		assertEquals(5, _geometries.findIntersections(new Ray(new Point(3, 7, 10), new Vector(0, -4, -8))).size(),
				"TC05: Ray intersects all geometries");
	}
}