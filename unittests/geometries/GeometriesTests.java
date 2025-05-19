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
	/** Empty explicit default constructor to make javadoc generator happy */
	public GeometriesTests() {
	}

	/**
	 * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
	 */
	@Test
	void testFindIntersections() {

		// Create base geometries: Plane, Triangle, Sphere, Polygon
		Plane plane = new Plane(new Point(2, 1, 3), new Point(1, 3, 2), new Point(1, 1, 4));
		Triangle triangle = new Triangle(new Point(1, 3, 4), new Point(4, 3, 1), new Point(2, 3, 1));
		Sphere sphere = new Sphere(new Point(3, 3, 1), 1d);
		Polygon polygon = new Polygon(new Point(0, 5, 2), new Point(2, 5, 1), new Point(4, 5, 1), new Point(2, 5, 5));

		// Shared base geometries object
		Geometries geometries = new Geometries(plane, triangle, sphere, polygon);
		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray intersects 3 out of 4 geometries
		assertEquals(3, geometries.findIntersections(new Ray(new Point(1, 5, 1), new Vector(3, -3, 0.5))).size(),
				"TC01: Ray intersects 3 out of 4 geometries");

		// =============== Boundary Values Tests ==================

		// TC11: Empty geometries collection → should return null
		Geometries emptyGeometries = new Geometries();
		assertNull(emptyGeometries.findIntersections(new Ray(new Point(1, 5, 1), new Vector(3, -3, 0.5))),
				"TC11: Empty collection should return null");

		// TC12: Ray misses all geometries → should return null
		Geometries geometriesWithExtra = new Geometries(plane, triangle, sphere, polygon);
		geometriesWithExtra.add(new Sphere(new Point(4, 6, 7), 2d)); // Adding extra geometry
		assertNull(geometriesWithExtra.findIntersections(new Ray(new Point(8, 9, 10), new Vector(1, 0, -1))),
				"TC12: Ray misses all geometries");

		// TC13: Ray intersects exactly one geometry → should return 1 point
		assertEquals(1, geometries.findIntersections(new Ray(new Point(8, 9, 10), new Vector(0, -2, -3))).size(),
				"TC13: Ray intersects only one geometry");

		// TC14: Ray intersects all geometries → should return 3 points
		assertEquals(3, geometries.findIntersections(new Ray(new Point(3, 7, 10), new Vector(0, -4, -8))).size(),
				"T14: Ray intersects all geometries");

	}

}