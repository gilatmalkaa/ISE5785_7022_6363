package renderer;

import org.junit.jupiter.api.Test;

import primitives.Color;

/**
 * Unit test for the {@link ImageWriter} class. This test creates a 800x500
 * image with a colored background and a red grid.
 */
public class ImageWriterTest {

	@Test
	void testWriteImageWithGrid() {
		int width = 800;
		int height = 500;
		ImageWriter imageWriter = new ImageWriter(width, height);

		Color background = new Color(173, 216, 230); // light blue
		Color gridColor = new Color(255, 0, 0); // red

		int gridStepX = width / 16; // 50
		int gridStepY = height / 10; // 50

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Draw grid lines
				if (x % gridStepX == 0 || y % gridStepY == 0) {
					imageWriter.writePixel(x, y, gridColor);
				} else {
					imageWriter.writePixel(x, y, background);
				}
			}
		}

		// Save the image as a PNG file in the /images directory
		imageWriter.writeToImage("grid_test_image");
	}
}
