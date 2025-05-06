package geometries;

import primitives.Ray;
import primitives.Point;
import java.util.List;

/**
 * Interface for geometric shapes that can be intersected by a ray.
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