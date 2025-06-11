package renderer;

import java.util.List;

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
		List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
		if (intersections == null)
			return scene.background;

		Intersection closest = ray.findClosestIntersection(intersections);
		if (closest == null)
			return scene.background;

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
		if (!preprocessIntersection(intersection, ray.getDir())) {
			return Color.BLACK;
		}

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
		if (intersection == null || intersection.geometry == null) {
			return false;
		}

		intersection.cacheRayDirection = rayDirection;
		intersection.cacheNormal = intersection.geometry.getNormal(intersection.point);
		intersection.cacheRayDirectionDotCacheNormal = intersection.cacheNormal.dotProduct(rayDirection);

		return !Util.isZero(intersection.cacheRayDirectionDotCacheNormal);
	}

	/**
	 * Sets the light source data on the intersection for shading.
	 *
	 * @param intersection the intersection being shaded
	 * @param light        the light source used
	 * @return {@code true} if the light affects the surface
	 */
	private boolean setLightSource(Intersection intersection, LightSource light) {
		intersection.cacheLightSource = light;
		intersection.cacheLightDirection = light.getL(intersection.point);
		intersection.cacheLightSourceDirectionDotCacheNormal = intersection.cacheNormal
				.dotProduct(intersection.cacheLightDirection);

		return !Util.isZero(intersection.cacheLightSourceDirectionDotCacheNormal);
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
			if (!setLightSource(intersection, light)) {
				continue;
			}

			Double3 diffuse = calcDiffusive(intersection);
			Double3 specular = calcSpecular(intersection);

			if (Util.compareSign(intersection.cacheLightSourceDirectionDotCacheNormal,
					intersection.cacheRayDirectionDotCacheNormal)) {
				Color lightIntensity = light.getIntensity(intersection.point);
				color = color.add(lightIntensity.scale(diffuse.add(specular)));
			}
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
		double product = intersection.cacheLightSourceDirectionDotCacheNormal
				* intersection.cacheRayDirectionDotCacheNormal;

		if (product <= 0)
			return Double3.ZERO;

		Vector n = intersection.cacheNormal;
		Vector l = intersection.cacheLightDirection;
		double nl = intersection.cacheLightSourceDirectionDotCacheNormal;
		Vector r = l.subtract(n.scale(2 * nl));

		Vector v = intersection.cacheRayDirection.scale(-1);
		double vr = v.dotProduct(r);
		if (vr <= 0)
			return Double3.ZERO;

		return intersection.material.kS.scale(Math.pow(vr, intersection.material.nShininess));
	}

	/**
	 * Calculates the diffuse component of the Phong reflection model.
	 *
	 * @param intersection the intersection where diffuse is computed
	 * @return diffuse intensity as {@link Double3}
	 */
	private Double3 calcDiffusive(Intersection intersection) {
		return intersection.material.kD.scale(Math.abs(intersection.cacheLightSourceDirectionDotCacheNormal));
	}
}