package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Geometries represents a collection of geometries in the 3D space The
 * class is based on the Intersectable
 */
public class Geometries implements Intersectable {
	final private List<Intersectable> geometries = new LinkedList<Intersectable>();

	/**
	 * Default empty Constructor for a collection of geometries in the 3D space
	 */
	public Geometries() {
	};

	/**
	 * Constructor for a collection of geometries in the 3D space
	 * 
	 * @param geometries the geometries to add to the collection
	 */
	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	/**
	 * Add geometries to the collection
	 * 
	 * @param geometries the geometries to add to the collection
	 */
	public void add(Intersectable... geometries) {
		Collections.addAll(this.geometries, geometries);
	}

	@Override
	public List<Point> findIntersections(Ray ray) {
		List<Point> intersections = null;
		for (Intersectable geometry : geometries) {
			List<Point> geometryIntersections = geometry.findIntersections(ray);
			if (geometryIntersections != null) {
				if (intersections == null) {
					intersections = new LinkedList<Point>();
				}
				intersections.addAll(geometryIntersections);
			}
		}
		return intersections;
	}
}
