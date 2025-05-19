package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;

/**
 * Interface for geometric shapes that can be intersected by a ray. Any shape
 * implementing this interface must implement a method to find intersection
 * points with a given ray.
 */
public interface Intersectable {
	/**
	 * Find intersection points with the given ray.
	 * 
	 * @param ray the ray to intersect with
	 * @return List of intersection points, or null if none
	 */
	List<Point> findIntersections(Ray ray);
}