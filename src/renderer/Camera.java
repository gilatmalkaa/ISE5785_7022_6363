package renderer;

import static primitives.Util.isZero;

import java.util.MissingResourceException;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * Camera class represents a pinhole camera model used to generate rays through
 * a view plane. This class is part of the scene rendering process.
 * 
 * Implements Cloneable as required by the design pattern instructions. All
 * fields are private and the class includes only a default constructor.
 */
public class Camera implements Cloneable {

	/** Number of horizontal pixels */
	private int _nX = 0;

	/** Number of vertical pixels */
	private int _nY = 0;

	/** Width of a single pixel (calculated) */
	private double _rX = 0.0;

	/** Height of a single pixel (calculated) */
	private double _rY = 0.0;

	/** Camera location in 3D space */
	private Point _p0;

	/** Forward direction vector */
	private Vector _vTo;

	/** Upward direction vector */
	private Vector _vUp;

	/** Rightward direction vector */
	private Vector _vRight;

	/** Width of the view plane */
	private double _width = 0.0;

	/** Height of the view plane */
	private double _height = 0.0;

	/** Distance from the camera to the view plane */
	private double _distance = 0.0;

	/**
	 * Private default constructor for internal use. No other constructors are
	 * allowed according to project instructions.
	 */
	private Camera() {
	}

	/**
	 * Creates a shallow copy of the Camera object using Java's built-in clone.
	 *
	 * @return a cloned Camera object
	 */
	@Override
	public Camera clone() {
		try {
			return (Camera) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Cloning failed, even though we implement Cloneable", e);
		}
	}

	/**
	 * Builder class for constructing Camera objects step-by-step.
	 */
	public static class Builder {

		/** Internal Camera instance being configured by the Builder. */
		private final Camera _camera;

		/**
		 * Default constructor. Initializes a new empty Camera object.
		 */
		public Builder() {

			_camera = new Camera();
		}

		/**
		 * Constructor that wraps an existing Camera object.
		 *
		 * @param camera the camera to wrap in this builder
		 */
		public Builder(Camera camera) {
			_camera = camera;
		}

		/**
		 * Sets the camera location.
		 *
		 * @param p0 the camera position
		 * @return this builder instance
		 */
		public Builder setLocation(Point p0) {
			if (p0 == null)
				throw new IllegalArgumentException("Camera location point cannot be null.");
			_camera._p0 = p0;
			return this;
		}

		/**
		 * Sets the camera orientation using orthogonal vectors.
		 *
		 * @param vTo forward vector
		 * @param vUp upward vector
		 * @return this builder instance
		 */
		public Builder setDirection(Vector vTo, Vector vUp) {
			if (vTo == null || vUp == null)
				throw new IllegalArgumentException("Camera direction vectors cannot be null.");
			if (!Util.isZero(vTo.dotProduct(vUp))) {
				throw new IllegalArgumentException(
						"Camera direction vectors must be orthogonal (dot product must be zero).");
			}
			_camera._vTo = vTo.normalize();
			_camera._vUp = vUp.normalize();
			_camera._vRight = _camera._vTo.crossProduct(_camera._vUp);
			_camera._vUp = _camera._vRight.crossProduct(_camera._vTo);
			return this;
		}

		/**
		 * Sets the camera direction using a target point and an upward vector.
		 *
		 * @param target  point the camera looks at
		 * @param upGuess a guess for the upward direction
		 * @return this builder instance
		 */
		public Builder setDirection(Point target, Vector upGuess) {
			if (target == null || upGuess == null)
				throw new IllegalArgumentException("Target point and upGuess vector cannot be null.");
			if (_camera._p0 == null)
				throw new IllegalArgumentException("Camera location (p0) must be set before direction.");
			Vector vTo = target.subtract(_camera._p0);
			if (isZero(vTo.length()))
				throw new IllegalArgumentException("Camera target must not be equal to its location.");
			_camera._vTo = vTo.normalize();
			Vector vRight = _camera._vTo.crossProduct(upGuess);
			if (isZero(vRight.length()))
				throw new IllegalArgumentException("vTo and upGuess must not be parallel.");
			_camera._vRight = vRight.normalize();
			_camera._vUp = _camera._vRight.crossProduct(_camera._vTo);
			return this;
		}

