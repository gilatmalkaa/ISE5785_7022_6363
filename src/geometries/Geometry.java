package geometries;

import primitives.*;

/**
 * An interface that characterizes a geometric shape
 */
public abstract class Geometry {
    /**
     * Abstract method to get the normal vector at a given point.
     *
     * @param point the point to calculate the normal for
     * @return the normal vector
     */
    public abstract Vector getNormal(Point point);
}
