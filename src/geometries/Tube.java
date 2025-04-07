package geometries;

import primitives.*;

/** A class that represents a tube */
public class Tube extends RadialGeometry {
    protected final Ray _ray;

    /** constructor */
    public Tube(Ray ray, double radius) {
        super(radius);
        _ray = ray;
    }

    /** Implementation of the method getNormal */
    public Vector getNormal(Point point) {
        return  null;
    }
}
