package renderer;

import java.util.List;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

/**
 * Simple implementation of a ray tracer. In this stage, calculates basic color
 * using only ambient light, with no shading, reflection, or material effects.
 */
public class SimpleRayTracer extends RayTracerBase {

	/**
	 * Initializes the ray tracer with the given scene.
	 *
	 * @param scene the scene to be used for ray tracing
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);
	}

	/**
	 * Traces a ray through the scene and returns the color at the intersection
	 * point. If there is no intersection, returns the scene's background color.
	 *
	 * @param ray the ray to trace
	 * @return the computed color
	 */
	@Override
	public Color traceRay(Ray ray) {
		List<Point> intersections = scene.geometries.findIntersections(ray);

		if (intersections == null || intersections.isEmpty()) {
			return scene.background;
		}

		Point closestPoint = ray.findClosestPoint(intersections);
		return calcColor(closestPoint);
	}

	/**
	 * Calculates the color at a given point. Currently returns only the ambient
	 * light intensity of the scene.
	 *
	 * @param point the intersection point (not used in this stage)
	 * @return the ambient light color
	 */
	private Color calcColor(Point point) {
		return scene.ambientLight.getIntensity();
	}
}
