package renderer;

import static primitives.Util.alignZero;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

/**
 * A simple implementation of a ray tracer that calculates color at a given
 * intersection point using basic Phong reflection model with ambient, diffuse,
 * and specular components.
 */
public class SimpleRayTracer extends RayTracerBase {

	/**
	 * Constructs a SimpleRayTracer with the given scene.
	 *
	 * @param scene the scene to render with this ray tracer
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);
	}

	@Override
	public Color traceRay(Ray ray) {
		var intersections = scene.geometries.calculateIntersections(ray);
		if (intersections == null)
			return scene.background;

		var closest = ray.findClosestIntersection(intersections);
		return calcColor(closest, ray);
	}

	/**
	 * Calculates the color at a given intersection point based on lighting and
	 * material.
	 *
	 * @param intersection the intersection point in the scene
	 * @param ray          the ray that caused the intersection
	 * @return the calculated color at the intersection
	 */
	private Color calcColor(Intersection intersection, Ray ray) {
		if (!preprocessIntersection(intersection, ray.getDir()))
			return Color.BLACK;

		Color color = intersection.geometry.getEmission();
		color = color.add(scene.ambientLight.getIntensity().scale(intersection.material.kA));
		color = color.add(calcColorLocalEffects(intersection));

		return color;
	}

	/**
	 * Prepares necessary cached data in the intersection for lighting calculations.
	 *
	 * @param intersection the intersection to preprocess
	 * @param rayDirection the direction of the incoming ray
	 * @return {@code true} if intersection is valid and usable for lighting
	 */
	boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
		intersection.rayDirection = rayDirection;
		intersection.normal = intersection.geometry.getNormal(intersection.point);
		intersection.rayDirectionDotNormal = alignZero(intersection.normal.dotProduct(rayDirection));
		return intersection.rayDirectionDotNormal != 0;
	}

	/**
	 * Sets the light source data on the intersection for shading.
	 *
	 * @param intersection the intersection being shaded
	 * @param light        the light source used
	 * @return {@code true} if the light affects the surface
	 */
	private boolean setLightSource(Intersection intersection, LightSource light) {
		intersection.light = light;
		intersection.l = light.getL(intersection.point);
		intersection.pointToLight = intersection.l.scale(-1);
		intersection.lDotNormal = alignZero(intersection.normal.dotProduct(intersection.l));
		return intersection.lDotNormal * intersection.rayDirectionDotNormal > 0;
	}

	/**
	 * Calculates the local lighting effects from all light sources including
	 * diffuse and specular components.
	 *
	 * @param intersection the intersection to shade
	 * @return the resulting color from local lighting
	 */
	private Color calcColorLocalEffects(Intersection intersection) {
		Color color = Color.BLACK;

		for (LightSource light : scene.lights) {
			if (!setLightSource(intersection, light))
				continue;

			Double3 diffuse = calcDiffusive(intersection);
			Double3 specular = calcSpecular(intersection);
			Color lightIntensity = light.getIntensity(intersection.point);
			color = color.add(lightIntensity.scale(diffuse.add(specular)));
		}

		return color;
	}

	/**
	 * Calculates the specular component of the Phong reflection model.
	 *
	 * @param intersection the intersection where specular is computed
	 * @return specular intensity as {@link Double3}
	 */
	private Double3 calcSpecular(Intersection intersection) {
		Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.lDotNormal));
		double minusVR = -intersection.rayDirection.dotProduct(r);
		return minusVR <= 0 ? Double3.ZERO
				: intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nShininess));
	}

	/**
	 * Calculates the diffuse component of the Phong reflection model.
	 *
	 * @param intersection the intersection where diffuse is computed
	 * @return diffuse intensity as {@link Double3}
	 */
	private Double3 calcDiffusive(Intersection intersection) {
		return intersection.material.kD.scale(Math.abs(intersection.lDotNormal));
	}
}