package primitives;

/**
 * A class that represents a vector by a point in space.
 */
public class Vector extends Point {

    /**
     * Creates a vector from 3 coordinates.
     *
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @param z the z-coordinate of the vector
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);

        if (_xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ZERO vector is not allowed");
    }

    /**
     * Creates a vector from a given point.
     *
     * @param xyz the coordinates of the vector as a Double3 object
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);

        if (_xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("ZERO vector is not allowed");
    }

    /**
     * Adds this vector to another vector and returns the resulting vector.
     *
     * @param vector the vector to be added to this vector
     * @return the resulting vector after the addition
     */
    public Vector add(Vector vector) {
        return new Vector(_xyz.add(vector._xyz));
    }

    /**
     * Scales this vector by a given scalar and returns the scaled vector.
     *
     * @param num the scalar value to multiply the vector by
     * @return the scaled vector
     */
    @SuppressWarnings("unused")
    public Vector scale(double num) {
        return new Vector(_xyz.scale(num));
    }

    /**
     * Calculates the scalar product (dot product) of this vector and another vector.
     *
     * @param vector the vector to calculate the dot product with
     * @return the scalar product of the two vectors
     */
    public double dotProduct(Vector vector) {
        return _xyz.d1() * vector._xyz.d1() + _xyz.d2() * vector._xyz.d2() + _xyz.d3() * vector._xyz.d3();
    }

    /**
     * Calculates the vector product (cross product) of this vector and another vector.
     * Returns a new vector that is perpendicular to both vectors.
     *
     * @param vector the vector to calculate the cross product with
     * @return a vector that is perpendicular to both vectors
     */
    public Vector crossProduct(Vector vector) {
        // Calculation of a vector cross product according to the formula of Linear Algebra
        return new Vector(
                _xyz.d2() * vector._xyz.d3() - vector._xyz.d2() * _xyz.d3(),
                _xyz.d3() * vector._xyz.d1() - vector._xyz.d3() * _xyz.d1(),
                _xyz.d1() * vector._xyz.d2() - vector._xyz.d1() * _xyz.d2()
        );
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        // Multiplying each coordinate by itself and adding them together
        return _xyz.d1() * _xyz.d1() + _xyz.d2() * _xyz.d2() + _xyz.d3() * _xyz.d3();
    }

    /**
     * Calculates the length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**git add .
     git commit -m "First commit of project"
     git push origin main
     * Normalizes this vector (makes its length 1).
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        return new Vector(_xyz.reduce(length()));
    }

    /**
     * Compares this vector with another object for equality.
     *
     * @param object the object to compare this vector to
     * @return true if the object is a Vector and is equal to this vector, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        return object instanceof Vector && super.equals(object);
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string representing the vector
     */
    @Override
    public String toString() {
        return "Vector" + super.toString();
    }
}