package geometries;

import primitives.Ray;

/** A class that represents a Cylinder */
public class Cylinder extends Tube {

    /** Stave for the high */
    final private double _height;


    /** constructor */
    public Cylinder(Ray ray, double radius, double height) {
        super(ray, radius);
        this._height = height;
    }

}