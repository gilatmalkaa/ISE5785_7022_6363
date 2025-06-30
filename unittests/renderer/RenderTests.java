package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
class RenderTests {
	/** Default constructor to satisfy JavaDoc generator */
	RenderTests() {
		/* to satisfy JavaDoc generator */ }

	/** Camera builder of the tests */
	private final Camera.Builder camera = Camera.getBuilder() //
			.setLocation(Point.ZERO).setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
			.setVpDistance(100) //
			.setVpSize(500, 500);

	/**
	 * Produce a scene with basic 3D model and render it into a png image with a
	 * grid
	 */
	@Test
	void renderTwoColorTest() {
		Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191)));
		scene.geometries //
				.add(// center
						new Sphere(50d, new Point(0, 0, -100)),
						// up left
						new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
						// down left
						new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
						// down right
						new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(YELLOW)) //
				.writeToImage("Two color render test");
	}

	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	@Test
	void renderMultiColorTest() {
		Scene scene = new Scene("Multi color").setAmbientLight(new AmbientLight(new Color(51, 51, 51)));
		scene.geometries //
				.add(// center
						new Sphere(50, new Point(0, 0, -100)),
						// up left
						new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)) //
								.setEmission(new Color(GREEN)),
						// down left
						new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)) //
								.setEmission(new Color(RED)),
						// down right
						new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)) //
								.setEmission(new Color(BLUE)));

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(WHITE)) //
				.writeToImage("color render test");
	}

	/**
	 * Unit test for rendering geometries with only ambient materials and no
	 * emission color.
	 * <p>
	 * This test verifies that the ambient reflection (kA) values of the materials
	 * affect the base color under strong white ambient light. The geometries have
	 * no emission color set.
	 * </p>
	 */
	@Test
	void renderMultiColorTestWithAmbientMaterials() {
		// Create a new scene with strong white ambient light
		Scene scene = new Scene("Multi color with ambient materials")
				.setAmbientLight(new AmbientLight(new Color(WHITE))); // strong white ambient light

		// Add geometries without emission but with ambient reflection materials (kA)
		scene.geometries.add(
				// Triangle up left with green ambient reflection component (0, 0.8, 0)
				new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
						.setMaterial(new Material().setKA(new Double3(0, 0.8, 0))),
				// Triangle down left with red ambient reflection component (0.8, 0, 0)
				new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
						.setMaterial(new Material().setKA(new Double3(0.8, 0, 0))),
				// Sphere with ambient reflection coefficient 0.4 (greyish)
				new Sphere(50, new Point(0, 0, -100)).setMaterial(new Material().setKA(new Double3(0.4))),
				// Triangle down right with blue ambient reflection component (0, 0, 0.8)
				new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
						.setMaterial(new Material().setKA(new Double3(0, 0, 0.8))));

		// Set up the camera, resolution and ray tracer then render the image
		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(1000, 1000).build().renderImage()
				.printGrid(100, new Color(WHITE)) // Print grid with white lines every 100 pixels
				.writeToImage("multi_color_ambient_materials_test"); // Save output image file
	}

	/** Test for XML based scene - for bonus */
	@Test
	void basicRenderXml() {
		Scene scene = new Scene("Using XML");
		// enter XML file name and parse from XML file into scene object instead of the
		// new Scene above,
		// Use the code you added in appropriate packages
		// ...
		// NB: unit tests is not the correct place to put XML parsing code

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(YELLOW)) //
				.writeToImage("xml render test");
	}

	/** Test for JSON based scene - for bonus */
	@Test
	void basicRenderJson() {
		Scene scene = new Scene("Using Json");
		// enter XML file name and parse from JSON file into scene object instead of the
		// new Scene above,
		// Use the code you added in appropriate packages
		// ...
		// NB: unit tests is not the correct place to put XML parsing code

		camera //
				.setRayTracer(scene, RayTracerType.SIMPLE) //
				.setResolution(1000, 1000) //
				.build() //
				.renderImage() //
				.printGrid(100, new Color(YELLOW)) //
				.writeToImage("xml render test");
	}

}