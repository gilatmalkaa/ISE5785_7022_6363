package primitives;

import static primitives.Util.isZero;

import java.util.Objects;

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
		return "Ray= " + "head: " + _head + ", direction: " + _direction + '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		return (object instanceof Ray other) && this._head.equals(other._head) && this._direction.equals(other._direction);
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
		return isZero(t) ? _head : _head.add(_direction.scale(t));
	}
}