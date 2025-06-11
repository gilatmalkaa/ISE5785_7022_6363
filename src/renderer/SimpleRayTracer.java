package renderer;

import java.util.List;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

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

		// Start with emission color
		Color color = intersection.geometry.getEmission();

		// Add ambient light component
		color = color.add(scene.ambientLight.getIntensity().scale(intersection.material.kA));

		// Add local effects from all light sources
		color = color.add(calcColorLocalEffects(intersection));

		return color;
	}

	/**
	 * Prepares and caches necessary values at the intersection for lighting
	 * calculations.
	 *
	 * @param intersection the intersection to preprocess
	 * @param rayDirection the direction of the incoming ray
	 * @return {@code true} if the intersection is valid and usable for shading,
	 *         otherwise {@code false}
	 */
	boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
		if (intersection == null || intersection.geometry == null) {
			return false;
		}

		// Cache ray direction
		intersection.cacheRayDirection = rayDirection;

		// Calculate and cache normal vector
		intersection.cacheNormal = intersection.geometry.getNormal(intersection.point);

		// Calculate dot product of normal and ray direction
		intersection.cacheRayDirectionDotCacheNormal = intersection.cacheNormal.dotProduct(rayDirection);

		// Check if ray and normal are perpendicular (if dot product is zero)
		return !Util.isZero(intersection.cacheRayDirectionDotCacheNormal);
	}

	/**
	 * Sets the current light source direction and its relation to the surface
	 * normal for shading.
	 *
	 * @param intersection the intersection point being shaded
	 * @param light        the light source to use for lighting calculations
	 * @return {@code true} if the light affects the surface (not perpendicular),
	 *         otherwise {@code false}
	 */
	private boolean setLightSource(Intersection intersection, LightSource light) {
		// Set the current light source being processed
		intersection.cacheLightSource = light;

		// Get light direction
		intersection.cacheLightDirection = light.getL(intersection.point);

		// Calculate dot product with normal
		intersection.cacheLightSourceDirectionDotCacheNormal = intersection.cacheNormal
				.dotProduct(intersection.cacheLightDirection);

		// Check if light and normal are perpendicular (if dot product is zero)
		return !Util.isZero(intersection.cacheLightSourceDirectionDotCacheNormal);
	}

	/**
	 * Calculates the local lighting effects (diffuse and specular) from all light
	 * sources.
	 *
	 * @param intersection the intersection point where lighting is computed
	 * @return the resulting color from local lighting effects
	 */
	private Color calcColorLocalEffects(Intersection intersection) {
		Color color = Color.BLACK; // Start with black color

		// Process each light source
		for (LightSource light : scene.lights) {
			if (!setLightSource(intersection, light)) {
				continue; // Skip this light source if it doesn't affect this point
			}

			// Calculate diffuse and specular components
			Double3 diffuse = calcDiffusive(intersection);
			Double3 specular = calcSpecular(intersection);

			if (Util.compareSign(intersection.cacheLightSourceDirectionDotCacheNormal,
					intersection.cacheRayDirectionDotCacheNormal)) {
				// Get light intensity at this point
				Color lightIntensity = light.getIntensity(intersection.point);

				// Add the light's contribution with both components
				color = color.add(lightIntensity.scale(diffuse.add(specular)));
			}
		}

		return color;
	}

	/**
	 * Calculates the specular component of the Phong reflection model at the
	 * intersection point.
	 *
	 * @param intersection the intersection point where specular reflection is
	 *                     computed
	 * @return the specular intensity as a {@code Double3}
	 */
	private Double3 calcSpecular(Intersection intersection) {
		// Calculate the product to check if the viewer and light are on the same side
		double product = intersection.cacheLightSourceDirectionDotCacheNormal
				* intersection.cacheRayDirectionDotCacheNormal;

		// If the viewer and light are on opposite sides, no specular reflection
		if (product <= 0) {
			return Double3.ZERO;
		}

		// Calculate reflection vector r = l - 2(n·l)n
		Vector n = intersection.cacheNormal;
		Vector l = intersection.cacheLightDirection;
		double nl = intersection.cacheLightSourceDirectionDotCacheNormal;
		Vector r = l.subtract(n.scale(2 * nl));

		// Calculate v·r (dot product of view direction and reflection vector)
		// The viewer direction is the opposite of ray direction
		Vector v = intersection.cacheRayDirection.scale(-1);
		double vr = v.dotProduct(r);

		// If v·r <= 0, no specular reflection
		if (vr <= 0) {
			return Double3.ZERO;
		}

		// kS * (v·r)^nShininess
		return intersection.material.kS.scale(Math.pow(vr, intersection.material.nShininess));
	}

	/**
	 * Calculates the diffuse component of the Phong reflection model at the
	 * intersection point.
	 *
	 * @param intersection the intersection point where diffuse reflection is
	 *                     computed
	 * @return the diffuse intensity as a {@code Double3}
	 */
	private Double3 calcDiffusive(Intersection intersection) {
		// kD * |nl|
		return intersection.material.kD.scale(Math.abs(intersection.cacheLightSourceDirectionDotCacheNormal));
	}

}