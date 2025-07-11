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

	/**
	 * Transparency coefficient. Controls how much light passes through the surface.
	 */
	public Double3 kT = Double3.ZERO;

	/**
	 * Reflection coefficient. Controls how much light is reflected like a mirror.
	 */
	public Double3 kR = Double3.ZERO;

	/** Shininess factor for specular reflection. */
	public int nShininess = 0;

	/**
	 * Sets the diffuse reflection coefficient.
	 *
	 * @param kD the diffuse reflection coefficient as {@link Double3}
	 * @return the updated {@code Material} instance
	 */
	public Material setKD(Double3 kD) {
		this.kD = kD;
		return this;
	}

	/**
	 * Sets the diffuse reflection coefficient using a single scalar.
	 *
	 * @param kD the diffuse reflection coefficient as a scalar
	 * @return the updated {@code Material} instance
	 */
	public Material setKD(double kD) {
		this.kD = new Double3(kD);
		return this;
	}

	/**
	 * Sets the specular reflection coefficient.
	 *
	 * @param kS the specular reflection coefficient as {@link Double3}
	 * @return the updated {@code Material} instance
	 */
	public Material setKS(Double3 kS) {
		this.kS = kS;
		return this;
	}

	/**
	 * Sets the specular reflection coefficient using a single scalar.
	 *
	 * @param kS the specular reflection coefficient as a scalar
	 * @return the updated {@code Material} instance
	 */
	public Material setKS(double kS) {
		this.kS = new Double3(kS);
		return this;
	}

	/**
	 * Sets the ambient reflection coefficient.
	 *
	 * @param kA the ambient reflection coefficient as {@link Double3}
	 * @return the updated {@code Material} instance
	 */
	public Material setKA(Double3 kA) {
		this.kA = kA;
		return this;
	}

	/**
	 * Sets the ambient reflection coefficient using a single scalar.
	 *
	 * @param kA the ambient reflection coefficient as a scalar
	 * @return the updated {@code Material} instance
	 */
	public Material setKA(double kA) {
		this.kA = new Double3(kA);
		return this;
	}

	/**
	 * Sets the transparency coefficient using a {@link Double3}.
	 *
	 * @param kT the transparency coefficient as a {@code Double3}
	 * @return the current {@code Material} object for method chaining
	 */
	public Material setKT(Double3 kT) {
		this.kT = kT;
		return this;
	}

	/**
	 * Sets the transparency coefficient using a single double value.
	 *
	 * @param kT the transparency coefficient value
	 * @return the current {@code Material} object for method chaining
	 */
	public Material setKT(double kT) {
		return setKT(new Double3(kT));
	}

	/**
	 * Sets the reflection coefficient using a {@link Double3}.
	 *
	 * @param kR the reflection coefficient as a {@code Double3}
	 * @return the current {@code Material} object for method chaining
	 */
	public Material setKR(Double3 kR) {
		this.kR = kR;
		return this;
	}

	/**
	 * Sets the reflection coefficient using a single double value.
	 *
	 * @param kR the reflection coefficient value
	 * @return the current {@code Material} object for method chaining
	 */
	public Material setKR(double kR) {
		return setKR(new Double3(kR));
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