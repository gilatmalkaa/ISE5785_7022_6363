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
	 * The maximum recursion level for calculating color in reflections and
	 * refractions. Limits the depth of ray tracing to prevent infinite loops and
	 * manage performance.
	 */
	private static final int MAX_CALC_COLOR_LEVEL = 10;

	/**
	 * The minimal contribution factor for color calculations. Used to terminate
	 * recursion when the color contribution is negligible.
	 */
	private static final double MIN_CALC_COLOR_K = 0.001;

	/**
	 * The initial attenuation coefficient (K) for light calculations. Starts with
	 * full intensity (1.0 for all RGB channels).
	 */
	private static final Double3 INITIAL_K = Double3.ONE;
	/**
	 * Offset to avoid self-intersection.
	 */
	private static final double DELTA = 0.1;

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
		var closetIntersection = findClosestIntersection(ray);
		return closetIntersection == null ? scene.background : calcColor(closetIntersection, ray);
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
		return preprocessIntersection(intersection, ray.getDir())
				? calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(
						scene.ambientLight.getIntensity().scale(intersection.material.kA))
				: Color.BLACK;
	}

	/**
	 * Prepares necessary cached data in the intersection for lighting calculations.
	 *
	 * @param intersection the intersection to preprocess
	 * @param rayDirection the direction of the incoming ray
	 * @return {@code true} if intersection is valid and usable for lighting
	 */
	boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
		if (intersection == null || intersection.geometry == null)
			return false;

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
	 * Calculates the local lighting color at the intersection point.
	 * <p>
	 * This includes only the diffuse and specular components from all light sources
	 * that are not blocked by other geometries (i.e., unshaded). Each light source
	 * contributes to the color based on the Phong reflection model.
	 * </p>
	 *
	 * @param intersection the intersection point containing the geometry, normal,
	 *                     and material data
	 * @param k            the attenuation factor (used for recursive calculations,
	 *                     not directly used here)
	 * @return the color resulting from the local lighting effects at the
	 *         intersection point
	 */
	private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
		Color color = intersection.geometry.getEmission();
		for (LightSource lightSource : scene.lights) {
			if (setLightSource(intersection, lightSource)) {
				Double3 ktr = transparency(intersection);
				if (!(ktr.product(k).lowerThan(MIN_CALC_COLOR_K))) {
					Color il = lightSource.getIntensity(intersection.point).scale(ktr);
					Double3 d = calcDiffusive(intersection).add(calcSpecular(intersection));
					color = color.add(il.scale(d));
				}
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

	/**
	 * Checks if a point on a surface is unshaded (not blocked from the light
	 * source).
	 *
	 * @param intersection the intersection point with normal and light info
	 * @return true if no geometry blocks the light (point is lit), false otherwise
	 */

	private boolean unshaded(Intersection intersection) {
		Vector pointToLight = intersection.l.scale(-1);
		Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);
		double lightDistance = intersection.light.getDistance(intersection.point);

		var intersections = scene.geometries.findIntersections(shadowRay);
		if (intersections == null)
			return true;
		for (Point p : intersections) {
			double distancePoint = intersection.point.distance(p);
			if (distancePoint < lightDistance)
				return false;
		}
		return true;
	}

	/**
	 * Calculates the total color at the given intersection point, including both
	 * local lighting effects (diffuse and specular) and global effects (reflection
	 * and refraction), depending on the recursion level.
	 *
	 * @param intersection the intersection point containing geometry, normal, and
	 *                     material data
	 * @param level        the remaining recursion depth for calculating global
	 *                     effects
	 * @param k            the current cumulative attenuation factor for the color
	 *                     contribution
	 * @return the final color at the intersection point considering all lighting
	 *         effects
	 */
	private Color calcColor(Intersection intersection, int level, Double3 k) {
		Color color = calcColorLocalEffects(intersection, k);
		return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
	}

	/**
	 * Constructs a refracted ray from the intersection point.
	 *
	 * @param intersection the intersection point
	 * @return the refracted ray
	 */
	private Ray constructRefractedRay(Intersection intersection) {
		return new Ray(intersection.point, intersection.rayDirection, intersection.normal);
	}

	/**
	 * Constructs a reflected ray from the intersection point.
	 *
	 * @param intersection the intersection point
	 * @return the reflected ray
	 */
	private Ray constructReflectedRay(Intersection intersection) {
		return new Ray(intersection.point,
				intersection.rayDirection.subtract(intersection.normal.scale(2 * intersection.rayDirectionDotNormal)),
				intersection.normal);
	}

	/**
	 * Calculates the combined global lighting effects (reflection and refraction)
	 * at the given intersection point.
	 *
	 * @param intersection the intersection point from which the global effects
	 *                     originate
	 * @param level        the current recursion depth for global effect
	 *                     calculations
	 * @param k            the cumulative attenuation factor up to this point
	 * @return the combined color contribution from reflection and refraction
	 */
	private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
		return calcGlobalEffect(constructRefractedRay(intersection), level, k, intersection.material.kT)
				.add(calcGlobalEffect(constructReflectedRay(intersection), level, k, intersection.material.kR));
	}

	/**
	 * Calculates the global lighting effect (reflection or refraction) caused by a
	 * ray. If the effect is negligible (based on the attenuation coefficient),
	 * returns black. Otherwise, traces the ray and computes the resulting color
	 * contribution.
	 *
	 * @param ray   the reflected or refracted ray
	 * @param level the remaining recursion depth
	 * @param k     the current cumulative attenuation factor
	 * @param kx    the reflection or refraction coefficient of the geometry
	 * @return the color contribution of the global effect, scaled by kx
	 */
	private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
		Double3 kkx = k.product(kx);
		if (kkx.lowerThan(MIN_CALC_COLOR_K))
			return Color.BLACK;
		Intersection intersection = findClosestIntersection(ray);
		if (intersection == null)
			return scene.background.scale(kx);
		return preprocessIntersection(intersection, ray.getDir()) ? calcColor(intersection, level - 1, kkx).scale(kx)
				: Color.BLACK;
	}

	/**
	 * Finds the closest intersection point between a given ray and the scene's
	 * geometries.
	 *
	 * @param ray the ray to test for intersections
	 * @return the closest valid intersection, or {@code null} if none found
	 */
	private Intersection findClosestIntersection(Ray ray) {
		var intersection = scene.geometries.calculateIntersections(ray);
		return ray.findClosestIntersection(intersection);
	}

	/**
	 * Calculates the transparency factor (ktr) at a given intersection point.
	 * <p>
	 * This method traces a shadow ray from the intersection point toward the light
	 * source and checks if any geometry blocks the light. If a blocking geometry is
	 * found within the distance to the light source and its transparency (kT) is
	 * below a minimal threshold, the method returns {@code Double3.ZERO} to
	 * indicate full shadow (no transparency). Otherwise, it returns
	 * {@code Double3.ONE} to indicate full transparency (light reaches the point).
	 * </p>
	 *
	 * @param intersection the intersection point being checked for transparency
	 *                     toward the light source
	 * @return the transparency factor ({@link Double3#ZERO} if fully blocked,
	 *         {@link Double3#ONE} otherwise)
	 */
	private Double3 transparency(Intersection intersection) {
		Double3 ktr = Double3.ONE;
		Vector pointToLight = intersection.l.scale(-1);
		Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);
		double lightDistance = intersection.light.getDistance(intersection.point);

		var shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
		if (shadowIntersections == null)
			return ktr;

		for (Intersection shadowInter : shadowIntersections) {
			if (intersection.point.distance(shadowInter.point) < lightDistance) {
				if (shadowInter.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K))
					return Double3.ZERO;
			}
		}
		return ktr;
	}
}