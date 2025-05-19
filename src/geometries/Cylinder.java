package geometries;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A class that represents a cylinder. The cylinder is defined by its axis (a
 * ray) and its radius (from the parent class Tube).
 */
public class Cylinder extends Tube {

	/**
	 * The height of the cylinder.
	 */
	private final double _height;

	/**
	 * Constructor to create a cylinder with a specified axis, radius, and height.
	 *
	 * @param ray    the ray defining the axis of the cylinder
	 * @param radius the radius of the cylinder
	 * @param height the height of the cylinder
	 */
	public Cylinder(Ray ray, double radius, double height) {
		super(ray, radius); // Calling the constructor of Tube to set the ray and radius
		this._height = height;
	}

	@Override
	public Vector getNormal(Point point) {
		// Get the base point and direction vector of the cylinder's axis
		Point p0 = _ray.getP0();
		Vector dir = _ray.getDir();

		// Compute the projection of the point onto the axis
		Vector p0ToPoint = point.subtract(p0);
		double t = alignZero(dir.dotProduct(p0ToPoint));

		// Check if the point lies on the bottom base (t ≈ 0)
		if (isZero(t))
			return dir.scale(-1); // Normal points opposite to the axis direction

		// Check if the point lies on the top base (t ≈ height)
		if (isZero(t - _height))
			return dir; // Normal points in the axis direction

		// Otherwise, the point lies on the side surface – use the Tube logic
		return super.getNormal(point);
	}

}