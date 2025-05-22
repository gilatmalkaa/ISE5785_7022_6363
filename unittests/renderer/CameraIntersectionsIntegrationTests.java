package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Integration tests between camera rays and geometric shapes. Tests are based
 * on ray construction from camera and their intersections with Sphere, Plane,
 * and Triangle. Each test checks the number of intersections between the rays
 * and the given shape.
 */
class CameraIntersectionsIntegrationTests {

	/**
	 * Helper method to count the total number of intersection points between camera
	 * rays and a geometry.
	 *
	 * @param camera      the camera creating the rays
	 * @param geometry    the geometry to intersect with
	 * @param expected    the expected number of intersection points
	 * @param description description to identify the test case
	 */
	private void assertCountIntersections(Camera camera, Intersectable geometry, int expected, String description) {
		int nX = 3;
		int nY = 3;
		int count = 0;

		for (int i = 0; i < nY; i++) {
			for (int j = 0; j < nX; j++) {
				Ray ray = camera.constructRay(nX, nY, j, i);
				List<Point> intersections = geometry.findIntersections(ray);
				if (intersections != null) {
					count += intersections.size();
				}
			}
		}

		assertEquals(expected, count, "Wrong number of intersections in test: " + description);
	}

	/**
	 * Integration test of camera rays with a sphere. Verifies that multiple rays
	 * intersect with a sphere at different positions and configurations.
	 */
	@Test
	void testCameraRaySphereIntersections() {
		Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 0.5))
				.setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1).setVpSize(3, 3)
				.setResolution(3, 3).build();

		Sphere sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
		assertCountIntersections(camera, sphere, 18, "Camera-Sphere integration");
	}

	/**
	 * Integration test of camera rays with a plane. Verifies that all rays
	 * intersect a plane located in front of the camera.
	 */
	@Test
	void testCameraRayPlaneIntersections() {
		Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 0))
				.setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1).setVpSize(3, 3)
				.setResolution(3, 3).build();

		Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
		assertCountIntersections(camera, plane, 9, "Camera-Plane integration");
	}

	/**
	 * Integration test of camera rays with a triangle. Verifies that only the
	 * center ray intersects a small triangle.
	 */
	@Test
	void testCameraRayTriangleIntersections() {
		Camera camera = Camera.getBuilder().setLocation(new Point(0, 0, 0))
				.setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(1).setVpSize(3, 3)
				.setResolution(3, 3).build();

		Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));

		assertCountIntersections(camera, triangle, 1, "Camera-Triangle integration");
	}
}
