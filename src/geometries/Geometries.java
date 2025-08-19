package geometries;

import java.util.LinkedList;
import java.util.List;

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

	/** flag: auto BVH already built for this node */
	private boolean _builtAuto = false;

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
	 * Add one or more geometries to this collection.
	 *
	 * @param geometries geometries to add (null values are ignored)
	 * @return this Geometries instance (for chaining)
	 */
	public Geometries add(Intersectable... geometries) {
		if (geometries != null) {
			for (Intersectable g : geometries) {
				if (g != null)
					this._geometries.add(g);
			}
		}
		return this;
	}

	/**
	 * Create a grouped Geometries node from given children.
	 * 
	 * @param children geometries to include
	 * @return new Geometries containing all children
	 */
	public static Geometries group(Intersectable... children) {
		return new Geometries(children);
	}

	/** Build auto-BVH once if config requests it. */
	private void ensureBVHAutoBuilt() {
		var cfg = scene.Scene.currentConfig();
		if (cfg == null || !cfg.enableBVHAuto() || _builtAuto)
			return;
		buildBVHAuto(cfg.bvhMaxDepth(), cfg.bvhLeafSize());
		_builtAuto = true;
		// recompute this node's AABB after restructure
		_aabb = computeBoundingBox();
	}

	/**
	 * Build automatic BVH for this node.
	 * 
	 * @param maxDepth maximum depth allowed
	 * @param leafSize max geometries per leaf
	 */
	private void buildBVHAuto(int maxDepth, int leafSize) {
		if (_geometries.size() <= Math.max(1, leafSize) || maxDepth <= 0)
			return;

		// ensure every child has a box; if none have — give up (infinite shapes only)
		boolean anyBox = false;
		for (Intersectable g : _geometries) {
			if (g._aabb == null)
				g._aabb = g.computeBoundingBox();
			if (g._aabb != null)
				anyBox = true;
		}
		if (!anyBox)
			return;

		// union box + axis extents
		primitives.AABB u = null;
		for (Intersectable g : _geometries)
			if (g._aabb != null)
				u = primitives.AABB.union(u, g._aabb);
		if (u == null)
			return; // all infinite -> nothing to split

		double lenX = u.maxX - u.minX, lenY = u.maxY - u.minY, lenZ = u.maxZ - u.minZ;
		int axis = (lenX >= lenY && lenX >= lenZ) ? 0 : (lenY >= lenZ ? 1 : 2);

		// sort by center along axis; items without box -> push to end
		_geometries.sort((a, b) -> {
			double ca = centerOnAxis(a, axis);
			double cb = centerOnAxis(b, axis);
			return Double.compare(ca, cb);
		});

		int n = _geometries.size();
		int mid = n / 2;
		if (mid == 0 || mid == n)
			return; // cannot split

		Geometries left = new Geometries();
		Geometries right = new Geometries();
		for (int i = 0; i < mid; i++)
			left._geometries.add(_geometries.get(i));
		for (int i = mid; i < n; i++)
			right._geometries.add(_geometries.get(i));

		// replace children by two BVH subgroups
		_geometries.clear();
		_geometries.add(left);
		_geometries.add(right);

		// recurse
		left.buildBVHAuto(maxDepth - 1, leafSize);
		right.buildBVHAuto(maxDepth - 1, leafSize);

		// cache AABBs for subgroups
		left._aabb = left.computeBoundingBox();
		right._aabb = right.computeBoundingBox();
	}

	/**
	 * Compute the center of a geometry on a given axis.
	 * 
	 * @param g    the geometry
	 * @param axis axis index (0=x,1=y,2=z)
	 * @return center coordinate on that axis
	 */
	private static double centerOnAxis(Intersectable g, int axis) {
		var b = g._aabb;
		if (b == null)
			return Double.POSITIVE_INFINITY;
		return switch (axis) {
		case 0 -> 0.5 * (b.minX + b.maxX);
		case 1 -> 0.5 * (b.minY + b.maxY);
		default -> 0.5 * (b.minZ + b.maxZ);
		};
	}

	@Override
	protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
		// 1) auto-BVH: build once if requested
		ensureBVHAutoBuilt();

		// 2) manual BVH (group-level pruning by this node's AABB)
		var cfg = scene.Scene.currentConfig();
		if (cfg != null && cfg.enableBVHManual()) {
			primitives.AABB box = (_aabb != null) ? _aabb : (_aabb = computeBoundingBox());
			if (box != null && !box.hit(ray, Double.POSITIVE_INFINITY))
				return null;
		}

		// 3) descend to children
		List<Intersection> hits = null;
		for (Intersectable ch : _geometries) {
			var lst = ch.calculateIntersections(ray);
			if (lst != null) {
				if (hits == null)
					hits = new LinkedList<>(lst);
				else
					hits.addAll(lst);
			}
		}
		return hits;
	}

	// ---------- AABB for a group (union of children) ----------
	@Override
	protected primitives.AABB computeBoundingBox() {
		if (_geometries.isEmpty())
			return null;
		primitives.AABB acc = null;
		for (Intersectable g : _geometries) {
			primitives.AABB child = (g._aabb != null) ? g._aabb : (g._aabb = g.computeBoundingBox());
			if (child != null)
				acc = primitives.AABB.union(acc, child);
		}
		return acc; // may be null if all children are infinite
	}
}