package primitives;

/**
 * Axis-Aligned Bounding Box (AABB).
 * 
 * Holds min and max coordinates (x, y, z) of a box aligned to axes. Used for
 * fast intersection checks in acceleration structures.
 */
public class AABB {
	/** Minimum x coordinate of the box. */
	public final double minX;
	/** Minimum y coordinate of the box. */
	public final double minY;
	/** Minimum z coordinate of the box. */
	public final double minZ;
	/** Maximum x coordinate of the box. */
	public final double maxX;
	/** Maximum y coordinate of the box. */
	public final double maxY;
	/** Maximum z coordinate of the box. */
	public final double maxZ;

	/**
	 * Create a new axis-aligned bounding box with given min and max values.
	 *
	 * @param minX minimum x
	 * @param minY minimum y
	 * @param minZ minimum z
	 * @param maxX maximum x
	 * @param maxY maximum y
	 * @param maxZ maximum z
	 */
	public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	/**
	 * Check if a ray hits this AABB using the slabs method.
	 *
	 * @param ray  the ray to test
	 * @param tMax maximum ray distance (can be +INF)
	 * @return true if ray intersects the box, false otherwise
	 */
	public boolean hit(Ray ray, double tMax) {
		double tmin = 0.0;
		double tmax = tMax;

		Double3 po = ray.getP0()._xyz;
		Double3 vd = ray.getDir()._xyz;

		double[] o = { po.d1(), po.d2(), po.d3() };
		double[] d = { vd.d1(), vd.d2(), vd.d3() };
		double[] mn = { minX, minY, minZ };
		double[] mx = { maxX, maxY, maxZ };

		for (int k = 0; k < 3; k++) {
			double invD = 1.0 / d[k]; // OK even if d[k]==0 (=> +/-Infinity)
			double t0 = (mn[k] - o[k]) * invD;
			double t1 = (mx[k] - o[k]) * invD;
			if (invD < 0) {
				double tmp = t0;
				t0 = t1;
				t1 = tmp;
			}
			tmin = Math.max(tmin, t0);
			tmax = Math.min(tmax, t1);
			if (tmax <= tmin)
				return false;
		}
		return true;
	}

	/**
	 * Build a new AABB that is the union of two boxes.
	 * 
	 * @param a first box (may be null)
	 * @param b second box (may be null)
	 * @return merged box, or the non-null one if only one exists
	 */
	public static AABB union(AABB a, AABB b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return new AABB(Math.min(a.minX, b.minX), Math.min(a.minY, b.minY), Math.min(a.minZ, b.minZ),
				Math.max(a.maxX, b.maxX), Math.max(a.maxY, b.maxY), Math.max(a.maxZ, b.maxZ));
	}
}