package primitives;

/**
 * Material class represents the material properties of a geometry.
 */
public class Material {
	/**
	 * kD is the diffuse factor.
	 */
	public Double3 kD = Double3.ZERO;

	/**
	 * kS is the specular factor.
	 */
	public Double3 kS = Double3.ZERO;

	/**
	 * kA is the ambient reflection factor.
	 */
	public Double3 kA = Double3.ONE; // Default value is ONE

	/**
	 * Shininess factor for specular reflection.
	 */
	public int nShininess = 0;

	/**
	 * Sets the diffuse factor.
	 * 
	 * @param kD the diffuse factor.
	 * @return the material.
	 */
	public Material setKd(Double3 kD) {
		this.kD = kD;
		return this;
	}

	public Material setKd(double kD) {
		this.kD = new Double3(kD);
		return this;
	}

	/**
	 * Sets the specular factor.
	 * 
	 * @param kS the specular factor.
	 * @return the material.
	 */
	public Material setKs(Double3 kS) {
		this.kS = kS;
		return this;
	}

	public Material setKs(double kS) {
		this.kS = new Double3(kS);
		return this;
	}

	/**
	 * Sets the ambient reflection factor.
	 * 
	 * @param kA the ambient reflection factor.
	 * @return the material.
	 */
	public Material setKa(Double3 kA) {
		this.kA = kA;
		return this;
	}

	/**
	 * Sets the ambient reflection factor.
	 * 
	 * @param kA the ambient reflection factor.
	 * @return the material.
	 */
	public Material setKa(double kA) {
		this.kA = new Double3(kA);
		return this;
	}

	/**
	 * Sets the shininess factor.
	 * 
	 * @param nShininess the shininess factor.
	 * @return the material.
	 */
	public Material setShininess(int nShininess) {
		this.nShininess = nShininess;
		return this;
	}
}
