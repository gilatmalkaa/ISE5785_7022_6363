package geometries;

import primitives.*;

/**
 * A class that represents a tube.
 * It extends the RadialGeometry class and includes a ray defining the tube's axis.
 */
public class Tube extends RadialGeometry {

    /**
     * The ray that defines the axis of the tube.
     */
    protected final Ray _ray;

    /**
     * Constructor to create a tube with a specified axis and radius.
     *
     * @param ray    the ray defining the axis of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray ray, double radius) {
        super(radius);  // Calling the constructor of RadialGeometry to set the radius
        _ray = ray;
    }

    /**
     * Calculates the normal vector at a given point on the tube's surface.
     * The normal vector is perpendicular to the surface of the tube at the specified point.
     *
     * @param point the point on the surface of the tube
     * @return the normal vector at the given point
     */
    public Vector getNormal(Point point) {
        return null;  // Placeholder, implement logic for calculating the normal
    }
}