package primitives;

/**
 * A class that represents a vector in 3D Euclidean space. This class extends
 * {@link Point} but ensures that the zero vector is not allowed.
 */
public class Vector extends Point {

	/** Constant vector representing the X axis (1, 0, 0). */
	public static final Vector AXIS_X = new Vector(1, 0, 0);

	/** Constant vector representing the Y axis (0, 1, 0). */
	public static final Vector AXIS_Y = new Vector(0, 1, 0);

	/** Constant vector representing the Z axis (0, 0, 1). */
	public static final Vector AXIS_Z = new Vector(0, 0, 1);

	/**
	 * Constructs a vector with the given components.
	 *
	 * @param x the x-component
	 * @param y the y-component
	 * @param z the z-component
	 * @throws IllegalArgumentException if the vector is a zero vector
	 */
	public Vector(double x, double y, double z) {
		super(x, y, z);
		if (_xyz.equals(Double3.ZERO))
			throw new IllegalArgumentException("ZERO vector is not allowed");
	}

	/**
	 * Constructs a vector from a {@link Double3} instance.
	 *
	 * @param xyz a Double3 representing the vector components
	 * @throws IllegalArgumentException if the vector is a zero vector
	 */
	public Vector(Double3 xyz) {
		super(xyz);
		if (_xyz.equals(Double3.ZERO))
			throw new IllegalArgumentException("ZERO vector is not allowed");
	}

	/**
	 * Adds this vector to another vector.
	 *
	 * @param vector the vector to add
	 * @return the resulting vector
	 */
	public Vector add(Vector vector) {
		return new Vector(_xyz.add(vector._xyz));
	}

	/**
	 * Multiplies the vector by a scalar.
	 *
	 * @param num the scalar to multiply by
	 * @return a new scaled vector
	 */
	public Vector scale(double num) {
		return new Vector(_xyz.scale(num));
	}

	/**
	 * Computes the dot product between this vector and another.
	 *
	 * @param vector the other vector
	 * @return the dot product (scalar result)
	 */
	public double dotProduct(Vector vector) {
		return _xyz.d1() * vector._xyz.d1() + _xyz.d2() * vector._xyz.d2() + _xyz.d3() * vector._xyz.d3();
	}

	/**
	 * Computes the cross product between this vector and another.
	 *
	 * @param vector the other vector
	 * @return a new vector perpendicular to both
	 */
	public Vector crossProduct(Vector vector) {
		return new Vector(_xyz.d2() * vector._xyz.d3() - vector._xyz.d2() * _xyz.d3(),
				_xyz.d3() * vector._xyz.d1() - vector._xyz.d3() * _xyz.d1(),
				_xyz.d1() * vector._xyz.d2() - vector._xyz.d1() * _xyz.d2());
	}

	/**
	 * Returns the squared length of the vector.
	 *
	 * @return squared length
	 */
	public double lengthSquared() {
		return dotProduct(this);
	}

	/**
	 * Returns the length (magnitude) of the vector.
	 *
	 * @return the vector's magnitude
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Normalizes the vector (i.e., makes it unit length).
	 *
	 * @return a normalized version of this vector
	 */
	public Vector normalize() {
		return new Vector(_xyz.reduce(length()));
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		return object instanceof Vector && super.equals(object);
	}

	@Override
	public String toString() {
		return "->" + super.toString();
	}
}