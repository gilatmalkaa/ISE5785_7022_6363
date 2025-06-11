package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.*;

/**
 * Unit tests for {@link geometries.Geometries} class. This class tests the
 * ability of {@code Geometries} to find intersections with a collection of
 * geometric objects such as Plane, Triangle, Sphere, and Polygon.
 */
class GeometriesTests {

	/**
	 * Default constructor for the test class. Required for JavaDoc generation
	 * tools.
	 */
	public GeometriesTests() {
	}

	/**
	 * Test method for {@link geometries.Geometries#findIntersections(Ray)}. Tests
	 * various scenarios of ray intersections with multiple geometric shapes.
	 * Includes both equivalence partitioning (EP) and boundary value analysis (BVA)
	 * tests.
	 */
	@Test
	void testFindIntersections() {

		// Create base geometries: Plane, Triangle, Sphere, Polyg
		Plane plane = new Plane(new Point(2, 1, 3), new Point(1, 3, 2), new Point(1, 1, 4));
		Triangle triangle = new Triangle(new Point(1, 3, 4), new Point(4, 3, 1), new Point(2, 3, 1));
		Sphere sphere = new Sphere(1d, new Point(3, 3, 1));
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
		geometriesWithExtra.add(new Sphere(2d, new Point(4, 6, 7))); // Adding extra geometry
		assertNull(geometriesWithExtra.findIntersections(new Ray(new Point(8, 9, 10), new Vector(1, 0, -1))),
				"TC12: Ray misses all geometries");

		// TC13: Ray intersects exactly one geometry → should return 1 point
		assertEquals(1, geometries.findIntersections(new Ray(new Point(8, 9, 10), new Vector(0, -2, -3))).size(),
				"TC13: Ray intersects only one geometry");

		// TC14: Ray intersects all geometries → should return 3 points
		assertEquals(3, geometries.findIntersections(new Ray(new Point(3, 7, 10), new Vector(0, -4, -8))).size(),
				"TC14: Ray intersects all geometries");

	}
}