		/**
		 * Sets the direction using a target point, with default up as Y axis.
		 *
		 * @param target the point the camera looks at
		 * @return this builder instance
		 */
		public Builder setDirection(Point target) {
			return setDirection(target, new Vector(0, 1, 0));
		}

		/**
		 * Sets the physical size of the view plane.
		 *
		 * @param width  the width
		 * @param height the height
		 * @return this builder instance
		 */
		public Builder setVpSize(double width, double height) {
			if (width <= 0 || height <= 0)
				throw new IllegalArgumentException("View plane width and height must be positive.");
			_camera._width = width;
			_camera._height = height;
			return this;
		}

		/**
		 * Sets the distance from the camera to the view plane.
		 *
		 * @param distance positive distance
		 * @return this builder instance
		 */
		public Builder setVpDistance(double distance) {
			if (distance <= 0)
				throw new IllegalArgumentException("View plane distance must be positive.");
			_camera._distance = distance;
			return this;
		}

		/**
		 * Placeholder method for future resolution configuration.
		 *
		 * @param nX horizontal resolution
		 * @param nY vertical resolution
		 * @return this builder instance
		 */
		public Builder setResolution(int nX, int nY) {
			return this;
		}

		/**
		 * Finalizes the building process of the Camera object. This method ensures that
		 * all required fields are properly set. If any critical rendering data is
		 * missing, a MissingResourceException is thrown. If validation succeeds, the
		 * method calculates the missing direction vector (vRight), and returns a clone
		 * of the fully configured Camera.
		 *
		 * @return a clone of the configured Camera object
		 * @throws MissingResourceException if any critical rendering data is missing
		 */
		public Camera build() {
			final String MISSING = "Missing rendering data";
			final String CLASS_NAME = "Camera";

			if (_camera._p0 == null)
				throw new MissingResourceException(MISSING, CLASS_NAME, "Camera position (p0)");
			if (_camera._vTo == null)
				throw new MissingResourceException(MISSING, CLASS_NAME, "Forward direction (vTo)");
			if (_camera._vUp == null)
				throw new MissingResourceException(MISSING, CLASS_NAME, "Up direction (vUp)");
			if (_camera._distance == 0)
				throw new MissingResourceException(MISSING, CLASS_NAME, "View plane distance");
			if (_camera._width == 0 || _camera._height == 0)
				throw new MissingResourceException(MISSING, CLASS_NAME, "View plane size");

			_camera._vRight = _camera._vTo.crossProduct(_camera._vUp).normalize();
			return _camera.clone();
		}
	}

	/**
	 * Returns a new instance of the Builder for constructing a Camera.
	 *
	 * @return a new Builder instance
	 */
	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Constructs a ray from the camera through a specific pixel on the view plane.
	 *
	 * @param nX Number of columns (pixels in width)
	 * @param nY Number of rows (pixels in height)
	 * @param j  Column index of the pixel (0-based from left to right)
	 * @param i  Row index of the pixel (0-based from top to bottom)
	 * @return Ray from the camera through the specified pixel
	 */
	public Ray constructRay(int nX, int nY, int j, int i) {
		Point pc = _p0.add(_vTo.scale(_distance));
		double rX = _width / nX;
		double rY = _height / nY;
		double xj = (j - (nX - 1) / 2.0) * rX;
		double yi = -(i - (nY - 1) / 2.0) * rY;
		Point pij = pc;
		if (!isZero(xj)) {
			pij = pij.add(_vRight.scale(xj));
		}
		if (!isZero(yi)) {
			pij = pij.add(_vUp.scale(yi));
		}
		return new Ray(_p0, pij.subtract(_p0));
	}
}
