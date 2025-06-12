package renderer;

import static primitives.Util.*;

import java.util.MissingResourceException;

import primitives.*;
import scene.Scene;

/**
 * Camera class represents a pinhole camera model used to generate rays through
 * a view plane. This class is part of the scene rendering process.
 * 
 * Implements Cloneable as required by the design pattern instructions. All
 * fields are private and the class includes only a default constructor.
 */
public class Camera implements Cloneable {

	/** Image writer for outputting the rendered image. */
	private ImageWriter imageWriter;

	/** Ray tracer engine used to trace rays and compute pixel colors. */
	private RayTracerBase rayTracer;

	/** Number of horizontal pixels */
	private int _nX = 1;

	/** Number of vertical pixels */
	private int _nY = 1;

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
		 * Initializes a new Builder with a fresh Camera instance.
		 */
		public Builder() {
			_camera = new Camera();
		}

		/**
		 * Initializes a new Builder with the given Camera instance.
		 *
		 * @param camera the Camera to initialize the Builder with
		 */

		public Builder(Camera camera) {
			_camera = camera;
		}

		/**
		 * Sets the camera's location point.
		 *
		 * @param p0 the location of the camera
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if the point is null
		 */

		public Builder setLocation(Point p0) {
			if (p0 == null)
				throw new IllegalArgumentException("Camera location point cannot be null.");
			_camera._p0 = p0;
			return this;
		}

		/**
		 * Sets the camera's direction vectors explicitly using orthogonal vTo and vUp
		 * vectors.
		 *
		 * @param vTo the forward direction vector
		 * @param vUp the upward direction vector (must be orthogonal to vTo)
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if either vector is null or not orthogonal
		 */

		public Builder setDirection(Vector vTo, Vector vUp) {
			if (vTo == null || vUp == null)
				throw new IllegalArgumentException("Camera direction vectors cannot be null.");
			if (!Util.isZero(vTo.dotProduct(vUp))) {
				throw new IllegalArgumentException("Camera direction vectors must be orthogonal.");
			}
			_camera._vTo = vTo.normalize();
			_camera._vUp = vUp.normalize();
			_camera._vRight = _camera._vTo.crossProduct(_camera._vUp);
			_camera._vUp = _camera._vRight.crossProduct(_camera._vTo);
			return this;
		}

		/**
		 * Sets the camera's viewing direction and up vector based on a target point and
		 * an up-guess vector.
		 *
		 * @param target  the point the camera should look at
		 * @param upGuess a vector approximating the up direction (must not be parallel
		 *                to the view direction)
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if any argument is null, or if the direction
		 *                                  vectors are invalid
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
		 * Sets the camera's direction using a target point, assuming a default up
		 * direction (0, 1, 0).
		 *
		 * @param target the point the camera should look at
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if the target is null or invalid
		 */

		public Builder setDirection(Point target) {
			return setDirection(target, new Vector(0, 1, 0));
		}

		/**
		 * Sets the size of the view plane.
		 *
		 * @param width  the width of the view plane (must be positive)
		 * @param height the height of the view plane (must be positive)
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if width or height is not positive
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
		 * @param distance the view plane distance (must be positive)
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if distance is not positive
		 */

		public Builder setVpDistance(double distance) {
			if (distance <= 0)
				throw new IllegalArgumentException("View plane distance must be positive.");
			_camera._distance = distance;
			return this;
		}

		/**
		 * Set the image writer
		 * 
		 * @param imageWriter the image writer
		 * @return the camera builder
		 */
		public Builder setImageWriter(ImageWriter imageWriter) {
			_camera.imageWriter = imageWriter;
			return this;
		}

		/**
		 * Sets the resolution of the image by specifying the number of pixels in each
		 * dimension.
		 *
		 * @param nX number of horizontal pixels (must be positive)
		 * @param nY number of vertical pixels (must be positive)
		 * @return the updated Builder instance
		 * @throws IllegalArgumentException if nX or nY is not positive
		 */

		public Builder setResolution(int nX, int nY) {
			if (nX <= 0 || nY <= 0)
				throw new IllegalArgumentException("Resolution values must be positive.");
			_camera._nX = nX;
			_camera._nY = nY;
			return this;
		}

