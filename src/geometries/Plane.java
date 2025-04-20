package geometries;

import primitives.*;

/**
 * A class that represents a plane
 */
public class Plane extends Geometry {

    /**
     * The point that lies on the plane
     */
    private final Point q;
    /**
     * The normal vector to the plane
     */
    private final Vector normal;

    /**
     * Constructor with parameters.
     *
     * @param q      the point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Builder that gets points and calculates the normal vector.
     *
     * @param q1 the first point on the plane
     * @param q2 the second point on the plane
     * @param q3 the third point on the plane
     */
    public Plane(Point q1, Point q2, Point q3) {
        normal = null;
        q = q1;
    }

    /**
     * Getter for the normal vector.
     *
     * @return the normal vector of the plane
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Calculates the normal vector at a given point on the plane.
     *
     * @param point the point on the plane
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }
}