package geometries;

import static primitives.Util.alignZero;

/**
 * Abstract base class for all geometries with a radial (circular) shape.
 * Inherits from {@link Geometry} and adds a radius and its square.
 */
public abstract class RadialGeometry extends Geometry {

	/**
	 * The radius of the geometry.
	 */
	protected final double _radius;

	/**
	 * The square of the radius (precomputed for efficiency).
	 */
	protected final double _radiusSquared;

	/**
	 * Constructs a radial geometry with the specified radius. Validates that the
	 * radius is positive and computes its square.
	 *
	 * @param radius the radius of the geometry
	 * @throws IllegalArgumentException if the radius is zero or negative
	 */
	RadialGeometry(double radius) {
		if (alignZero(radius) <= 0)
			throw new IllegalArgumentException("Radius must be positive");
		_radius = radius;
		_radiusSquared = radius * radius;
	}
}