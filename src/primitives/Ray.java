package primitives;

import static primitives.Util.isZero;

import java.util.List;

import geometries.Intersectable.Intersection;

/**
 * A class that represents a ray in 3D space. A ray is defined by a starting
 * point (head) and a normalized direction vector.
 */
public class Ray {

	/**
	 * The origin point (head) of the ray.
	 */
	private final Point _head;

	/**
	 * The direction vector of the ray (always normalized).
	 */
	private final Vector _direction;

	/**
	 * Constructor that initializes the ray with a point and direction vector.
	 *
	 * @param head      the origin point of the ray
	 * @param direction the direction vector (normalized automatically)
	 */
	public Ray(Point head, Vector direction) {
		this._head = head;
		this._direction = direction.normalize();
	}

	/**
	 * Returns the origin point (head) of the ray.
	 *
	 * @return the origin point
	 */
	public Point getP0() {
		return _head;
	}

	/**
	 * Returns the normalized direction vector of the ray.
	 *
	 * @return the direction vector
	 */
	public Vector getDir() {
		return _direction;
	}

	/**
	 * Calculates a point on the ray at a given distance {@code t} from the origin.
	 * The point is calculated as: P = P₀ + t·v
	 *
	 * @param t the distance from the ray's origin
	 * @return the point at distance {@code t} from the ray's origin
	 */
	public Point getPoint(double t) {
		return isZero(t) ? _head : _head.add(_direction.scale(t));
	}

	/**
	 * Finds the point in the list that is closest to the ray's origin.
	 *
	 * @param points list of points
	 * @return the closest point to the ray's origin, or {@code null} if list is
	 *         empty or null
	 */
	public Point findClosestPoint(List<Point> points) {
		return points == null || points.isEmpty() ? null
				: findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
	}

	/**
	 * Finds the closest intersection point to the ray's origin from a list of
	 * intersections.
	 *
	 * @param intersections list of intersection points
	 * @return the closest intersection, or {@code null} if list is empty or null
	 */
	public Intersection findClosestIntersection(List<Intersection> intersections) {
		if (intersections == null || intersections.isEmpty())
			return null;

		Intersection closest = null;
		double minDistance = Double.MAX_VALUE;

		for (Intersection inter : intersections) {
			double distance = _head.distance(inter.point);
			if (distance < minDistance) {
				minDistance = distance;
				closest = inter;
			}
		}

		return closest;
	}
}
