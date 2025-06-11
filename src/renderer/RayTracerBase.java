package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing.
 * <p>
 * Contains a reference to a scene used for tracing rays and calculating colors.
 * Subclasses should implement specific ray tracing algorithms by overriding the
 * {@link #traceRay(Ray)} method.
 * </p>
 */
public abstract class RayTracerBase {

	/**
	 * The scene associated with this ray tracer.
	 */
	protected final Scene scene;

	/**
	 * Constructs a ray tracer with the specified scene.
	 *
	 * @param scene the scene to be used for ray tracing computations
	 */
	public RayTracerBase(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Traces the given ray through the scene and returns the resulting color.
	 *
	 * @param ray the ray to trace
	 * @return the color at the point the ray intersects the scene
	 */
	public abstract Color traceRay(Ray ray);
}
