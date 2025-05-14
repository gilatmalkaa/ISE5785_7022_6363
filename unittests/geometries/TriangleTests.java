/**
 * 
 */
package geometries;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Triangle} class. Includes tests for
 * getNormal(Point) according to stage 2 requirements.
 */
class TriangleTests {

	/**
	 * Accuracy tolerance used for comparing floating-point values in tests. This
	 * small value accounts for rounding errors when comparing doubles.
	 */

	private static final double DELTA = 1e-10;

	/**
	 * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
	 * Verifies that the returned vector is normalized and orthogonal to the
	 * triangle.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Regular triangle in 3D space
		Point p1 = new Point(0, 0, 1);
		Point p2 = new Point(1, 0, 0);
		Point p3 = new Point(0, 1, 0);

		Triangle triangle = new Triangle(p1, p2, p3);
		Vector normal = triangle.getNormal(p1);

		// Check that the normal is a unit vector
		assertEquals(1, normal.length(), DELTA, "Normal is not a unit vector");

		// Check that the normal is orthogonal to the triangle edges
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		assertEquals(0, normal.dotProduct(v1), DELTA, "Normal is not orthogonal to v1");
		assertEquals(0, normal.dotProduct(v2), DELTA, "Normal is not orthogonal to v2");

		// =============== Boundary Values Tests ==================

		// TC11: Triangle lies flat in the XY plane
		Point q1 = new Point(0, 0, 0);
		Point q2 = new Point(1, 0, 0);
		Point q3 = new Point(0, 1, 0);

		Triangle flatTriangle = new Triangle(q1, q2, q3);
		Vector flatNormal = flatTriangle.getNormal(q1);

		// Expected direction is (0, 0, ±1)
		assertTrue(flatNormal.equals(new Vector(0, 0, 1)) || flatNormal.equals(new Vector(0, 0, -1)),
				"Flat triangle normal direction is not as expected");
	}

	/**
	 * Test method for
	 * {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
	 * We know that this test is not mandatory, since all the validation is handled
	 * in the Polygon class. However, for the sake of clarity and code organization,
	 * we preferred to include it. 😊
	 */
	@Test
	void testTriangle() {
		// TC01: Sanity - triangle builds without exception
		assertDoesNotThrow(() -> new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0)),
				"Triangle constructor failed on valid input");
	}

	/**
	 * Test method for {@link geometries.Triangle#findIntersections(Ray)}.
	 */
	@Test
	void testFindIntersections() {
		Triangle triangle = new Triangle(new Point(0, 1, 0), new Point(-6, 6, 1), new Point(-7, 3, 5));

		// ============ Equivalence Partitions Tests ==============

		// TC01: The intersection point is in the triangle (1 point)
		assertEquals(List.of(new Point(-4, 4, 1)),
				triangle.findIntersections(new Ray(new Point(1, 2, 3), new Vector(-5, 2, -2))),
				"ERROR: The point supposed to be in the triangle - not working as expected");

		// TC02: The intersection point is outside the triangle, against edge (0 point)
		assertNull(triangle.findIntersections(new Ray(new Point(1, 2, 3), new Vector(-9, 3, 0))),
				"ERROR: The point supposed to be outside the triangle, against edge - not working as expected");

		// TC03: The intersection point is outside the triangle, against vertex (0
		// point)
		assertNull(triangle.findIntersections(new Ray(new Point(1, 2, 3), new Vector(-11, 1.86, 4.14))),
				"ERROR: The point supposed to be outside the triangle, against vertex - not working as expected");

		// =============== Boundary Values Tests ==================

		

		// TC10: The point is on edge (0 point)
		assertNull(triangle.findIntersections(new Ray(new Point(1, 2, 3), new Vector(-5, 0.14, -0.15))),
				"ERROR: The point supposed to be on edge - not working as expected");

		// TC11: The point is in a vertex (0 point)
		assertNull(triangle.findIntersections(new Ray(new Point(1, 2, 3), new Vector(-1, -1, -3))),
				"ERROR: The point supposed to be in vertex - not working as expected");

		// TC12: The point is on edge's continuation (0 point)
		assertNull(triangle.findIntersections(new Ray(new Point(3, 0, 0), new Vector(3, -4, -1))),
				"ERROR: The point supposed to be on edge's continuation - not working as expected");
	}
}