/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import primitives.Point;

/**
 *  Unit tests for {@link geometries.Sphere} class.
 *  @author Gilat Kedem and Shira Amar
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

        // TC01: A regular point on the surface → should return null (current stub)
        assertNull(
            sphere.getNormal(new Point(1, 0, 0)),
            "getNormal() for sphere should return null (stub)"
        );

        // =============== Boundary Values Tests ==================

        // TC11: Edge point on axis (e.g. top of sphere)
        assertNull(
            sphere.getNormal(new Point(0, 0, 1)),
            "getNormal() on axial point should return null (stub)"
	);
	}

	/**
	 * Test method for {@link geometries.Sphere#Sphere(primitives.Point, double)}.
	 * Ensures invalid radius values are rejected
	 */
	@Test
	void testSphere() {
		 // ============ Equivalence Partitions Tests ==============

	    // TC01: Valid sphere with positive radius
	    assertDoesNotThrow(() ->
	        new Sphere(new Point(1, 1, 1), 1),
	        "Constructor failed on valid input");

	    // =============== Boundary Values Tests ==================

	    // TC11: Radius is 0 → should throw exception
	    assertThrows(IllegalArgumentException.class,
	        () -> new Sphere(new Point(1, 1, 1), 0),
	        "Constructor should throw exception on radius = 0");

	    // TC12: Radius is negative → should throw exception
	    assertThrows(IllegalArgumentException.class,
	        () -> new Sphere(new Point(1, 1, 1), -1),
	        "Constructor should throw exception on negative radius");
	}

}
