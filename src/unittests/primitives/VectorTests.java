/**
 * 
 */
package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Double3;
import primitives.Vector;

/**
 * Unit tests for {@link primitives.Vector} class. Includes tests for add,
 * scale, dotProduct, crossProduct, length, normalize, etc. Each test follows EP
 * and BVA principles.
 * 
 * @author Gilat Kedem and Shira Amar
 */
class VectorTests {

	/**
	 * Test method for {@link primitives.Vector#Vector(double, double, double)}
	 * Ensures vectors are initialized correctly, and zero vector is rejected
	 */
	@Test
	void testVectorDoubleDoubleDouble() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Valid vector (1, 2, 3)
		assertDoesNotThrow(() -> new Vector(1, 2, 3), "Constructor with coordinates failed on valid vector");

		// TC02: Valid vector with negative components (-1, -2, -3)
		assertDoesNotThrow(() -> new Vector(-1, -2, -3), "Constructor with coordinates failed on negative vector");

		// =============== Boundary Values Tests ==================

		// TC11: Zero vector (0, 0, 0) → should throw exception
		assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0),
				"Constructor with coordinates should throw for zero vector");

	}

	/**
	 * Test method for {@link primitives.Vector#Vector(primitives.Double3)}.
	 */
	@Test
	void testVectorDouble3() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Valid Double3 vector (1, 2, 3)
		assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "Constructor with Double3 failed on valid vector");

		// TC02: Valid Double3 with negatives (-1, -2, -3)
		assertDoesNotThrow(() -> new Vector(new Double3(-1, -2, -3)),
				"Constructor with Double3 failed on negative vector");

		// =============== Boundary Values Tests ==================

		// TC11: Zero vector → should throw exception
		assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO),
				"Constructor with Double3 should throw for zero vector");

	}

	/**
	 * Test method for {@link primitives.Vector#add(primitives.Vector)}. This test
	 * checks vector addition and throws exception when resulting in a zero vector
	 */
	@Test
	void testAddVector() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Add two regular vectors → should return the sum
		assertEquals(new Vector(2, 3, 4), new Vector(1, 1, 1).add(new Vector(1, 2, 3)),
				"add() failed on simple addition");

		// TC02: Add vector with negatives → should return correct mixed result
		assertEquals(new Vector(0, 0, 6), new Vector(1, 2, 3).add(new Vector(-1, -2, 3)),
				"add() failed on vector with negative components");

		// =============== Boundary Values Tests ==================

		// TC11: Add opposite vector → should throw exception (resulting in zero vector)
		assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).add(new Vector(-1, -2, -3)),
				"add() should throw for resulting zero vector");
	}

	/**
	 * Test method for {@link primitives.Vector#scale(double)} This test checks
	 * scaling a vector by different scalar values, and ensures scaling to zero
	 * vector is not allowed.
	 */
	@Test
	void testScale() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Scale by positive scalar
		assertEquals(new Vector(2, 4, 6), new Vector(1, 2, 3).scale(2), "scale() failed on positive scalar");

		// TC02: Scale by negative scalar → should reverse direction
		assertEquals(new Vector(-1, -2, -3), new Vector(1, 2, 3).scale(-1), "scale() failed on negative scalar");

		// =============== Boundary Values Tests ==================

		// TC11: Scale by 0 → should throw exception (zero vector not allowed)
		assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).scale(0),
				"scale() should throw when scaling to zero vector");
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)} This
	 * test checks scalar (dot) product results between vectors.
	 */
	@Test
	void testDotProduct() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Dot product with same direction → positive result
		assertEquals(14, new Vector(1, 2, 3).dotProduct(new Vector(1, 2, 3)), "dotProduct() failed on equal vectors");

		// TC02: Dot product with opposite direction → negative result
		assertEquals(-14, new Vector(1, 2, 3).dotProduct(new Vector(-1, -2, -3)),
				"dotProduct() failed on opposite vectors");

		// TC03: Dot product of two negative vectors → should return positive result
		assertEquals(14, new Vector(-1, -2, -3).dotProduct(new Vector(-1, -2, -3)),
				"dotProduct() failed on two negative vectors (should be positive)");

		// =============== Boundary Values Tests ==================

		// TC11: Dot product of orthogonal vectors → should return 0
		assertEquals(0, new Vector(1, 2, 3).dotProduct(new Vector(0, 3, -2)), 1e-10,
				"dotProduct() with orthogonal vectors should return 0");
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 * This test checks that the result is orthogonal to both vectors, and that an
	 * exception is thrown when vectors are parallel.
	 */
	@Test
	void testCrossProduct() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: Cross product of V1 and V2 → should be orthogonal to both
		Vector v1 = new Vector(1, 2, 3);
		Vector v2 = new Vector(-2, -4, -1);
		Vector vr = v1.crossProduct(v2);

		// Check orthogonality
		assertEquals(0, vr.dotProduct(v1), 1e-10, "Result not orthogonal to V1");
		assertEquals(0, vr.dotProduct(v2), 1e-10, "Result not orthogonal to V2");

		// Check length (not zero)
		assertTrue(vr.length() > 0, "crossProduct() resulted in zero vector");

		// =============== Boundary Values Tests ==================

		// TC11: Cross product of parallel vectors → should throw exception
		assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(2, 4, 6)), // parallel to v1
				"crossProduct() should throw for parallel vectors");

	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}. This test checks
	 * the squared length of various vectors.
	 */
	@Test
	void testLengthSquared() {
		// ============ Equivalence Partitions Tests ==============

		// TC01: V = (1, 2, 3) → lengthSquared = 1^2 + 2^2 + 3^2 = 14
		assertEquals(14, new Vector(1, 2, 3).lengthSquared(), "lengthSquared() failed on positive vector");

		// TC02: V = (-1, -2, -3) → same result: 1 + 4 + 9 = 14
		assertEquals(14, new Vector(-1, -2, -3).lengthSquared(), "lengthSquared() failed on negative vector");
	}

	/**
	 * Test method for {@link primitives.Vector#length()}. This test checks the
	 * actual length (magnitude) of a vector.
	 */
	@Test
	void testLength() {
		// ============ Equivalence Partitions Tests ==============
		
		// TC01: V = (1, 2, 3) → lengthSquared = 1^2 + 2^2 + 3^2 = 14
		assertEquals(14, new Vector(1, 2, 3).lengthSquared(), "lengthSquared() failed on positive vector");

		// TC02: V = (-1, -2, -3) → same result: 1 + 4 + 9 = 14
		assertEquals(14, new Vector(-1, -2, -3).lengthSquared(), "lengthSquared() failed on negative vector");
	}

	/**
	 * Test method for {@link primitives.Vector#length()}. This test checks the
	 * actual length (magnitude) of a vector.
	 */
	@Test
	void testLength1() {
		// TC01: Vector = (1, 2, 3), expected length = sqrt(14)
		assertEquals(Math.sqrt(14), new Vector(1, 2, 3).length(), 1e-10, "length() failed on regular vector");

		// TC02: V = (0, 3, 4) → sqrt(9+16) = 5
		assertEquals(5, new Vector(0, 3, 4).length(), 1e-10, "length() failed on vector with known length");
	}

	/**
	 * Test method for {@link primitives.Vector#normalize()}. This test checks that
	 * the result is a unit vector with the same direction
	 */
	@Test
	void testNormalize() {
		Vector original = new Vector(1, 2, 3);
		Vector normalized = original.normalize();

		// ============ Equivalence Partitions Tests ==============

		// TC01: The normalized vector should have length = 1
		assertEquals(1, normalized.length(), 1e-10, "normalize() did not produce unit vector");

		// TC02: Direction should remain the same (dotProduct > 0)
		assertTrue(original.dotProduct(normalized) > 0, "normalize() changed the direction of the vector");
	}

}
