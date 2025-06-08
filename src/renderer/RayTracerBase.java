package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing. Contains a reference to a scene used for
 * tracing rays and calculating colors.
 */
public abstract class RayTracerBase {

	/**
	 * The scene associated with this ray tracer.
	 */
	protected final Scene scene;

	/**
	 * Constructor to initialize the ray tracer with a scene.
	 *
	 * @param scene the scene to be used by this ray tracer
	 */
	public RayTracerBase(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Traces the given ray and returns the color intensity.
	 *
	 * @param ray the ray to trace
	 * @return the color intensity at the intersection point
	 */
	public abstract Color traceRay(Ray ray);
}
