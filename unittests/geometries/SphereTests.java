/**
 * 
 */
package geometries;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
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

	/** A point used in some tests */
	private final Point _p001 = new Point(0, 0, 1);
	/** A point used in some tests */
	private final Point _p100 = new Point(1, 0, 0);
	/** A vector used in some tests */
	private final Vector _v001 = new Vector(0, 0, 1);

	@Test
	void testGetNormal() {
		Sphere sphere = new Sphere(new Point(0, 0, 0), 1);

		// ============ Equivalence Partitions Tests ==============

		// TC01: A regular point on the surface → should return normalized vector
		assertEquals(new Vector(1, 0, 0), sphere.getNormal(_p100),
				"getNormal() did not return the expected normal vector");

		// =============== Boundary Values Tests ==================

		// TC11: A point on the top of the sphere
		assertEquals(_v001, sphere.getNormal(_p001),
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
		final Sphere sphere = new Sphere(_p100, 1d);
		final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
		final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
		final var exp = List.of(gp1, gp2);
		final Vector v310 = new Vector(3, 1, 0);
		final Vector v110 = new Vector(1, 1, 0);
		final Point p01 = new Point(-1, 0, 0);

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray's line is outside the sphere (0 points)
		Ray ray1 = new Ray(p01, v110);
		assertNull(sphere.findIntersections(ray1), "TC01: Ray's line is outside the sphere");

		// TC02: Ray starts before and crosses the sphere (2 points)
		Ray ray2 = new Ray(p01, v310);
		List<Point> result2 = sphere.findIntersections(ray2);
		assertEquals(exp, result2, "TC02: Ray crosses sphere");

		// TC03: Ray starts inside the sphere (1 point)
		Ray ray3 = new Ray(_p100, _v001); // Starting inside the sphere
		List<Point> result3 = sphere.findIntersections(ray3);
		assertNotNull(result3, "TC03: Ray starts inside – should intersect once");
		assertEquals(1, result3.size(), "TC03: Expected one point");

		// TC04: Ray starts after the sphere (0 points)
		Ray ray4 = new Ray(new Point(3, 0, 0), new Vector(1, 0, 0));
		assertNull(sphere.findIntersections(ray4), "TC04: Ray starts after the sphere");

		// =============== Boundary Values Tests ==================

		// **** Group 1: "Ray starts on the surface of the sphere: one enters, one
		// exits"

		// TC11: Ray starts at sphere and goes inside (1 point)
		Ray ray5 = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
		List<Point> result5 = sphere.findIntersections(ray5);
		assertNotNull(result5, "TC11: Expected one intersection from surface inward");
		assertEquals(1, result5.size(), "TC11: Expected one point");

		// TC12: Ray starts at sphere and goes outside (0 points)
		Ray ray6 = new Ray(new Point(0, 0, 0), new Vector(-1, 0, 0)); // Exiting the surface
		assertNull(sphere.findIntersections(ray6), "TC12: Ray exits from surface");

		// **** Group 2: Ray goes through center

		// TC21: Ray goes through center, starts before (2 points)
		Ray ray7 = new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0)); // Passing through the center
		List<Point> result7 = sphere.findIntersections(ray7);
		assertNotNull(result7, "TC21: Ray through center");
		assertEquals(2, result7.size(), "TC21: Expected two points");

		// TC22: Ray through center, starts at surface (1 point)
		Ray ray8 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)); // At the surface, heading toward the center
		List<Point> result8 = sphere.findIntersections(ray8);
		assertNotNull(result8, "TC22: Ray through center from surface");
		assertEquals(1, result8.size(), "TC22: Expected one point");

		// TC23: Ray through center, starts inside (1 point)
		Ray ray9 = new Ray(_p100, _v001); // Starting inside the sphere
		List<Point> result9 = sphere.findIntersections(ray9);
		assertNotNull(result9, "TC23: Inside ray");
		assertEquals(1, result9.size(), "TC23: Expected one point");

		// TC24: Ray through center, starts at center (1 point)
		Ray ray10 = new Ray(_p100, _v001); // Starting at the center
		List<Point> result10 = sphere.findIntersections(ray10);
		assertNotNull(result10, "TC24: From center");
		assertEquals(1, result10.size(), "TC24: Expected one point");

		// TC25: Ray through center, starts after (0 points)
		Ray ray11 = new Ray(new Point(2, 0, 0), _v001); // Starting after the sphere
		assertNull(sphere.findIntersections(ray11), "TC25: Ray starts after sphere");

		// TC26: Ray through center, starts after sphere and goes backward (1 point)
		Ray rayBackward = new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0)); // Going backward through the center
		List<Point> resultBackward = sphere.findIntersections(rayBackward);
		assertNotNull(resultBackward, "TC26: Ray goes backward through center");
		assertEquals(1, resultBackward.size(), "TC26: Expected one point");

		// **** Group 3: Ray is tangent to the sphere (0 points)

		// TC31: Ray starts before the tangent point
		Ray ray12 = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
		assertNull(sphere.findIntersections(ray12), "TC31: Tangent before");

		// TC32: Ray starts at the tangent point
		Ray ray13 = new Ray(new Point(1, 1, 0), new Vector(1, 0, 0));
		assertNull(sphere.findIntersections(ray13), "TC32: Tangent at point");

		// TC33: Ray starts after the tangent point
		Ray ray14 = new Ray(new Point(2, 1, 0), new Vector(1, 0, 0));
		assertNull(sphere.findIntersections(ray14), "TC33: Tangent after");

		// **** Group 4: Special cases

		// TC41: Ray orthogonal to line from center, misses sphere
		Ray ray15 = new Ray(new Point(0, -2, 0), new Vector(1, 0, 0));
		assertNull(sphere.findIntersections(ray15), "TC41: Ray orthogonal and misses");

		// TC43: Ray orthogonal to line from center, starts inside sphere
		Ray rayInsideOrthogonal = new Ray(new Point(0.5, 0, 0), new Vector(0, 1, 0));
		List<Point> resultInsideOrthogonal = sphere.findIntersections(rayInsideOrthogonal);
		assertNotNull(resultInsideOrthogonal, "TC43: Ray orthogonal, starts inside – expect 1 intersection");
		assertEquals(1, resultInsideOrthogonal.size(), "TC43: Expected one point");
	}
}
