package renderer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Testing Camera Class
 * 
 * @author Dan
 */
class CameraTest {
	/**
	 * Explicit default constructor to satisfy JavaDoc generator.
	 */
	public CameraTest() {
	}

	/** Camera builder for the tests */
	private final Camera.Builder cameraBuilder = Camera.getBuilder()
			.setRayTracer(new Scene("Test"), RayTracerType.SIMPLE).setImageWriter(new ImageWriter(1, 1))
			.setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0)).setVpDistance(10);

	/**
	 * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
	 */
	@Test
	void testConstructRay() {
		final String badRay = "Bad ray";

		// ============ Equivalence Partitions Tests ==============
		// EP01: 4X4 Inside (1,1)
		Camera camera1 = cameraBuilder.setVpSize(8, 8).build();
		assertEquals(new Ray(Point.ZERO, new Vector(1, -1, -10)), camera1.constructRay(4, 4, 1, 1), badRay);

		// =============== Boundary Values Tests ==================
		// BV01: 4X4 Corner (0,0)
		assertEquals(new Ray(Point.ZERO, new Vector(3, -3, -10)), camera1.constructRay(4, 4, 0, 0), badRay);

		// BV02: 4X4 Side (0,1)
		assertEquals(new Ray(Point.ZERO, new Vector(1, -3, -10)), camera1.constructRay(4, 4, 1, 0), badRay);

		// BV03: 3X3 Center (1,1)
		Camera camera2 = cameraBuilder.setVpSize(6, 6).build();
		assertEquals(new Ray(Point.ZERO, new Vector(0, 0, -10)), camera2.constructRay(3, 3, 1, 1), badRay);

		// BV04: 3X3 Center of Upper Side (0,1)
		assertEquals(new Ray(Point.ZERO, new Vector(0, -2, -10)), camera2.constructRay(3, 3, 1, 0), badRay);

		// BV05: 3X3 Center of Left Side (1,0)
		assertEquals(new Ray(Point.ZERO, new Vector(2, 0, -10)), camera2.constructRay(3, 3, 0, 1), badRay);

		// BV06: 3X3 Corner (0,0)
		assertEquals(new Ray(Point.ZERO, new Vector(2, -2, -10)), camera2.constructRay(3, 3, 0, 0), badRay);

	}

	/**
	 * Test method for {@link renderer.Camera#renderImage()} with anti-aliasing
	 * enabled using jittered sampling.
	 * <p>
	 * <b>EP:</b> Creating a camera with valid parameters and enabling anti-aliasing
	 * with 4 rays per pixel should succeed without throwing.
	 * </p>
	 * <p>
	 * The purpose of this test is to verify that the method {@code renderImage}
	 * does not throw any exception when anti-aliasing with jittered sampling is
	 * used.
	 * </p>
	 */
	@Test
	void testCastRayWithJitteredSampling_doesNotThrow() {
		var builder = Camera.getBuilder().setLocation(new Point(0, 0, 200))
				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(3, 3).setVpDistance(200)
				.setResolution(200, 200).setRayTracer(new Scene("Test"), RayTracerType.SIMPLE).setRaysPerPixelAA(4);

		assertDoesNotThrow(() -> builder.build().renderImage(),
				"Anti-aliasing with jitter sampling threw an exception");
	}

	/**
	 * Renders a “rich” scene twice—first without anti-aliasing and then with
	 * anti-aliasing—and records the render times.
	 *
	 * <p>
	 * <b>Purpose:</b> Compliance test for Mini-Project 1 (AA):
	 * <ul>
	 * <li>Scene contains ≥10 geometries and ≥3 light sources.</li>
	 * <li>Runs <i>OFF</i> (raysPerPixelAA = 1) and <i>ON</i> (raysPerPixelAA = 81 ≈
	 * 9×9).</li>
	 * <li>Writes two output images: {@code AA_OFF_rich_scene.png} and
	 * {@code AA_ON_rich_scene.png}.</li>
	 * <li>Prints elapsed time (ms) for each run using
	 * {@code System.nanoTime()}.</li>
	 * </ul>
	 *
	 * <p>
	 * <b>Expected result:</b> The AA-ON image should exhibit smoother edges
	 * (reduced stair-stepping) relative to AA-OFF, at the cost of longer render
	 * time.
	 *
	 * <p>
	 * <b>Notes:</b>
	 * <ul>
	 * <li>{@code ImageWriter} is created by the {@code Camera.Builder} using the
	 * configured resolution.</li>
	 * <li>File names are provided via {@code writeToImage(...)}.</li>
	 * <li>Keep constructors consistent (e.g.,
	 * {@code Sphere(double radius, Point center)}).</li>
	 * </ul>
	 */
	@Test
	void aa_on_off_richScene_and_timing() {
		Scene scene = new Scene("rich-AA-scene").setBackground(new Color(20, 25, 30))
				.setAmbientLight(new AmbientLight(new Color(60, 60, 60)));

		Material mMatte = new Material().setKD(0.9).setKS(0.1).setShininess(64);
		Material mGloss = new Material().setKD(0.5).setKS(0.7).setShininess(300);
		Material mMirror = new Material().setKR(0.7).setKS(0.5).setShininess(400);
		Material mGlass = new Material().setKT(0.7).setKS(0.3).setShininess(300).setKR(0.1);

		scene.geometries.add(new Geometries(
				new Plane(new Point(0, -50, 0), new Vector(0, 1, 0)).setEmission(new Color(120, 120, 120))
						.setMaterial(mMatte),
				new Plane(new Point(0, 0, -400), new Vector(0, 0, 1)).setEmission(new Color(15, 15, 18))
						.setMaterial(mMatte),

				new Sphere(30, new Point(-80, -20, -200)).setEmission(new Color(220, 40, 40)).setMaterial(mGloss),
				new Sphere(20, new Point(-25, -10, -180)).setEmission(new Color(40, 180, 80)).setMaterial(mGloss),
				new Sphere(20, new Point(25, -10, -180)).setEmission(new Color(40, 80, 220)).setMaterial(mGloss),
				new Sphere(30, new Point(80, -20, -200)).setEmission(new Color(240, 220, 80)).setMaterial(mGloss),

				new Sphere(25, new Point(0, -25, -240)).setEmission(new Color(220, 220, 230)).setMaterial(mGlass),
				new Sphere(12, new Point(0, 5, -120)).setEmission(new Color(255, 255, 255)).setMaterial(mMirror),

				new Triangle(new Point(-15, -25, -150), new Point(15, -25, -150), new Point(0, -5, -130))
						.setEmission(new Color(160, 60, 200)).setMaterial(mMatte),
				new Triangle(new Point(-15, -25, -150), new Point(0, -5, -130), new Point(-30, -25, -130))
						.setEmission(new Color(60, 200, 200)).setMaterial(mMatte),
				new Triangle(new Point(15, -25, -150), new Point(30, -25, -130), new Point(0, -5, -130))
						.setEmission(new Color(250, 140, 60)).setMaterial(mMatte)));

		scene.lights.add(new DirectionalLight(new Color(300, 280, 260), new Vector(-1, -1, -2)));
		scene.lights.add(new PointLight(new Color(400, 350, 300), new Point(0, 50, -160)).setKl(0.0008).setKq(0.0002));
		scene.lights.add(new SpotLight(new Color(500, 450, 420), new Point(-120, 80, -120), new Vector(1, -1, -1))
				.setKl(0.0005).setKq(0.0002).setNarrowBeam(20));

		var builder = Camera.getBuilder().setLocation(new Point(0, 0, 200))
				.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(200, 200).setVpDistance(200)
				.setResolution(800, 800).setRayTracer(scene, RayTracerType.SIMPLE);
		// OFF
		long t0 = System.nanoTime();
		builder.setRaysPerPixelAA(1).build().renderImage().writeToImage("AA_OFF_rich_scene");
		long t1 = System.nanoTime();
		System.out.println("AA-OFF ms = " + (t1 - t0) / 1_000_000);

		// ON (81 ≈ 9x9)
		long t2 = System.nanoTime();
		builder.setRaysPerPixelAA(81).build().renderImage().writeToImage("AA_ON_rich_scene");
		long t3 = System.nanoTime();
		System.out.println("AA-ON  ms = " + (t3 - t2) / 1_000_000);
	}
}