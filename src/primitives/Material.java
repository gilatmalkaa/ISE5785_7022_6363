package primitives;

/**
 * Material class represents the material properties of a geometry. These
 * properties are used in lighting calculations such as diffuse and specular
 * reflections.
 */
public class Material {
	/**
	 * Explicit default constructor to satisfy JavaDoc generator.
	 */
	public Material() {
	}

	/** kD is the diffuse reflection coefficient. */
	public Double3 kD = Double3.ZERO;

	/** kS is the specular reflection coefficient. */
	public Double3 kS = Double3.ZERO;

	/** kA is the ambient reflection coefficient. */
	public Double3 kA = Double3.ONE;

	/** Shininess factor for specular reflection. */
	public int nShininess = 0;

	/**
	 * Sets the diffuse reflection coefficient.
	 *
	 * @param kD the diffuse reflection coefficient as {@link Double3}
	 * @return the updated {@code Material} instance
	 */
	public Material setKd(Double3 kD) {
		this.kD = kD;
		return this;
	}

	/**
	 * Sets the diffuse reflection coefficient using a single scalar.
	 *
	 * @param kD the diffuse reflection coefficient as a scalar
	 * @return the updated {@code Material} instance
	 */
	public Material setKd(double kD) {
		this.kD = new Double3(kD);
		return this;
	}

	/**
	 * Sets the specular reflection coefficient.
	 *
	 * @param kS the specular reflection coefficient as {@link Double3}
	 * @return the updated {@code Material} instance
	 */
	public Material setKs(Double3 kS) {
		this.kS = kS;
		return this;
	}

	/**
	 * Sets the specular reflection coefficient using a single scalar.
	 *
	 * @param kS the specular reflection coefficient as a scalar
	 * @return the updated {@code Material} instance
	 */
	public Material setKs(double kS) {
		this.kS = new Double3(kS);
		return this;
	}

	/**
	 * Sets the ambient reflection coefficient.
	 *
	 * @param kA the ambient reflection coefficient as {@link Double3}
	 * @return the updated {@code Material} instance
	 */
	public Material setKa(Double3 kA) {
		this.kA = kA;
		return this;
	}

	/**
	 * Sets the ambient reflection coefficient using a single scalar.
	 *
	 * @param kA the ambient reflection coefficient as a scalar
	 * @return the updated {@code Material} instance
	 */
	public Material setKa(double kA) {
		this.kA = new Double3(kA);
		return this;
	}

	/**
	 * Sets the shininess factor for specular reflection.
	 *
	 * @param nShininess the shininess factor
	 * @return the updated {@code Material} instance
	 */
	public Material setShininess(int nShininess) {
		this.nShininess = nShininess;
		return this;
	}
}