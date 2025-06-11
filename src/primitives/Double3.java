/**
 * This class represents a tuple of three double values,
 * and serves as a base for primitive mathematical operations such as addition, subtraction, scaling, etc.
 * <p>
 * Can be used for RGB color representation, vectors, and more.
 * </p>
 * 
 * @param d1 the first component
 * @param d2 the second component
 * @param d3 the third component
 * @author Dan Zilberstein
 */
package primitives;

import static primitives.Util.isZero;

public record Double3(double d1, double d2, double d3) {

	/** Zero triad (0,0,0) */
	public static final Double3 ZERO = new Double3(0, 0, 0);

	/** One's triad (1,1,1) */
	public static final Double3 ONE = new Double3(1, 1, 1);

	/**
	 * Constructor that sets all three components to the same value.
	 *
	 * @param value the value to assign to all three components
	 */
	public Double3(double value) {
		this(value, value, value);
	}

	/**
	 * Adds another {@code Double3} to this one component-wise.
	 *
	 * @param rhs the right-hand side operand to add
	 * @return a new {@code Double3} representing the result
	 */
	public Double3 add(Double3 rhs) {
		return new Double3(d1 + rhs.d1, d2 + rhs.d2, d3 + rhs.d3);
	}

	/**
	 * Subtracts another {@code Double3} from this one component-wise.
	 *
	 * @param rhs the right-hand side operand to subtract
	 * @return a new {@code Double3} representing the result
	 */
	public Double3 subtract(Double3 rhs) {
		return new Double3(d1 - rhs.d1, d2 - rhs.d2, d3 - rhs.d3);
	}

	/**
	 * Multiplies this {@code Double3} by a scalar.
	 *
	 * @param rhs the scalar value to multiply each component by
	 * @return a new {@code Double3} representing the result
	 */
	public Double3 scale(double rhs) {
		return new Double3(d1 * rhs, d2 * rhs, d3 * rhs);
	}

	/**
	 * Divides this {@code Double3} by a scalar.
	 *
	 * @param rhs the scalar value to divide each component by
	 * @return a new {@code Double3} representing the result
	 */
	public Double3 reduce(double rhs) {
		return new Double3(d1 / rhs, d2 / rhs, d3 / rhs);
	}

	/**
	 * Multiplies this {@code Double3} with another {@code Double3} component-wise.
	 *
	 * @param rhs the other {@code Double3} to multiply with
	 * @return a new {@code Double3} representing the result
	 */
	public Double3 product(Double3 rhs) {
		return new Double3(d1 * rhs.d1, d2 * rhs.d2, d3 * rhs.d3);
	}

	/**
	 * Checks if all components are less than the specified value.
	 *
	 * @param k the value to compare against
	 * @return true if all components are less than {@code k}, false otherwise
	 */
	public boolean lowerThan(double k) {
		return d1 < k && d2 < k && d3 < k;
	}

	/**
	 * Checks if all components are less than the corresponding components in
	 * another {@code Double3}.
	 *
	 * @param other the {@code Double3} to compare against
	 * @return true if this is strictly less than {@code other} component-wise,
	 *         false otherwise
	 */
	public boolean lowerThan(Double3 other) {
		return d1 < other.d1 && d2 < other.d2 && d3 < other.d3;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Double3 other) && isZero(d1 - other.d1) && isZero(d2 - other.d2)
				&& isZero(d3 - other.d3);
	}

	@Override
	public int hashCode() {
		return (int) Math.round(d1 + d2 + d3);
	}

	@Override
	public String toString() {
		return "(" + d1 + "," + d2 + "," + d3 + ")";
	}
}