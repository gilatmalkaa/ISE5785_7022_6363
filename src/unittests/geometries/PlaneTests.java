/**
 * 
 */
package unittests.geometries;

import java.util.List;
import primitives.Ray;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Plane} class.
 * 
 * @author Gilat Kedem and Shira Amar
 */
class PlaneTests {

	/**
	 * Accuracy tolerance used for comparing floating-point values in tests.
	 */
	private static final double DELTA = 1e-10;

	/**
	 * Tests {@link geometries.Plane#getNormal(Point)}. Ensures returned vector is
	 * normalized and orthogonal to vectors in the plane.
	 */
	@Test
	void testGetNormal() {
		// TC01: Regular plane
		Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
		Vector normal = plane.getNormal(new Point(0, 0, 1));
		assertEquals(1, normal.length(), DELTA, "Plane normal is not a unit vector");
		Vector v1 = new Point(1, 0, 0).subtract(new Point(0, 0, 1));
		Vector v2 = new Point(0, 1, 0).subtract(new Point(0, 0, 1));
		assertEquals(0, normal.dotProduct(v1), DELTA, "Normal not orthogonal to v1");
		assertEquals(0, normal.dotProduct(v2), DELTA, "Normal not orthogonal to v2");

		// TC11: Horizontal plane
		Plane flatPlane = new Plane(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));
		Vector flatNormal = flatPlane.getNormal(new Point(0, 0, 0));
		assertTrue(flatNormal.equals(new Vector(0, 0, 1)) || flatNormal.equals(new Vector(0, 0, -1)));
	}

	/**
	 * Tests the constructor Plane(Point, Vector) Validates correct creation and
	 * handling of invalid zero vector.
	 */
	@Test
	void testPlanePointVector() {
		assertDoesNotThrow(() -> new Plane(new Point(1, 2, 3), new Vector(0, 0, 1)));
		assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 2, 3), new Vector(0, 0, 0)));
	}

	/**
	 * Tests the constructor Plane(Point, Point, Point). Verifies valid and invalid
	 * inputs: duplicate and colinear points.
	 */
	@Test
	void testPlanePointPointPoint() {
		Point p1 = new Point(0, 0, 1);
		Point p2 = new Point(1, 0, 0);
		Point p3 = new Point(0, 1, 0);
		Plane plane = new Plane(p1, p2, p3);
		Vector normal = plane.getNormal(p1);
		assertEquals(1, normal.length(), DELTA);
		assertEquals(0, normal.dotProduct(p2.subtract(p1)), DELTA);
		assertEquals(0, normal.dotProduct(p3.subtract(p1)), DELTA);

		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p3));
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1));
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2));
		assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1));
		Point q1 = new Point(0, 0, 0);
		Point q2 = new Point(1, 1, 1);
		Point q3 = new Point(2, 2, 2);
		assertThrows(IllegalArgumentException.class, () -> new Plane(q1, q2, q3));
	}

	/**
	 * Tests {@link geometries.Plane#findIntersections(Ray)}. Includes EP and BVA:
	 * intersection, no intersection, parallel/orthogonal rays.
	 */
	@Test
	void testFindIntersections() {
		Plane _plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray intersects the plane
		Ray _ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		List<Point> _result1 = _plane.findIntersections(_ray1);
		assertNotNull(_result1, "TC01: Expected intersection point");
		assertEquals(1, _result1.size(), "TC01: Should be exactly 1 intersection");

		// TC02: Ray goes away from the plane
		Ray _ray2 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
		assertNull(_plane.findIntersections(_ray2), "TC02: Ray goes away – no intersection");

		// =============== Boundary Values Tests ==================

		// **** Group 1: Ray is parallel to the plane
		// TC11: Ray parallel and outside the plane
		Ray _ray3 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
		assertNull(_plane.findIntersections(_ray3), "TC11: Ray parallel and outside – no intersection");

		// TC12: Ray lies in the plane
		Ray _ray4 = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
		assertNull(_plane.findIntersections(_ray4), "TC12: Ray lies in the plane – no intersection");

		// **** Group 2: Ray is orthogonal to the plane
		// TC13: Ray orthogonal and starts before the plane
		Ray _ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
		List<Point> _result5 = _plane.findIntersections(_ray5);
		assertEquals(List.of(new Point(0, 0, 1)), _result5, "TC13: Ray should intersect orthogonally");

		// TC14: Ray orthogonal and starts in the plane
		Ray _ray6 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
		assertNull(_plane.findIntersections(_ray6), "TC14: Ray starts in plane – no intersection");

		// TC15: Ray orthogonal and starts after the plane
		Ray _ray7 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
		assertNull(_plane.findIntersections(_ray7), "TC15: Ray starts after plane – no intersection");
	}
}
