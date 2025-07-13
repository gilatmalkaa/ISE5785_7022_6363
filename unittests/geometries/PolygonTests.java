package geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import primitives.*;

/**
 * Unit tests for the {@link Polygon} class. Tests include constructor
 * validation, normal vector calculation, and ray intersection scenarios (both
 * equivalence partitions and boundary cases).
 * 
 * @author Dan
 */
class PolygonTests {

	/**
	 * Explicit empty constructor for JavaDoc generation.
	 */
	public PolygonTests() {
	}

	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private static final double DELTA = 0.000001;

	/**
	 * Test method for {@link Polygon#Polygon(Point...)} constructor. Verifies
	 * proper construction and appropriate exceptions for invalid input.
	 */
	@Test
	void testConstructor() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Correct concave quadrangle with valid vertex order
		assertDoesNotThrow(
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)),
				"Failed constructing a correct polygon");

		// TC02: Wrong vertex order
		assertThrows(IllegalArgumentException.class,
				() -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)),
				"Constructed a polygon with wrong order of vertices");

		// TC03: Points not in same plane
		assertThrows(IllegalArgumentException.class,
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)),
				"Constructed a polygon with vertices not in the same plane");

		// TC04: Concave polygon (illegal)
		assertThrows(IllegalArgumentException.class, () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0),
				new Point(0, 1, 0), new Point(0.5, 0.25, 0.5)), "Constructed a concave polygon");

		// =============== Boundary Values Tests ==================

		// TC10: Vertex lies on an edge
		assertThrows(IllegalArgumentException.class,
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0.5, 0.5)),
				"Constructed a polygon with vertex on an edge");

		// TC11: Last point is identical to first
		assertThrows(IllegalArgumentException.class,
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
				"Constructed a polygon with duplicate start/end point");

		// TC12: Duplicate points
		assertThrows(IllegalArgumentException.class,
				() -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
				"Constructed a polygon with duplicate vertices");
	}

	/**
	 * Test method for {@link Polygon#getNormal(Point)}. Verifies the normal vector
	 * is unit length and orthogonal to all edges.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Regular quad polygon
		Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
		Polygon pol = new Polygon(pts);
		assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "No exception expected");

		Vector result = pol.getNormal(new Point(0, 0, 1));
		assertEquals(1, result.length(), DELTA, "Normal is not a unit vector");

		// Ensure normal is orthogonal to all edges
		for (int i = 0; i < 3; ++i) {
			Vector edge = pts[i].subtract(pts[i == 0 ? 3 : i - 1]);
			assertEquals(0d, result.dotProduct(edge), DELTA, "Normal is not orthogonal to one of the edges");
		}
	}

	/**
	 * Test method for {@link Polygon#findIntersections(Ray)}. Validates ray-polygon
	 * intersection behavior in various edge cases.
	 */
	@Test
	void testFindIntersections() {
		Polygon polygon = new Polygon(new Point(0, 0, 0), new Point(2, 0, 0), new Point(2, 2, 0), new Point(0, 2, 0));

		// ============ Equivalence Partitions Tests ==============

		// TC01: Ray intersects inside the polygon
		Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(0, 0, -1));
		List<Point> result1 = polygon.findIntersections(ray1);
		assertNotNull(result1, "TC01: Expected intersection inside polygon");
		assertEquals(List.of(new Point(1, 1, 0)), result1, "TC01: Incorrect intersection point inside polygon");

		// TC02: Ray intersects outside polygon (against edge)
		Ray ray2 = new Ray(new Point(3, 1, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray2), "TC02: Ray outside polygon (edge)");

		// TC03: Ray intersects outside polygon (against vertex)
		Ray ray3 = new Ray(new Point(3, 3, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray3), "TC03: Ray outside polygon (vertex)");

		// =============== Boundary Values Tests ==================

		// TC11: Ray intersects exactly on an edge
		Ray ray4 = new Ray(new Point(1, 0, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray4), "TC11: Intersection on edge");

		// TC12: Ray intersects exactly on a vertex
		Ray ray5 = new Ray(new Point(0, 0, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray5), "TC12: Intersection on vertex");

		// TC13: Ray intersects on edge extension
		Ray ray6 = new Ray(new Point(-1, 0, 1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray6), "TC13: Intersection on edge extension");

		// TC14: Ray starts in the polygon plane
		Ray ray7 = new Ray(new Point(1, 1, 0), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray7), "TC14: Ray starts in plane");

		// TC15: Ray starts beyond the plane and moves away
		Ray ray8 = new Ray(new Point(1, 1, -1), new Vector(0, 0, -1));
		assertNull(polygon.findIntersections(ray8), "TC15: Ray starts beyond plane");
	}
}
