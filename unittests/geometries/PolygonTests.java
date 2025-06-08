package geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.*;

/**
 * Testing Polygons
 * 
 * @author Dan
 */
class PolygonTests {
	/**
	 * Empty explicit default constructor to satisfy JavaDoc generator
	 */
	public PolygonTests() {
	}

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

		Polygon polygon = new Polygon(new Point(0, 0, 0), new Point(2, 0, 0), new Point(2, 2, 0), new Point(0, 2, 0));

		// ============ Equivalence Partitions Tests ==============

		// TC01: Intersection inside the polygon
		Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
		List<Point> result1 = polygon.findIntersections(ray1);
		assertNotNull(result1, "TC01: Expected intersection inside polygon");
		assertEquals(1, result1.size(), "TC01: One point expected");

		// TC02: Intersection outside polygon (against edge)
		Ray ray2 = new Ray(new Point(3, 1, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray2), "TC02: Should be outside – edge");

		// TC03: Intersection outside polygon (against vertex)
		Ray ray3 = new Ray(new Point(3, 3, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray3), "TC03: Should be outside – vertex");

		// =============== Boundary Values Tests ==================

		// TC11: Intersection on edge
		Ray ray4 = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray4), "TC11: On edge – not inside");

		// TC12: Intersection on vertex
		Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray5), "TC12: On vertex – not inside");

		// TC13: Intersection on edge extension
		Ray ray6 = new Ray(new Point(-1, 0, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray6), "TC13: On edge extension – not inside");

		// TC14: Ray starts exactly in the plane
		Ray ray7 = new Ray(new Point(1, 1, 0), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray7), "TC14: Starts in plane – no intersection");

		// TC15: Ray starts after the plane, away from it
		Ray ray8 = new Ray(new Point(1, 1, -1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray8), "TC15: Starts beyond plane – no intersection");
	}
}
