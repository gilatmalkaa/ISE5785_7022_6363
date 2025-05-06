/**
 * 
 */
package unittests.geometries;

import java.util.List;
import primitives.Ray;
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

	/**
	 * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
	 */
	@Test
	void testFindIntersections() {
		Sphere _sphere = new Sphere(new Point(1, 0, 0), 1d);

		// ============ Equivalence Partitions Tests ==============

		// **** Group 1: General rays intersecting or missing

		// TC01: Ray's line is outside the sphere (0 points)
		Ray _ray1 = new Ray(new Point(-1, 0, 0), new Vector(0, 1, 0));
		assertNull(_sphere.findIntersections(_ray1), "TC01: Ray's line is outside the sphere");

		// TC02: Ray starts before and crosses the sphere (2 points)
		Ray _ray2 = new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0));
		Point _gp1 = new Point(0.065, 0.355, 0);
		Point _gp2 = new Point(1.535, 0.845, 0);
		List<Point> _result2 = _sphere.findIntersections(_ray2);
		assertNotNull(_result2, "TC02: Expected two intersection points");
		assertEquals(2, _result2.size(), "TC02: Wrong number of points");

		// TC03: Ray starts inside the sphere (1 point)
		Ray _ray3 = new Ray(new Point(1, 0.5, 0), new Vector(0, 1, 0));
		List<Point> _result3 = _sphere.findIntersections(_ray3);
		assertNotNull(_result3, "TC03: Ray starts inside – should intersect once");
		assertEquals(1, _result3.size(), "TC03: Expected one point");

		// TC04: Ray starts after the sphere (0 points)
		Ray _ray4 = new Ray(new Point(3, 0, 0), new Vector(1, 0, 0));
		assertNull(_sphere.findIntersections(_ray4), "TC04: Ray starts after the sphere");

		// =============== Boundary Values Tests ==================

		// **** Group 2: Ray starts on surface

		// TC11: Ray starts at sphere and goes inside (1 point)
		Ray _ray5 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
		List<Point> _result5 = _sphere.findIntersections(_ray5);
		assertNotNull(_result5, "TC11: Expected one intersection from surface inward");
		assertEquals(1, _result5.size(), "TC11: Expected one point");

		// TC12: Ray starts at sphere and goes outside (0 points)
		Ray _ray6 = new Ray(new Point(0, 0, 0), new Vector(-1, 0, 0));
		assertNull(_sphere.findIntersections(_ray6), "TC12: Ray exits from surface");

		// **** Group 3: Ray goes through center

		// TC21: Ray goes through center, starts before (2 points)
		Ray _ray7 = new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0));
		List<Point> _result7 = _sphere.findIntersections(_ray7);
		assertNotNull(_result7, "TC21: Ray through center");
		assertEquals(2, _result7.size(), "TC21: Expected two points");

		// TC22: Ray through center, starts at surface (1 point)
		Ray _ray8 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
		List<Point> _result8 = _sphere.findIntersections(_ray8);
		assertNotNull(_result8, "TC22: Ray through center from surface");
		assertEquals(1, _result8.size(), "TC22: Expected one point");

		// TC23: Ray through center, starts inside (1 point)
		Ray _ray9 = new Ray(new Point(1, 0, 0.5), new Vector(0, 0, 1));
		List<Point> _result9 = _sphere.findIntersections(_ray9);
		assertNotNull(_result9, "TC23: Inside ray");
		assertEquals(1, _result9.size(), "TC23: Expected one point");

		// TC24: Ray through center, starts at center (1 point)
		Ray _ray10 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
		List<Point> _result10 = _sphere.findIntersections(_ray10);
		assertNotNull(_result10, "TC24: From center");
		assertEquals(1, _result10.size(), "TC24: Expected one point");

		// TC25: Ray through center, starts after (0 points)
		Ray _ray11 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
		assertNull(_sphere.findIntersections(_ray11), "TC25: Ray starts after sphere");

		// **** Group 4: Ray is tangent to the sphere (0 points)

		// TC31: Ray starts before the tangent point
		Ray _ray12 = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
		assertNull(_sphere.findIntersections(_ray12), "TC31: Tangent before");

		// TC32: Ray starts at the tangent point
		Ray _ray13 = new Ray(new Point(1, 1, 0), new Vector(1, 0, 0));
		assertNull(_sphere.findIntersections(_ray13), "TC32: Tangent at point");

		// TC33: Ray starts after the tangent point
		Ray _ray14 = new Ray(new Point(2, 1, 0), new Vector(1, 0, 0));
		assertNull(_sphere.findIntersections(_ray14), "TC33: Tangent after");

		// **** Group 5: Special cases

		// TC41: Ray orthogonal to line from center, misses sphere
		Ray _ray15 = new Ray(new Point(0, -2, 0), new Vector(1, 0, 0));
		assertNull(_sphere.findIntersections(_ray15), "TC41: Ray orthogonal and misses");
	}
}
