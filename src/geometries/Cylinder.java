package geometries;

import static primitives.Util.*;

import primitives.*;

/**
 * Represents a finite cylinder in 3D space. The cylinder is defined by a
 * central axis (as a {@link Ray}), a radius (inherited from {@link Tube}), and
 * a finite height.
 */
public class Cylinder extends Tube {

	/** The height of the cylinder. */
	private final double _height;

	/**
	 * Constructs a cylinder with the given axis ray, radius, and height.
	 *
	 * @param ray    the axis ray of the cylinder
	 * @param radius the radius of the cylinder
	 * @param height the height of the cylinder
	 */
	public Cylinder(Ray ray, double radius, double height) {
		super(ray, radius);
		this._height = height;
	}

	@Override
	public Vector getNormal(Point point) {
		Point p0 = _ray.getP0();
		Vector dir = _ray.getDir();
		Vector p0ToPoint = point.subtract(p0);
		double t = alignZero(dir.dotProduct(p0ToPoint));

		if (isZero(t))
			return dir.scale(-1);
		if (isZero(t - _height))
			return dir;

		return super.getNormal(point);
	}
}