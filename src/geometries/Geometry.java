package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * An interface that characterizes a geometric shape
 */
public abstract class Geometry implements Intersectable {
	/** Empty explicit default constructor to make javadoc generator happy */
	public Geometry() {
	}

	/**
	 * Abstract method to get the normal vector at a given point.
	 *
	 * @param point the point to calculate the normal for
	 * @return the normal vector
	 */
	public abstract Vector getNormal(Point point);
}
