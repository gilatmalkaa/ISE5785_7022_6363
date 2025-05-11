/**
 * 
 */
package primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for primitives.Point class
 * 
 * @author Gilat Kedem and Shira Amar
 */

class PointTests {

	/** A sample point for tests: (1, 2, 3) */
	private final static Point _P1 = new Point(1, 2, 3);

	/** Another point for tests: origin (0, 0, 0) */
	private final static Point _P2 = new Point(0, 0, 0);

	/** A sample vector for vector-point operations */
	private final static Vector _V1 = new Vector(1, -1, 2);

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)} This test
	 * checks subtraction between two points, and returns a vector from the second
	 * point to the first.
	 */
	@Test
	void testSubtract() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Subtract P2 (0,0,0) from P1 (1,2,3) → should return Vector(1,2,3)
		assertEquals(new Vector(1, 2, 3), _P1.subtract(_P2), "subtract() did not return the correct vector");

		// TC02: Subtract new Point(2,3,4) from new Point(5,5,5) → Vector(3,2,1)
		assertEquals(new Vector(3, 2, 1), new Point(5, 5, 5).subtract(new Point(2, 3, 4)),
				"subtract() did not return the correct vector");

		// =============== Boundary Values Tests ==================

		// TC11: Subtract same point → should return zero vector (or throw exception)
		assertThrows(IllegalArgumentException.class, () -> _P1.subtract(new Point(1, 2, 3)),
				"subtract() did not throw an exception for zero vector");
	}

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)} This test
	 * checks the addition of a vector to a point, which should return a new point
	 * shifted by the vector.
	 */
	@Test
	void testAdd() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Add vector that brings P1 to origin → should return P2
		assertEquals(_P2, _P1.add(new Vector(-1, -2, -3)), "add() did not return the correct point");

		// TC02: Add V1 to P2 → should return Point(1,1,2)
		assertEquals(new Point(1, -1, 2), _P2.add(_V1), "add() did not return the correct point");

		// =============== Boundary Values Tests ==================

		// TC11: Add zero vector to P1 -> should throw exception
		assertThrows(IllegalArgumentException.class, () -> _P1.add(new Vector(0, 0, 0)),
				"add() did not throw exception on zero vector");
	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}
	 * This test checks the squared distance between two points.
	 */
	@Test
	void testDistanceSquared() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: distanceSquared between P1 (1,2,3) and P2 (0,0,0) → should be 14
		assertEquals(14, _P1.distanceSquared(_P2), "distanceSquared() did not return expected result");

		// TC02: distanceSquared between P1 (1,2,3) and Point(2,3,4) → (1^2 + 1^2 + 1^2)
		// = 3
		assertEquals(3, _P1.distanceSquared(new Point(2, 3, 4)), "distanceSquared() did not return expected result");

		// =============== Boundary Values Tests ==================

		// TC11: distanceSquared between P1 and itself → should be 0
		assertEquals(0, _P1.distanceSquared(_P1), "distanceSquared() between same point should return 0");
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}. This
	 * test checks the actual distance between two points.
	 */
	@Test
	void testDistance() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: distance between P1 (1,2,3) and P2 (0,0,0) → sqrt(14)
		assertEquals(Math.sqrt(14), _P1.distance(_P2), 0.00001, "distance() did not return expected result");

		// TC02: distance between P2 (0,0,0) and Point(3,4,0) → should be 5 (3-4-5
		// triangle)
		assertEquals(5, _P2.distance(new Point(3, 4, 0)), 0.00001, "distance() did not return expected result");

		// =============== Boundary Values Tests ==================

		// TC11: distance between P1 and itself → should return 0
		assertEquals(0, _P1.distance(_P1), 0.00001, "distance() between same point should return 0");
	}

}