		/**
		 * Sets the ray tracer type for the camera based on the given scene and type.
		 *
		 * @param scene the scene to use for ray tracing
		 * @param type  the type of ray tracer to apply
		 * @return the updated Builder instance
		 */

		public Builder setRayTracer(Scene scene, RayTracerType type) {
			if (type == RayTracerType.SIMPLE) {
				_camera.rayTracer = new SimpleRayTracer(scene);
			} else {
				_camera.rayTracer = null;
			}
			return this;
		}

		/**
		 * Builds and returns the fully configured Camera object. Validates that all
		 * required parameters are set before construction.
		 *
		 * @return the constructed Camera instance
		 * @throws MissingResourceException if any mandatory camera component is missing
		 * @throws IllegalArgumentException if the image resolution is invalid
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
			if (alignZero(_camera._width) <= 0 || _camera._height == 0)
				throw new MissingResourceException(MISSING, CLASS_NAME, "View plane size");
			if (_camera._nX <= 0 || _camera._nY <= 0)
				throw new IllegalArgumentException("Image resolution must be positive.");
			if (_camera.imageWriter == null)
				_camera.imageWriter = new ImageWriter(_camera._nX, _camera._nY);
			if (_camera.rayTracer == null)
				_camera.rayTracer = new SimpleRayTracer(null);

			_camera._vRight = _camera._vTo.crossProduct(_camera._vUp).normalize();
			return _camera.clone();
		}
	}

	/**
	 * Returns a new Builder instance for constructing a Camera.
	 *
	 * @return a new Builder object
	 */

	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * Constructs a ray from the camera through a specific pixel on the view plane.
	 *
	 * @param nX the number of pixels in the X axis (columns)
	 * @param nY the number of pixels in the Y axis (rows)
	 * @param j  the index of the column (X-axis) of the pixel
	 * @param i  the index of the row (Y-axis) of the pixel
	 * @return the constructed ray from the camera through the specified pixel
	 */

	public Ray constructRay(int nX, int nY, int j, int i) {
		Point pIJ = _p0;
		double yI = -(i - (nY - 1) / 2d) * _height / nY;
		double xJ = (j - (nX - 1) / 2d) * _width / nX;

		// check if xJ or yI are not zero, so we will not add zero vector
		if (!isZero(xJ))
			pIJ = pIJ.add(_vRight.scale(xJ));
		if (!isZero(yI))
			pIJ = pIJ.add(_vUp.scale(yI));

		// we need to move the point in the direction of vTo by distance
		pIJ = pIJ.add(_vTo.scale(_distance));

		return new Ray(_p0, pIJ.subtract(_p0).normalize());
	}

	/**
	 * Casts a ray through the center of a specific pixel, traces its color, and
	 * writes the result into the image.
	 *
	 * @param i the column index (X)
	 * @param j the row index (Y)
	 */
	private void castRay(int i, int j) {
		Ray ray = constructRay(_nX, _nY, j, i);
		Color color = rayTracer.traceRay(ray);
		imageWriter.writePixel(j, i, color);
	}

	/**
	 * Render the image based on scene and ray tracing.
	 *
	 * @return this camera instance
	 */

	public Camera renderImage() {
		for (int j = 0; j < _nY; j++)
			for (int i = 0; i < _nX; i++)
				castRay(i, j);
		return this;
	}

	/**
	 * Draw a grid on the image with the given color and spacing.
	 * 
	 * @param interval the spacing between grid lines
	 * @param color    the color of the grid lines
	 * @return this camera instance
	 */
	public Camera printGrid(int interval, Color color) {
		for (int j = 0; j < _nY; j++)
			for (int i = 0; i < _nX; i++)
				if (j % interval == 0 || i % interval == 0)
					imageWriter.writePixel(i, j, color);
		return this;
	}

	/**
	 * Writes the rendered image to disk.
	 * 
	 * @param filename the name of the image file (without extension)
	 * @return this camera instance
	 */
	public Camera writeToImage(String filename) {
		imageWriter.writeToImage(filename);
		return this;
	}
}
