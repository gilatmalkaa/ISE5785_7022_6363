package unittests.geometries;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.Polygon;
import primitives.*;

/**
 * Testing Polygons
 * 
 * @author Dan
 */
class PolygonTests {
	/**
	 * Delta value for accuracy when comparing the numbers of type 'double' in
	 * assertEquals
	 */
	private static final double DELTA = 0.000001;

	/** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
	@Test
	void testConstructor() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct concave quadrangular with vertices in correct order
		assertDoesNotThrow(
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)),
				"Failed constructing a correct polygon");

		// TC02: Wrong vertices order
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
				"Constructed a polygon with wrong order of vertices");

		// TC03: Not in the same plane
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
				"Constructed a polygon with vertices that are not in the same plane");

		// TC04: Concave quadrangular
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
						new Point(0.5, 0.25, 0.5)), //
				"Constructed a concave polygon");

		// =============== Boundary Values Tests ==================

		// TC10: Vertex on a side of a quadrangular
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0.5, 0.5)),
				"Constructed a polygon with vertix on a side");

		// TC11: Last point = first point
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
				"Constructed a polygon with vertice on a side");

		// TC12: Co-located points
		assertThrows(IllegalArgumentException.class, //
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
				"Constructed a polygon with vertice on a side");

	}

	/** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: There is a simple single test here - using a quad
		Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
		Polygon pol = new Polygon(pts);
		// ensure there are no exceptions
		assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
		// generate the test result
		Vector result = pol.getNormal(new Point(0, 0, 1));
		// ensure |result| = 1
		assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
		// ensure the result is orthogonal to all the edges
		for (int i = 0; i < 3; ++i)
			assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
					"Polygon's normal is not orthogonal to one of the edges");
	}

	/**
	 * Tests {@link geometries.Plane#findIntersections(Ray)}. Includes EP and BVA:
	 * intersection, no intersection, parallel/orthogonal rays.
	 */
	@Test
	void testFindIntersections() {
		Polygon _polygon = new Polygon(new Point(0, 0, 0), new Point(2, 0, 0), new Point(2, 2, 0), new Point(0, 2, 0));

		// ============ Equivalence Partitions Tests ==============

		// **** Group 1: General rays intersecting the polygon

		// TC01: Ray intersects inside the polygon
		Ray _ray1 = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
		List<Point> _result1 = _polygon.findIntersections(_ray1);
		assertNotNull(_result1, "TC01: Ray should intersect inside the polygon");
		assertEquals(1, _result1.size(), "TC01: Expected one intersection point");

		// TC02: Ray intersects plane outside the polygon (edge continuation)
		Ray _ray2 = new Ray(new Point(3, 1, 1), new Vector(0, 0, -1));
		assertNull(_polygon.findIntersections(_ray2), "TC02: Ray intersects plane but outside polygon");

		// =============== Boundary Values Tests ==================

		// **** Group 2: Ray hits edge or vertex or outside extension

		// TC11: Ray intersects exactly on edge of polygon
		Ray _ray3 = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
		assertNull(_polygon.findIntersections(_ray3), "TC11: Ray intersects exactly on edge – should not count");

		// TC12: Ray intersects exactly on vertex of polygon
		Ray _ray4 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
		assertNull(_polygon.findIntersections(_ray4), "TC12: Ray intersects exactly on vertex – should not count");

		// TC13: Ray intersects on edge extension (outside polygon)
		Ray _ray5 = new Ray(new Point(-1, 0, 1), new Vector(0, 0, -1));
		assertNull(_polygon.findIntersections(_ray5), "TC13: Ray intersects plane beyond polygon – edge extension");

		// **** Group 3: Ray starts in plane or after plane

		// TC14: Ray is orthogonal and starts in the plane
		Ray _ray6 = new Ray(new Point(1, 1, 0), new Vector(0, 0, -1));
		assertNull(_polygon.findIntersections(_ray6), "TC14: Ray starts in the plane – no intersection");

		// TC15: Ray is orthogonal and starts after the plane
		Ray _ray7 = new Ray(new Point(1, 1, -1), new Vector(0, 0, -1));
		assertNull(_polygon.findIntersections(_ray7), "TC15: Ray goes away from polygon – no intersection");
	}
}
