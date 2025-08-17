package renderer;

import static java.lang.Math.random;
import static primitives.Util.isZero;

import java.util.LinkedList;
import java.util.List;

import primitives.Point;
import primitives.Vector;

/**
 * Generates jittered sample points in a square area .
 */
public class JitterSampler {
	/**
	 * The center point of the sampling area (e.g., pixel or light source area).
	 */
	private final Point center;

	/**
	 * The rightward direction vector defining the local horizontal axis of the
	 * sampling area.
	 */
	private final Vector vRight;

	/**
	 * The upward direction vector defining the local vertical axis of the sampling
	 * area.
	 */
	private final Vector vUp;

	/**
	 * The size of a single sub-pixel or sub-area sample (step size between
	 * samples).
	 */
	private final double pixelSize;

	/**
	 * The number of samples to take per side (total samples = samplesPerSide ×
	 * samplesPerSide).
	 */
	private final int samplesPerSide;

	/**
	 * Constructs a jitter sampler with given area and sampling settings.
	 *
	 * @param center         the center of the sampling area
	 * @param vRight         the rightward direction vector
	 * @param vUp            the upward direction vector
	 * @param pixelSize      the width/height of the sampling square
	 * @param samplesPerSide number of jittered samples per side (grid is N×N)
	 */
	public JitterSampler(Point center, Vector vRight, Vector vUp, double pixelSize, int samplesPerSide) {
		this.center = center;
		this.vRight = vRight;
		this.vUp = vUp;
		this.pixelSize = pixelSize;
		this.samplesPerSide = samplesPerSide;
	}

	/**
	 * Generates a grid of jittered sample points within the sampling area. Each
	 * point is randomly offset inside its grid cell to reduce aliasing.
	 *
	 * @return list of jittered sample points
	 */
	public List<Point> getJitteredPoints() {
		List<Point> points = new LinkedList<>();
		double subPixelSize = pixelSize / samplesPerSide;

		for (int i = 0; i < samplesPerSide; i++) {
			for (int j = 0; j < samplesPerSide; j++) {
				double y = -(i - (samplesPerSide - 1.0) / 2.0 + random() - 0.5) * subPixelSize;
				double x = (j - (samplesPerSide - 1.0) / 2.0 + random() - 0.5) * subPixelSize;

				Point p = center;
				if (!isZero(x))
					p = p.add(vRight.scale(x));
				if (!isZero(y))
					p = p.add(vUp.scale(y));

				points.add(p);
			}
		}
		return points;
	}
}