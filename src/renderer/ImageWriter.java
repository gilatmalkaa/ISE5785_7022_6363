package renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import primitives.Color;

/**
 * ImageWriter class combines accumulation of pixel color matrix and finally
 * produces a non-optimized PNG image from this matrix.
 * <p>
 * The class is responsible for holding image-related parameters of the view
 * plane such as resolution and pixel matrix size.
 * </p>
 * 
 * @author Dan
 */
final class ImageWriter {
	/** Horizontal resolution of the image - number of pixels in row */
	private final int nX;
	/** Vertical resolution of the image - number of pixels in column */
	private final int nY;

	/**
	 * Directory path for the image file generation - relative to the user directory
	 */
	private static final String FOLDER_PATH = System.getProperty("user.dir") + "/images";

	/** Image generation buffer (the matrix of the pixels) */
	private final BufferedImage image;

	// ***************** Constructors ********************** //

	/**
	 * Constructs an ImageWriter with the specified resolution.
	 * 
	 * @param nX number of horizontal pixels (width)
	 * @param nY number of vertical pixels (height)
	 */
	ImageWriter(int nX, int nY) {
		this.nX = nX;
		this.nY = nY;
		image = new BufferedImage(nX, nY, BufferedImage.TYPE_INT_RGB);
	}

	// ***************** Getters ********************** //

	/**
	 * Returns the number of vertical pixels (height).
	 * 
	 * @return the vertical resolution of the image
	 */
	int nY() {
		return nY;
	}

	/**
	 * Returns the number of horizontal pixels (width).
	 * 
	 * @return the horizontal resolution of the image
	 */
	int nX() {
		return nX;
	}

	// ***************** Operations ******************** //

	/**
	 * Writes the buffered image to a PNG file in the predefined directory.
	 * 
	 * @param imageName the name of the PNG file (without extension)
	 * @throws IllegalStateException if the image could not be written
	 */
	void writeToImage(String imageName) {
		try {
			File file = new File(FOLDER_PATH + '/' + imageName + ".png");
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			throw new IllegalStateException("I/O error - may be missing directory " + FOLDER_PATH, e);
		}
	}

	/**
	 * Writes a single pixel's color to the image buffer.
	 * 
	 * @param xIndex X axis index of the pixel
	 * @param yIndex Y axis index of the pixel
	 * @param color  final color of the pixel
	 */
	void writePixel(int xIndex, int yIndex, Color color) {
		image.setRGB(xIndex, nY - 1 - yIndex, color.getColor().getRGB());
	}
}