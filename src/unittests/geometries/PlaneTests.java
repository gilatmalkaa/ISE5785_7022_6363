/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import primitives.Point;
import primitives.Vector;

/**
 * Unit tests for {@link geometries.Plane} class.
 * @author Gilat Kedem and Shira Amar
 */
class PlaneTests {

	private static final double DELTA = 1e-10;
	
	/**
	 * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
	 * Ensures the returned vector is normalized and orthogonal to the plane.
	 */
	@Test
	void testGetNormal() {
		  // ============ Equivalence Partitions Tests ==============

        // TC01: Regular plane defined by 3 points in 3D space
        Plane plane = new Plane(
            new Point(0, 0, 1),
            new Point(1, 0, 0),
            new Point(0, 1, 0)
        );

        Vector normal = plane.getNormal(new Point(0, 0, 1));

        // Normal must be unit vector (length = 1)
        assertEquals(1, normal.length(), DELTA, "Plane normal is not a unit vector");

        // Check orthogonality to two vectors in the plane
        Vector v1 = new Point(1, 0, 0).subtract(new Point(0, 0, 1));
        Vector v2 = new Point(0, 1, 0).subtract(new Point(0, 0, 1));

        assertEquals(0, normal.dotProduct(v1), DELTA, "Normal is not orthogonal to v1");
        assertEquals(0, normal.dotProduct(v2), DELTA, "Normal is not orthogonal to v2");

        // =============== Boundary Values Tests ==================

        // TC11: Plane defined with horizontal points
        Plane flatPlane = new Plane(
            new Point(0, 0, 0),
            new Point(1, 0, 0),
            new Point(0, 1, 0)
        );

        Vector flatNormal = flatPlane.getNormal(new Point(0, 0, 0));

        // Should be (0, 0, 1) or (0, 0, -1) – both valid
        assertEquals(1, flatNormal.length(), DELTA, "Flat plane normal is not a unit vector");
        assertTrue(
            flatNormal.equals(new Vector(0, 0, 1)) || flatNormal.equals(new Vector(0, 0, -1)),
            "Flat plane normal not in expected direction"
        );
	}

	/**
	 * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Vector)}.
	 * 
	 */
	@Test
	void testPlanePointVector() {
		 // TC01: Regular valid input → should succeed
	    assertDoesNotThrow(() ->
	        new Plane(new Point(1, 2, 3), new Vector(0, 0, 1)),
	        "Constructor failed on valid input");

	    // TC11: Normal vector is zero vector → should throw exception
	    assertThrows(IllegalArgumentException.class,
	        () -> new Plane(new Point(1, 2, 3), new Vector(0, 0, 0)),
	        "Constructor should throw for zero normal vector");
	}

	/**
	 * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
	 * With three points. Includes EP test and 5 BVA tests
	 */
	@Test
	void testPlanePointPointPoint() {
		   // ============ Equivalence Partitions Tests ==============

	    // TC01: Three non-colinear points → should create valid plane
	    Point p1 = new Point(0, 0, 1);
	    Point p2 = new Point(1, 0, 0);
	    Point p3 = new Point(0, 1, 0);

	    Plane plane = new Plane(p1, p2, p3);
	    Vector normal = plane.getNormal(p1);

	    // Verify normal is unit vector
	    assertEquals(1, normal.length(), DELTA, "Normal is not a unit vector");

	    // Verify normal is orthogonal to two vectors in the plane
	    Vector v1 = p2.subtract(p1);
	    Vector v2 = p3.subtract(p1);
	    assertEquals(0, normal.dotProduct(v1), DELTA, "Normal is not orthogonal to v1");
	    assertEquals(0, normal.dotProduct(v2), DELTA, "Normal is not orthogonal to v2");

	    // =============== Boundary Values Tests ==================

	    // TC11: First and second points are the same
	    assertThrows(IllegalArgumentException.class,
	        () -> new Plane(p1, p1, p3),
	        "Constructor should throw when first and second points are the same");

	    // TC12: First and third points are the same
	    assertThrows(IllegalArgumentException.class,
	        () -> new Plane(p1, p2, p1),
	        "Constructor should throw when first and third points are the same");

	    // TC13: Second and third points are the same
	    assertThrows(IllegalArgumentException.class,
	        () -> new Plane(p1, p2, p2),
	        "Constructor should throw when second and third points are the same");

	    // TC14: All three points are the same
	    assertThrows(IllegalArgumentException.class,
	        () -> new Plane(p1, p1, p1),
	        "Constructor should throw when all points are the same");

	    // TC15: All points on the same line (colinear)
	    Point q1 = new Point(0, 0, 0);
	    Point q2 = new Point(1, 1, 1);
	    Point q3 = new Point(2, 2, 2); // On the same line as q1-q2

	    assertThrows(IllegalArgumentException.class,
	        () -> new Plane(q1, q2, q3),
	        "Constructor should throw when all points are colinear");
	}

}
