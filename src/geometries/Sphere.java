package geometries;

import primitives.*;

/**
 * Department for representation Sphere
 */
public class Sphere extends RadialGeometry {

    /**
     * field for the center point
     */
    final private Point _center;

    /**
     * parameter constructor
     */
    public Sphere(Point center, double radius) {
        super(radius);
        _center = center;
    }

    /**
     * Implementation of the method getNormal
     */
    public Vector getNormal(Point point) {
        return null;
    }
}
