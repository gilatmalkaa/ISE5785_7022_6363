package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
public class RenderTests {
	/** Default constructor to satisfy JavaDoc generator */
	public RenderTests() {
		/* to satisfy JavaDoc generator */ }

	/** Camera builder of the tests */
	private final Camera.Builder camera = Camera.getBuilder() //
			.setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), Vector.AXIS_Y) //
			.setVpDistance(100) //
			.setVpSize(500, 500);

	/**
	 * Produce a scene with basic 3D model and render it into a png image with a
	 * grid
	 */
	@Test
	public void renderTwoColorTest() {
		Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191), Double3.ONE));
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

	// For stage 6 - please disregard in stage 5
	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	@Test
	public void renderMultiColorTest() {
		Scene scene = new Scene("Multi color")..setAmbientLight(new AmbientLight(new Color(WHITE), Double3.ONE)); // הוספת
																														// מקדם
																														// תאורה
																														// סביבתית

		// הוספת גופים עם חומרים מעודכנים
		scene.geometries.add(
				new Sphere(50d, new Point(0, 0, -100))
						.setMaterial(new Material().setKa(0.4).setKd(0.5).setKs(0.3).setShininess(20)), // הגדרת חומר
																										// לכדור
				new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
						.setMaterial(new Material().setKa(new Double3(0, 0.8, 0))), // הגדרת
																													// חומר
																													// למשולש
																													// הירוק
				new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
						.setMaterial(new Material().setKa(new Double3(0.8, 0, 0))), // הגדרת
																												// חומר
																												// למשולש
																												// האדום
				new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
						.setMaterial(new Material().setKa(new Double3(0, 0, 0.8))) // הגדרת
																												// חומר
																												// למשולש
																												// הכחול
		);

		// הוספת מקורות אור
		scene.addLight(new DirectionalLight(new Color(500, 300, 300), new Vector(1, -1, -1))); // אור כיווני
		scene.addLight(
				new PointLight(new Color(500, 300, 300), new Point(50, 50, -50)).setKc(1).setKl(0.1).setKq(0.01)); // אור
																													// נקודתי
																													// עם
																													// מקדמי
																													// הנחתה

		// יצירת התמונה
		camera.setRayTracer(scene, RayTracerType.SIMPLE).setResolution(1000, 1000).build().renderImage()
				.printGrid(100, new Color(WHITE)).writeToImage("color render test");
	}

	/** Test for XML based scene - for bonus */
	@Test
	public void basicRenderXml() {
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
	public void basicRenderJson() {
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
