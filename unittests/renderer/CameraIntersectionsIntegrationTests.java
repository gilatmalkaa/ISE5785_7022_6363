package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.Point;
import primitives.Vector;

/**
 * Integration tests for Camera ray construction and intersection with
 * geometries. This class checks that rays constructed through the view plane
 * intersect correctly with various geometric shapes.
 */
class CameraIntersectionsIntegrationTests {
	/**
	 * Empty explicit default constructor to satisfy JavaDoc generator
	 */
	public CameraIntersectionsIntegrationTests() {
	}

	/** Preconfigured camera builder for test cases */
	private final Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 0))
			.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpDistance(1).setVpSize(3, 3).build();

	/**
	 * Helper method to count intersections of all rays through the view plane with
	 * the given geometry.
	 *
	 * @param geometry the geometry to intersect
	 * @return total number of intersections
	 */
	private int countIntersections(Intersectable geometry) {
		int count = 0;
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				var intersections = geometry.findIntersections(camera.constructRay(3, 3, j, i));
				if (intersections != null)
					count += intersections.size();

			}
		}
		return count;
	}

	/**
	 * Tests intersection of rays with Sphere in various configurations.
	 */
	@Test
	void testSphereIntersections() {
		// TC01: Small sphere, 2 intersections (centered in front of camera)
		assertEquals(2, countIntersections(new Sphere(1, new Point(0, 0, -3))),
				"TC01: Expected 2 intersections with sphere");

		// TC02: Sphere encompassing all rays, 18 intersections
		assertEquals(18, countIntersections(new Sphere(2.5, new Point(0, 0, -3))),
				"TC02: Expected 18 intersections with large sphere");

		// TC03: Sphere partially within view, 10 intersections
		assertEquals(10, countIntersections(new Sphere(2, new Point(0, 0, -2.5))),
				"TC03: Expected 10 intersections with medium sphere");

		// TC04: Sphere enclosing the camera, 9 intersections (one per ray)
		assertEquals(9, countIntersections(new Sphere(4, new Point(0, 0, -1))),
				"TC04: Expected 9 intersections with very large sphere");

		// TC05: Sphere behind camera, 0 intersections
		assertEquals(0, countIntersections(new Sphere(0.5, new Point(0, 0, 1))),
				"TC05: Expected 0 intersections with sphere behind camera");
	}

	/**
	 * Tests intersection of rays with Plane in different orientations.
	 */
	@Test
	void testPlaneIntersections() {
		// TC01: Plane orthogonal to view direction, 9 intersections
		assertEquals(9, countIntersections(new Plane(new Point(0, 0, -1), new Vector(0, 0, -1))),
				"TC01: Expected 9 intersections with perpendicular plane");

		// TC02: Plane with small tilt, still intersecting all rays
		assertEquals(9, countIntersections(new Plane(new Point(0, 0, -1), new Vector(0, 1, -2))),
				"TC02: Expected 9 intersections with slightly angled plane");

		// TC03: Plane with steep angle, some rays miss (only 6 intersect)
		assertEquals(6, countIntersections(new Plane(new Point(0, 0, -1), new Vector(0, -1, -1))),
				"TC03: Expected 6 intersections with steeply angled plane");
	}

	/**
	 * Tests intersection of rays with Triangle in different configurations.
	 */
	@Test
	void testTriangleIntersections() {
		// TC01: Small triangle within center pixel, only one ray intersects
		assertEquals(1,
				countIntersections(new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2))),
				"TC01: Expected 1 intersection with small triangle");

		// TC02: Larger triangle covering center and adjacent pixels, 2 intersections
		assertEquals(2,
				countIntersections(new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2))),
				"TC02: Expected 2 intersections with larger triangle");
	}

}
