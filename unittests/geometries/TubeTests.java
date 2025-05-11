package geometries;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Tube} class.
 * 
 * @author Gilat Kedem and Shira Amar.
 */
class TubeTests {

	/**
	 * Test constructor of {@link geometries.Tube}. Ensures invalid radius or
	 * axisRay inputs are rejected.
	 */
	@Test
	void testTube() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Valid radius and axisRay
		assertDoesNotThrow(() -> new Tube(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), 1),
				"Constructor failed on valid input");

		// =============== Boundary Values Tests ==================

		// TC11: Radius is 0 → should throw exception
		assertThrows(IllegalArgumentException.class,
				() -> new Tube(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), 0),
				"Constructor should throw on radius = 0");

		// TC12: Radius is negative → should throw exception
		assertThrows(IllegalArgumentException.class,
				() -> new Tube(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), -1),
				"Constructor should throw on negative radius");

		// TC13: AxisRay with zero direction vector → should throw exception
		assertThrows(IllegalArgumentException.class,
				() -> new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 0)), 1),
				"Constructor should throw when axis ray has zero direction vector");
	}

	/**
	 * Test method for {@link geometries.Tube#getNormal(primitives.Point)}. Verifies
	 * the returned normal vector is correct in both EP and BVA cases.
	 */
	@Test
	void testGetNormal() {
		Tube tube = new Tube(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1);

		// ============ Equivalence Partitions Tests ==============
		// TC01: Point on the side of the tube at (1, 0, 5) should return (1, 0, 0)
		Vector expected = new Vector(1, 0, 0);
		Vector actual = tube.getNormal(new Point(1, 0, 5));

		assertEquals(expected, actual, "getNormal() did not return the expected normal vector");

		// TC02: Point on tube at height 0 (t = 0), expected normal is (0, 1, 0)
		Vector expectedNormal = new Vector(0, 1, 0);
		Vector actualNormal = tube.getNormal(new Point(0, 1, 0));
		assertTrue(expectedNormal.equals(actualNormal), "Expected normal (0,1,0) but got: " + actualNormal);

		// TC14: Point exactly on axis (0,0,5) → undefined, should throw
		assertThrows(IllegalArgumentException.class, () -> tube.getNormal(new Point(0, 0, 5)),
				"getNormal() should throw for point on axis (no defined normal)");
	}

	@Test
	void testFindIntersections() {
		// Not implemented
	}
}