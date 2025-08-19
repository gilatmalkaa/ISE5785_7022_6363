package geometries;

import static primitives.Util.isZero;

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
		Vector dir = _ray.getDir();
		Vector p0ToPoint = point.subtract(_ray.getP0());
		double t = dir.dotProduct(p0ToPoint);

		if (isZero(t))
			return dir.scale(-1);
		if (isZero(t - _height))
			return dir;

		return super.getNormal(point);
	}

	@Override
	protected primitives.AABB computeBoundingBox() {
		// Axis endpoints: p0 (base) and p1 (top) = p0 + v * height
		primitives.Point p0 = _ray.getP0();
		primitives.Vector v = _ray.getDir().normalize();
		primitives.Point p1 = p0.add(v.scale(_height));

		// Project both endpoints onto coordinate axes (no direct _xyz access)
		primitives.Vector c0 = p0.subtract(primitives.Point.ZERO);
		primitives.Vector c1 = p1.subtract(primitives.Point.ZERO);

		double x0 = c0.dotProduct(primitives.Vector.AXIS_X);
		double y0 = c0.dotProduct(primitives.Vector.AXIS_Y);
		double z0 = c0.dotProduct(primitives.Vector.AXIS_Z);

		double x1 = c1.dotProduct(primitives.Vector.AXIS_X);
		double y1 = c1.dotProduct(primitives.Vector.AXIS_Y);
		double z1 = c1.dotProduct(primitives.Vector.AXIS_Z);

		double r = _radius;

		// Conservative box: expand min/max by radius in all axes
		double minX = Math.min(x0, x1) - r;
		double minY = Math.min(y0, y1) - r;
		double minZ = Math.min(z0, z1) - r;

		double maxX = Math.max(x0, x1) + r;
		double maxY = Math.max(y0, y1) + r;
		double maxZ = Math.max(z0, z1) + r;

		return new primitives.AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}
}