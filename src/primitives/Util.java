package primitives;

/**
 * Util class is used for some internal utilities, e.g. controlling accuracy.
 * Provides static helper methods for numerical precision and utility
 * calculations. This class cannot be instantiated.
 * 
 * @author Dan
 */
public final class Util {
	/**
	 * It is binary, equivalent to ~1/1,000,000,000,000 in decimal (12 digits)
	 */
	private static final int ACCURACY = -40;

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Util() {
	}

	/**
	 * Gets the exponent of a double number based on IEEE 754 binary representation.
	 *
	 * @param num the original number
	 * @return the exponent value of the double
	 */
	private static int getExp(double num) {
		return (int) ((Double.doubleToRawLongBits(num) >> 52) & 0x7FFL) - 1023;
	}

	/**
	 * Checks whether the given number is zero or nearly zero, based on binary
	 * exponent comparison to a defined accuracy threshold.
	 *
	 * @param number the number to check
	 * @return true if the number is effectively zero, false otherwise
	 */
	public static boolean isZero(double number) {
		return getExp(number) < ACCURACY;
	}

	/**
	 * Rounds a number to 0 if it is close enough to zero (numerical noise).
	 *
	 * @param number the number to align
	 * @return 0.0 if the number is nearly zero, otherwise returns the number itself
	 */
	public static double alignZero(double number) {
		return isZero(number) ? 0.0 : number;
	}

	/**
	 * Checks whether two double values have the same sign.
	 *
	 * @param n1 first number
	 * @param n2 second number
	 * @return true if both numbers are positive or both are negative
	 */
	public static boolean compareSign(double n1, double n2) {
		return (n1 < 0 && n2 < 0) || (n1 > 0 && n2 > 0);
	}

	/**
	 * Returns a random double value between the given minimum (inclusive) and
	 * maximum (exclusive) bounds.
	 *
	 * @param min minimum value (inclusive)
	 * @param max maximum value (exclusive)
	 * @return a random double within the specified range
	 */
	public static double random(double min, double max) {
		return Math.random() * (max - min) + min;
	}
}
