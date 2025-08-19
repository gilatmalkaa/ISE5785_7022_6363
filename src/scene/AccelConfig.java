package scene;

/**
 * Holds acceleration settings (CBR, BVH Manual, BVH Auto). Used in tests to
 * toggle performance features.
 */
public class AccelConfig {

	/**
	 * Create a new AccelConfig with default values. CBR = false, BVH Manual =
	 * false, BVH Auto = false, maxDepth = 20, leafSize = 8.
	 */
	public AccelConfig() {
		// fields already initialized with default values
	}

	/** Enable/disable CBR (AABB early reject). */
	private boolean _enableCBR = false;

	/** Enable/disable Manual BVH grouping. */
	private boolean _enableBVHManual = false;

	/** Enable/disable Automatic BVH building. */
	private boolean _enableBVHAuto = false;

	/** Maximum depth for automatic BVH building. */
	private int _bvhMaxDepth = 20;

	/** Maximum number of geometries per leaf in automatic BVH. */
	private int _bvhLeafSize = 8;

	/**
	 * Turn CBR on or off.
	 * 
	 * @param v true to enable, false to disable
	 * @return this config (for chaining)
	 */
	public AccelConfig setCBR(boolean v) {
		_enableCBR = v;
		return this;
	}

	/**
	 * Turn Manual BVH on or off.
	 * 
	 * @param v true to enable, false to disable
	 * @return this config (for chaining)
	 */
	public AccelConfig setBVHManual(boolean v) {
		_enableBVHManual = v;
		return this;
	}

	/**
	 * Turn Automatic BVH on or off.
	 * 
	 * @param v true to enable, false to disable
	 * @return this config (for chaining)
	 */
	public AccelConfig setBVHAuto(boolean v) {
		_enableBVHAuto = v;
		return this;
	}

	/**
	 * Set maximum depth for Auto BVH.
	 * 
	 * @param d max depth value
	 * @return this config (for chaining)
	 */
	public AccelConfig setBvhMaxDepth(int d) {
		_bvhMaxDepth = d;
		return this;
	}

	/**
	 * Set maximum leaf size for Auto BVH.
	 * 
	 * @param s number of geometries per leaf
	 * @return this config (for chaining)
	 */
	public AccelConfig setBvhLeafSize(int s) {
		_bvhLeafSize = s;
		return this;
	}

	/**
	 * Check if CBR is enabled.
	 * 
	 * @return true if enabled
	 */
	public boolean enableCBR() {
		return _enableCBR;
	}

	/**
	 * Check if Manual BVH is enabled.
	 * 
	 * @return true if enabled
	 */
	public boolean enableBVHManual() {
		return _enableBVHManual;
	}

	/**
	 * Check if Auto BVH is enabled.
	 * 
	 * @return true if enabled
	 */
	public boolean enableBVHAuto() {
		return _enableBVHAuto;
	}

	/**
	 * Get the maximum depth for Auto BVH.
	 * 
	 * @return max depth
	 */
	public int bvhMaxDepth() {
		return _bvhMaxDepth;
	}

	/**
	 * Get the maximum leaf size for Auto BVH.
	 * 
	 * @return leaf size
	 */
	public int bvhLeafSize() {
		return _bvhLeafSize;
	}
}