package primitives;

import java.util.Objects;

/**
 * A class that represents a ray.
 */
public class Ray {

    /**
     *  Creating the point
     *  */
    private final Point head;

    /**
     * Creating the vector
     * */
    private final Vector direction;

    /**
     * Constructor that accepts point and vector parameters.
     * @param head the point at the origin of the ray
     * @param direction the direction vector of the ray, which will be normalized
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Returns a string representation of the ray.
     * The string includes the head and direction of the ray.
     * @return a string representation of the ray in the format: "Ray= head: head, direction: direction"
     */
    @Override
    public String toString() {
        return "Ray= " + "head: " + head + ", direction: " + direction + '}';
    }

    /**
     * Compares this ray to another object for equality.
     * @param object the object to compare this ray to
     * @return true if the object is a Ray and has the same head and direction as this ray, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        return (object instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    /**
     * Returns a hash code for the ray.
     * @return an integer hash code representing this ray
     */
    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }
}