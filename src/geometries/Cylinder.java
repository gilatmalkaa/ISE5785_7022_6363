package geometries;

import primitives.Ray;

/**
 * A class that represents a cylinder.
 * The cylinder is defined by its axis (a ray) and its radius (from the parent class Tube).
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    final private double _height;

    /**
     * Constructor to create a cylinder with a specified axis, radius, and height.
     *
     * @param ray    the ray defining the axis of the cylinder
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Ray ray, double radius, double height) {
        super(ray, radius);  // Calling the constructor of Tube to set the ray and radius
        this._height = height;
    }

}