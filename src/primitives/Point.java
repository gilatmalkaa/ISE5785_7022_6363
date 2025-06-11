package primitives;

/**
 * A class that represents a point in the 3D Euclidean coordinate system.
 */
public class Point {
	/**
	 * The point that consists of 3 numbers (coordinates).
	 */
	protected final Double3 _xyz;

	/**
	 * The point at the head of the 3D Euclidean coordinate system.
	 */
	public static final Point ZERO = new Point(Double3.ZERO);

	/**
	 * Creates a point from three coordinate values.
	 *
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 * @param z the z-coordinate of the point
	 */
	public Point(double x, double y, double z) {
		_xyz = new Double3(x, y, z);
	}

	/**
	 * Creates a point from a {@link Double3} object.
	 *
	 * @param xyz a Double3 object representing the coordinates of the point
	 */
	Point(Double3 xyz) {
		_xyz = xyz;
	}

	/**
	 * Subtracts another point from this point, producing a vector.
	 *
	 * @param other another point to subtract from this point
	 * @return the vector from {@code other} to this point
	 * @throws IllegalArgumentException if the result is the zero vector
	 */
	public Vector subtract(Point other) {
		return new Vector(_xyz.subtract(other._xyz));
	}

	/**
	 * Adds a vector to this point and returns the resulting point.
	 *
	 * @param vector the vector to add
	 * @return a new point after adding the vector to this point
	 */
	public Point add(Vector vector) {
		return new Point(_xyz.add(vector._xyz));
	}

	/**
	 * Calculates the squared distance between this point and another point.
	 *
	 * @param other the other point
	 * @return the squared Euclidean distance
	 */
	public double distanceSquared(Point other) {
		double dx = other._xyz.d1() - _xyz.d1();
		double dy = other._xyz.d2() - _xyz.d2();
		double dz = other._xyz.d3() - _xyz.d3();
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Calculates the Euclidean distance between this point and another point.
	 *
	 * @param other the other point
	 * @return the Euclidean distance
	 */
	public double distance(Point other) {
		return Math.sqrt(distanceSquared(other));
	}
}