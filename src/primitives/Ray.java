package primitives;

import java.util.Objects;

public class Ray {

    /** Creating the point */
    private final Point head;

    /** Creating the vector */
    private final Vector direction;

    /** A constructor that accepts point and vector parameters */
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
        if (this == object) return true;
        return (object instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }
}
