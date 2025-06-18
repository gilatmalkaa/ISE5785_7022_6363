package geometries;

import java.util.List;

import lighting.LightSource;
import primitives.*;

/**
 * Abstract base class for all objects in the scene that can be intersected by
 * rays.
 */
public abstract class Intersectable {
	/**
	 * Explicit default constructor to satisfy JavaDoc generator.
	 */
	public Intersectable() {
	}

	/**
	 * Passive data structure representing a single intersection point between a ray
	 * and a geometry.
	 */
	public static class Intersection {
		/**
		 * The point of intersection.
		 */
		public final Point point;
		/**
		 * The geometry object that was intersected.
		 */
		public final Geometry geometry;
		/**
		 * The material of the intersected geometry.
		 */
		public final Material material;

		/**
		 * Cached direction of the ray for shading calculations.
		 */
		public Vector rayDirection;
		/**
		 * Cached normal vector at the point of intersection.
		 */
		public Vector normal;
		/**
		 * Dot product of the ray direction and the normal vector.
		 */
		public double rayDirectionDotNormal;

		/**
		 * The light source associated with the intersection.
		 */
		public LightSource light;
		/**
		 * Cached direction from the light source to the point.
		 */
		public Vector l;
		/**
		 * Cached direction from the point to the light source.
		 */
		public Vector pointToLight;
		/**
		 * Dot product of the light direction and the normal vector.
		 */
		public double lDotNormal;

		/**
		 * Constructs an Intersection with the given geometry and point.
		 *
		 * @param geometry the intersected geometry
		 * @param point    the point of intersection
		 */
		public Intersection(Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
			this.material = geometry != null ? geometry.getMaterial() : null;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			return obj instanceof Intersection other && geometry == other.geometry && point.equals(other.point);
		}

		@Override
		public String toString() {
			return "Intersection{" + "geometry=" + geometry + ", point=" + point + '}';
		}
	}

	/**
	 * Helper method that calculates intersections of a ray with the geometry.
	 * Subclasses must implement this method.
	 *
	 * @param ray the ray to intersect with
	 * @return list of {@link Intersection} objects, or {@code null} if none
	 */
	protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

	/**
	 * Calculates intersections of a ray with the geometry. Delegates to
	 * {@link #calculateIntersectionsHelper(Ray)}.
	 *
	 * @param ray the ray to intersect with
	 * @return list of {@link Intersection} objects, or {@code null} if none
	 */
	public final List<Intersection> calculateIntersections(Ray ray) {
		return calculateIntersectionsHelper(ray);
	}

	/**
	 * Finds the intersection points (without geometry metadata) of a ray with the
	 * geometry.
	 *
	 * @param ray the ray to intersect with
	 * @return list of {@link Point} objects, or {@code null} if none
	 */
	public List<Point> findIntersections(Ray ray) {
		var list = calculateIntersections(ray);
		return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
	}
}