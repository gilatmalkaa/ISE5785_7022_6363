package geometries;

import primitives.Point;

/**
 * A class that represents a triangle, which is a specific type of polygon.
 * The triangle is defined by three points in 3D space.
 */
public class Triangle extends Polygon {

    /**
     * Constructor to create a triangle from three points.
     *
     * @param p1 the first point of the triangle
     * @param p2 the second point of the triangle
     * @param p3 the third point of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);  // Calls the constructor of Polygon to create the triangle
    }
}