package primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}