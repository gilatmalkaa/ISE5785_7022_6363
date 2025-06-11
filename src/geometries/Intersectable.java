package geometries;

import java.util.List;

import lighting.LightSource;
import primitives.*;

/**
 * Abstract class representing an intersectable object in the scene
 */
public abstract class Intersectable {

	public static class Intersection {
		public final Geometry geometry;
		public final Point point;
		public final Material material;

		public Vector cacheRayDirection;
		public Vector cacheLightDirection;
		public Vector cacheNormal;
		public double cacheRayDirectionDotCacheNormal;
		public LightSource cacheLightSource;
		public double cacheLightSourceDirectionDotCacheNormal;

		public Intersection(Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
			this.material = geometry != null ? geometry.getMaterial() : null;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			return obj instanceof Intersection other && geometry == other.geometry && point.equals(other.point); // Changed
																													// geometry
																													// comparison
		}

		@Override
		public String toString() {
			return "Intersection{" + "geometry=" + geometry + ", point=" + point + '}';
		}
	}

	/**
	 * Find intersections of a ray with the geometry (helper method)
	 *
	 * @param ray the ray to find intersections with
	 * @return a list of intersection points
	 */
	protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

	/**
	 * Find intersections of a ray with the geometry
	 *
	 * @param ray the ray to find intersections with
	 * @return a list of intersection points
	 */
	public final List<Intersection> calculateIntersections(Ray ray) {
		return calculateIntersectionsHelper(ray);
	}

	/**
	 * Find intersections of a ray with the geometry
	 *
	 * @param ray the ray to find intersections with
	 * @return a list of intersection points
	 */
	public List<Point> findIntersections(Ray ray) {
		var list = calculateIntersections(ray); // Changed variable name
		return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
	}
}