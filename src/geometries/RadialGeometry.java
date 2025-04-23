package geometries;

import static primitives.Util.*;

/**
 * An abstract class that implements the geometry interface
 */
public abstract class RadialGeometry extends Geometry {
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
		if (alignZero(radius) <= 0)
			throw new IllegalArgumentException("Radius must be positive");
		_radius = radius;
		_radiusSquared = radius * radius;
	}

}
