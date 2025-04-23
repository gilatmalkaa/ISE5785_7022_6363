package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Tube;
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
	 * Accuracy tolerance used for comparing floating-point values in tests. This
	 * small value accounts for rounding errors when comparing doubles.
	 */
	private static final double DELTA = 1e-10;

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
	}
}