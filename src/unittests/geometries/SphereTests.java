/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Sphere} class.
 * 
 * @author Gilat Kedem and Shira Amar
 */
class SphereTests {

	/**
	 * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
	 * Current implementation returns null, so we check that explicitly.
	 */
	@Test
	void testGetNormal() {
		Sphere sphere = new Sphere(new Point(0, 0, 0), 1);

		// ============ Equivalence Partitions Tests ==============

		// TC01: A regular point on the surface → should return normalized vector
		assertEquals(new Vector(1, 0, 0), sphere.getNormal(new Point(1, 0, 0)),
				"getNormal() did not return the expected normal vector");

		// =============== Boundary Values Tests ==================

		// TC11: A point on the top of the sphere
		assertEquals(new Vector(0, 0, 1), sphere.getNormal(new Point(0, 0, 1)),
				"getNormal() did not return the expected normal on the axial point");
	}

	/**
	 * Test method for {@link geometries.Sphere#Sphere(primitives.Point, double)}.
	 * Ensures invalid radius values are rejected
	 */
	@Test
	void testSphere() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Valid sphere with positive radius
		assertDoesNotThrow(() -> new Sphere(new Point(1, 1, 1), 1), "Constructor failed on valid input");

		// =============== Boundary Values Tests ==================

		// TC11: Radius is 0 → should throw exception
		assertThrows(IllegalArgumentException.class, () -> new Sphere(new Point(1, 1, 1), 0),
				"Constructor should throw exception on radius = 0");

		// TC12: Radius is negative → should throw exception
		assertThrows(IllegalArgumentException.class, () -> new Sphere(new Point(1, 1, 1), -1),
				"Constructor should throw exception on negative radius");
	}

}
