package primitives;

import java.util.List;
import java.util.Objects;

import geometries.Intersectable.Intersection;

/**
 * A class that represents a ray.
 */
public class Ray {

	/**
	 * Creating the point
	 */
	private final Point _head;

	/**
	 * Creating the vector
	 */
	private final Vector _direction;

	/**
	 * Constructor that accepts point and vector parameters.
	 *
	 * @param head      the point at the origin of the ray
	 * @param direction the direction vector of the ray, which will be normalized
	 */
	public Ray(Point head, Vector direction) {
		this._head = head;
		this._direction = direction.normalize();
	}

	@Override
	public String toString() {
		return "Ray{" + _head + _direction + '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		return (object instanceof Ray other) && this._head.equals(other._head)
				&& this._direction.equals(other._direction);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_head, _direction);
	}

	/**
	 * Returns the origin point (head) of the ray.
	 *
	 * @return the origin point of the ray
	 */
	public Point getP0() {
		return _head;
	}

	/**
	 * Returns the direction vector of the ray. The vector is normalized upon
	 * construction of the ray.
	 *
	 * @return the normalized direction vector of the ray
	 */
	public Vector getDir() {
		return _direction;
	}

	/**
	 * Calculates a point along the ray at a given distance {@code t} from the ray's
	 * origin.
	 * <p>
	 * The point is calculated using the formula: P = P₀ + t·v, where P₀ is the
	 * ray's origin and v is the direction vector.
	 * </p>
	 * 
	 * @param t the distance from the ray's origin along the direction vector. If
	 *          {@code t} is 0, the origin point is returned.
	 * @return the point at distance {@code t} from the origin along the ray's
	 *         direction.
	 */
	public Point getPoint(double t) {
		// if t is zero, return the head point
		try {
			return _head.add(_direction.scale(t));
		} catch (IllegalArgumentException ignored) {
			return _head;
		}
	}

	/**
	 * Method to find the closest point to the head of the ray
	 * 
	 * @param points list of points (either null or non-empty)
	 * @return the closest point to the head of the ray
	 */
	public Point findClosestPoint(List<Point> points) {
		return points == null ? null
				: findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
	}

	/**
	 * Finds the intersection point that is closest to the ray's origin (p₀) from a
	 * given list of intersections.
	 *
	 * @param intersections list of intersection candidates to check
	 * @return the closest intersection to the ray's origin, or {@code null} if none
	 *         exist
	 */
	public Intersection findClosestIntersection(List<Intersection> intersections) {
		if (intersections == null)
			return null;

		Intersection closest = null;
		double minDistanceSquared = Double.POSITIVE_INFINITY;

		for (Intersection intersection : intersections) {
			double distanceSquared = _head.distanceSquared(intersection.point);
			if (distanceSquared < minDistanceSquared) {
				minDistanceSquared = distanceSquared;
				closest = intersection;
			}
		}

		return closest;
	}
}