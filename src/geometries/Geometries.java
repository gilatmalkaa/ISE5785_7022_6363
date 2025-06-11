package geometries;

import java.util.*;

import primitives.Ray;

/**
 * Represents a collection (composite) of geometric objects that can be
 * intersected by rays. This class implements the Composite design pattern and
 * extends {@link Intersectable}.
 */
public class Geometries extends Intersectable {

	/**
	 * Internal list of all {@link Intersectable} geometries in this composite.
	 */
	private final List<Intersectable> _geometries = new LinkedList<>();

	/**
	 * Constructs an empty collection of geometries.
	 */
	public Geometries() {
	}

	/**
	 * Constructs a geometry collection initialized with the given geometries.
	 *
	 * @param geometries one or more geometries to add to the collection
	 */
	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	/**
	 * Adds one or more geometries to this collection.
	 *
	 * @param geometries the geometries to add
	 */
	public void add(Intersectable... geometries) {
		Collections.addAll(this._geometries, geometries);
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		List<Intersection> intersections = null;
		for (Intersectable geometry : _geometries) {
			List<Intersection> geometryIntersections = geometry.calculateIntersectionsHelper(ray);
			if (geometryIntersections != null) {
				if (intersections == null) {
					intersections = new LinkedList<>();
				}
				intersections.addAll(geometryIntersections);
			}
		}
		return intersections;
	}
}