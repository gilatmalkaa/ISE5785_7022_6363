/**
 * 
 */
package unittests.geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Vector;
import geometries.Triangle;

/**
 * Unit tests for {@link geometries.Triangle} class.
 * Includes tests for getNormal(Point) according to stage 2 requirements.
 */
class TriangleTests {
	
	 private static final double DELTA = 1e-10;
	 
	 /**
	     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
	     * Verifies that the returned vector is normalized and orthogonal to the triangle.
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

	        // Length should be 1
	        assertEquals(1, flatNormal.length(), DELTA, "Flat triangle normal is not a unit vector");

	        // Expected direction is (0, 0, ±1)
	        assertTrue(
	            flatNormal.equals(new Vector(0, 0, 1)) || flatNormal.equals(new Vector(0, 0, -1)),
	            "Flat triangle normal direction is not as expected"
	        );
	 
	 

	/**
	 * Test method for {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
	 * We know that this test is not mandatory, since all the validation is handled in the Polygon class.
	 *  However, for the sake of clarity and code organization, we preferred to include it. 😊
	 */
	@Test
	void testTriangle() {
		// TC01: Sanity - triangle builds without exception
	    assertDoesNotThrow(() ->
	        new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0)),
	        "Triangle constructor failed on valid input"
	        );
	}

}
