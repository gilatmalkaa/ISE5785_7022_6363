package primitives;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for primitives.Ray class
 */
class RayTests {
	/**
	 * Empty explicit default constructor to satisfy JavaDoc generator
	 */
	public RayTests() {
	}

	/**
	 * Test method for {@link primitives.Ray#getPoint(double)}.
	 */
	@Test
	void testGetPoint() {
		Ray ray = new Ray(new Point(1, 1, 1), new Vector(1, 0, 0));

		// ============ Equivalence Partitions Tests ==============

		// EP01: t is positive (positive distance)
		assertEquals(new Point(3, 1, 1), ray.getPoint(2), "Ray.getPoint() for t > 0 is incorrect");

		// EP02: t is negative (negative distance)
		assertEquals(new Point(-1, 1, 1), ray.getPoint(-2), "Ray.getPoint() for t < 0 is incorrect");

		// =============== Boundary Values Tests ==================

		// BV01: t = 0 (zero distance, should return the head point)
		assertEquals(new Point(1, 1, 1), ray.getPoint(0), "Ray.getPoint() for t = 0 does not return the head");
	}

	/**
	 * Test method for {@link primitives.Ray#findClosestPoint(java.util.List)}.
	 */
	@Test
	void testFindClosestPoint() {
		Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

		// ============ Equivalence Partitions Tests ==============

		// EP01: Closest point is in the middle of the list
		Point p1 = new Point(1, 2, 0);
		Point p2 = new Point(0.5, 0, 0); // Closest
		Point p3 = new Point(2, 2, 0);
		assertEquals(p2, ray.findClosestPoint(List.of(p1, p2, p3)),
				"EP01: Incorrect closest point when it is in the middle");

		// =============== Boundary Values Tests ==================

		// BV01: Null list
		assertNull(ray.findClosestPoint(null), "BV01: Expected null for null list");

		// BV03: Closest point is the first in the list
		Point p4 = new Point(0.5, 0, 0); // Closest
		Point p5 = new Point(1, 1, 0);
		Point p6 = new Point(2, 2, 0);
		assertEquals(p4, ray.findClosestPoint(List.of(p4, p5, p6)), "BV03: First point should be the closest");

		// BV04: Closest point is the last in the list
		Point p7 = new Point(3, 1, 0);
		Point p8 = new Point(2, 1, 0);
		Point p9 = new Point(0.5, 0, 0); // Closest
		assertEquals(p9, ray.findClosestPoint(List.of(p7, p8, p9)), "BV04: Last point should be the closest");
	}

}