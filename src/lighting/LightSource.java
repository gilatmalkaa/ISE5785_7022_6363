package lighting;

import primitives.*;

/**
 * Interface for light sources in a 3D scene. Light sources define how light
 * interacts with points in space.
 */
public interface LightSource {

	/**
	 * Returns the intensity of the light at the given point.
	 *
	 * @param p the point in the scene
	 * @return the color intensity at the point
	 */
	Color getIntensity(Point p);

	/**
	 * Returns the direction vector from the light to the given point.
	 *
	 * @param p the point in the scene
	 * @return the normalized direction vector from the light to the point
	 */
	Vector getL(Point p);

	/**
	 * Returns the distance from the light source to the given point.
	 *
	 * @param point the point in the scene
	 * @return the distance to the point
	 */
	double getDistance(Point point);
}