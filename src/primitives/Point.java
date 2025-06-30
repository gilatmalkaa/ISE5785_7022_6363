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
	 * Dummy constructor to support placeholder use only. Not for actual usage.
	 */
	public Point() {
		_xyz = Double3.ZERO;
	}

	/**
	 * Creates a constructor by 3 points that are received as parameters.
	 *
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 * @param z the z-coordinate of the point
	 */
	public Point(double x, double y, double z) {
		_xyz = new Double3(x, y, z);
	}

	/**
	 * Creates a constructor with a datum that is a Point object.
	 *
	 * @param xyz a Double3 object representing the coordinates of the point
	 */
	Point(Double3 xyz) {
		_xyz = xyz;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Point other) && this._xyz.equals(other._xyz);
	}

	@Override
	public String toString() {
		return "" + _xyz;
	}

	@Override
	public int hashCode() {
		return _xyz.hashCode();
	}

	/**
	 * Subtracts one point from another, returning the resulting vector.
	 *
	 * @param other another point to subtract from this point
	 * @return the vector from the other point to this point
	 * @throws IllegalArgumentException if the subtraction results in a zero vector
	 */
	public Vector subtract(Point other) {
		return new Vector(_xyz.subtract(other._xyz));
	}

	/**
	 * Adds a vector to this point and returns the resulting new point.
	 *
	 * @param vector the vector to add to this point
	 * @return a new point obtained by adding the vector to this point
	 */
	public Point add(Vector vector) {
		return new Point(_xyz.add(vector._xyz));
	}

	/**
	 * Calculates the squared distance between this point and another point.
	 *
	 * @param other the other point to calculate the distance to
	 * @return the squared distance between this point and the other point
	 */
	public double distanceSquared(Point other) {
		double dx = other._xyz.d1() - _xyz.d1();
		double dy = other._xyz.d2() - _xyz.d2();
		double dz = other._xyz.d3() - _xyz.d3();
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Calculates the distance between 2 points
	 *
	 * @param other Another point from which the distance is calculated
	 * @return A number
	 */
	public double distance(Point other) {
		return Math.sqrt(distanceSquared(other));
	}
}