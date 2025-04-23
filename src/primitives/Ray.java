package primitives;

import java.util.Objects;

/**
 * A class that represents a ray.
 */
public class Ray {

	/**
	 * Creating the point
	 */
	private final Point head;

	/**
	 * Creating the vector
	 */
	private final Vector direction;

	/**
	 * Constructor that accepts point and vector parameters.
	 *
	 * @param head      the point at the origin of the ray
	 * @param direction the direction vector of the ray, which will be normalized
	 */
	public Ray(Point head, Vector direction) {
		this.head = head;
		this.direction = direction.normalize();
	}

	@Override
	public String toString() {
		return "Ray= " + "head: " + head + ", direction: " + direction + '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		return (object instanceof Ray other) && this.head.equals(other.head) && this.direction.equals(other.direction);
	}

	@Override
	public int hashCode() {
		return Objects.hash(head, direction);
	}

	/**
	 * Returns the origin point (head) of the ray.
	 *
	 * @return the origin point of the ray
	 */
	public Point getP0() {
		return head;
	}

	/**
	 * Returns the direction vector of the ray.
	 * The vector is normalized upon construction of the ray.
	 *
	 * @return the normalized direction vector of the ray
	 */
	public Vector getDir() {
		return direction;
	}
}