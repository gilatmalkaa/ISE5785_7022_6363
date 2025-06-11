package renderer;

/**
 * Enumeration of ray tracer types used for rendering scenes.
 * <p>
 * Each type represents a different ray tracing strategy.
 * </p>
 */
public enum RayTracerType {

	/**
	 * Simple (basic) ray tracer.
	 * <p>
	 * This tracer performs basic intersection and shading calculations without
	 * acceleration structures.
	 * </p>
	 */
	SIMPLE,

	/**
	 * Ray tracer using a regular spatial grid.
	 * <p>
	 * This tracer utilizes a uniform grid structure to accelerate intersection
	 * computations by reducing the number of object checks.
	 * </p>
	 */
	GRID
}
