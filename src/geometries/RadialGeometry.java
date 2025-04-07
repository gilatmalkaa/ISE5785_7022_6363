package geometries;

/**
 * An abstract class that implements the geometry interface
 */
public abstract class RadialGeometry extends Geometry  {
    /**
     * Radius for a round shape
     */
    final protected double _radius;
    /**
     * Radius for a round shape
     */
    final protected double _radiusSquared;

    /**
     * Constructor to initialize the radius and calculate its square.
     *
     * @param radius the radius of the round shape
     */
    RadialGeometry(double radius) {
        _radius = radius;
        _radiusSquared = radius * radius;
    }

}
