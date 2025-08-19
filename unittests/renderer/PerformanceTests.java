package renderer;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.AccelConfig;
import scene.Scene;

/**
 * Performance tests for MP2 (CBR and BVH). Runs heavy scenes with different
 * acceleration configs and prints render times.
 */
public class PerformanceTests {

	/**
	 * Default constructor for PerformanceTests. JUnit uses this constructor to
	 * create the test class.
	 */
	public PerformanceTests() {
		// no setup needed
	}

	/**
	 * Stage 1 – CBR ON vs OFF on the same scene. Goal: show that AABB early‐reject
	 * (CBR) makes the render faster, while the image stays the same. Steps: run
	 * once with CBR=false, then with CBR=true; save images and print ms.
	 */
	@Test
	void stage1_CBR_on_off_same_scene() {
		Scene scene = new Scene("MP2-CBR");
		buildHeavyScene(scene); // many geometries + 5+ lights

		// === CBR OFF ===
		scene.setConfig(new AccelConfig().setCBR(false));
		long t0 = System.nanoTime();
		Camera camOff = Camera.getBuilder().setLocation(new Point(0, 60, 250)) // camera position
				.setDirection(new Point(0, 0, -200)) // look-at target (up defaults to (0,1,0))
				.setVpSize(400, 400) // view plane size
				.setVpDistance(300) // distance to view plane
				.setResolution(800, 800) // image resolution
				.setRayTracer(scene, RayTracerType.SIMPLE) // use your SimpleRayTracer
				.build().renderImage();
		long offMs = (System.nanoTime() - t0) / 1_000_000;
		camOff.writeToImage("mp2_stage1_cbr_off");
		System.out.println("CBR OFF: " + offMs + " ms");

		// === CBR ON ===
		scene.setConfig(new AccelConfig().setCBR(true));
		long t1 = System.nanoTime();
		Camera camOn = Camera.getBuilder().setLocation(new Point(0, 60, 250)).setDirection(new Point(0, 0, -200))
				.setVpSize(400, 400).setVpDistance(300).setResolution(800, 800)
				.setRayTracer(scene, RayTracerType.SIMPLE).build().renderImage();
		long onMs = (System.nanoTime() - t1) / 1_000_000;
		camOn.writeToImage("mp2_stage1_cbr_on");
		System.out.println("CBR ON: " + onMs + " ms");
	}

	/**
	 * Build a heavy flat scene with many geometries and lights. Used to test CBR
	 * ON/OFF.
	 * 
	 * @param scene the scene to configure
	 */
	private static void buildHeavyScene(Scene scene) {
		scene.setAmbientLight(new AmbientLight(new Color(80, 80, 80)));

		// Planes: ground + back wall
		scene.geometries.add(new Plane(new Point(0, -60, 0), new Vector(0, 1, 0)),
				new Plane(new Point(0, 0, -400), new Vector(0, 0, 1)));

		// Grid of spheres (8x6 ≈ 48 spheres)
		double r = 18;
		for (int ix = -7; ix <= 7; ix += 2) {
			for (int iy = -3; iy <= 3; iy++) {
				double x = ix * 28;
				double y = iy * 28 - 20;
				double z = -120 - (Math.abs(ix) % 3) * 15;
				scene.geometries.add(new Sphere(r, new Point(x, y, z)));
			}
		}

		// A small triangle “sculpture”
		Point a = new Point(-120, -20, -150);
		Point b = new Point(-60, 80, -180);
		Point c = new Point(-10, -10, -130);
		Point d = new Point(90, 70, -190);
		Point e = new Point(130, -30, -160);
		scene.geometries.add(new Triangle(a, b, c), new Triangle(c, b, d), new Triangle(c, d, e),
				new Triangle(a, c, e));

		// 5+ lights
		scene.lights.addAll(List.of(new PointLight(new Color(350, 300, 240), new Point(120, 160, 60)),
				new PointLight(new Color(220, 260, 300), new Point(-140, 130, 40)),
				new SpotLight(new Color(500, 400, 300), new Point(0, 180, 120), new Vector(0, -1, -2)),
				new SpotLight(new Color(300, 350, 450), new Point(160, 40, 0), new Vector(-1, 0, -1)),
				new DirectionalLight(new Color(120, 120, 140), new Vector(-1, -1, -1))));

		scene.lights.add(new PointLight(new Color(400, 350, 300), new Point(120, 160, 60)).setKl(0.0004).setKq(0.0002));

		scene.lights.add(new SpotLight(new Color(500, 400, 300), new Point(-120, 140, 80), new Vector(0, -1, -2))
				.setKl(0.0005).setKq(0.0002));

		Sphere sphere = new Sphere(20, new Point(0, 0, -100));
		sphere.setEmission(new Color(30, 30, 60));
		sphere.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80));

		scene.geometries.add(sphere);
	}

	/**
	 * Build a hierarchical scene (manual BVH groups). Used to test BVH Manual
	 * ON/OFF.
	 * 
	 * @param scene the scene to configure
	 */
	private static void buildHeavyScene1(Scene scene) {
		scene.setAmbientLight(new AmbientLight(new Color(80, 80, 80)));
		scene.setBackground(new Color(10, 10, 10));

		// Infinite walls/ground can still be added כרגיל (אין להן AABB ולכן לא מפריע)
		Geometries walls = Geometries.group(new Plane(new Point(0, -60, 0), new Vector(0, 1, 0)),
				new Plane(new Point(0, 0, -400), new Vector(0, 0, 1)));

		// Example: build rows (groups) of spheres
		Geometries row1 = Geometries.group(new Sphere(18, new Point(-100, -20, -120)),
				new Sphere(18, new Point(-72, -20, -120)), new Sphere(18, new Point(-44, -20, -120)));
		Geometries row2 = Geometries.group(new Sphere(18, new Point(44, -20, -120)),
				new Sphere(18, new Point(72, -20, -120)), new Sphere(18, new Point(100, -20, -120)));

		// Higher-level node combining rows + walls
		Geometries bvhRoot = Geometries.group(row1, row2, walls);

		// Use as the scene root (instead of scene.geometries.add(...))
		scene.setGeometries(bvhRoot);

		// Lights can stay as you had them
		scene.lights.add(new PointLight(new Color(350, 300, 240), new Point(120, 160, 60)));
		scene.lights.add(new PointLight(new Color(220, 260, 300), new Point(-140, 130, 40)));
		scene.lights.add(new SpotLight(new Color(500, 400, 300), new Point(0, 180, 120), new Vector(0, -1, -2)));
		scene.lights.add(new SpotLight(new Color(300, 350, 450), new Point(160, 40, 0), new Vector(-1, 0, -1)));
		scene.lights.add(new DirectionalLight(new Color(120, 120, 140), new Vector(-1, -1, -1)));
	}

	/**
	 * Stage 2 – BVH Manual ON vs OFF on the same hierarchical scene. Goal: show
	 * manual grouping prunes more nodes and reduces time. Steps: CBR=true in both;
	 * toggle BVH Manual; save images and print ms.
	 */
	@Test
	void stage2_BVH_manual_on_off() {
		Scene scene = new Scene("MP2-BVH-MANUAL");
		buildHeavyScene1(scene); // the hierarchical scene with groups

		// Keep CBR ON in both runs; toggle only BVH Manual
		var camBuilder = Camera.getBuilder().setLocation(new Point(0, 60, 250)).setDirection(new Point(0, 0, -200))
				.setVpSize(400, 400).setVpDistance(300).setResolution(800, 800)
				.setRayTracer(scene, RayTracerType.SIMPLE);

		// BVH MANUAL OFF (baseline with CBR ON)
		scene.setConfig(new AccelConfig().setCBR(true).setBVHManual(false));
		long t0 = System.nanoTime();
		var camOff = camBuilder.build().renderImage();
		long offMs = (System.nanoTime() - t0) / 1_000_000;
		camOff.writeToImage("mp2_stage2_bvh_manual_off");
		System.out.println("BVH MANUAL OFF: " + offMs + " ms");

		// BVH MANUAL ON (hierarchy pruning at group level)
		scene.setConfig(new AccelConfig().setCBR(true).setBVHManual(true));
		long t1 = System.nanoTime();
		var camOn = camBuilder.build().renderImage();
		long onMs = (System.nanoTime() - t1) / 1_000_000;
		camOn.writeToImage("mp2_stage2_bvh_manual_on");
		System.out.println("BVH MANUAL ON:  " + onMs + " ms");
	}

	/**
	 * Build a flat scene (no manual groups). Used to test BVH Auto ON/OFF.
	 * 
	 * @param scene the scene to configure
	 */
	private static void buildHeavyFlatScene(Scene scene) {
		scene.setAmbientLight(new AmbientLight(new Color(80, 80, 80)));

		scene.setBackground(new Color(10, 10, 10));

		// Two infinite planes (no AABB -> won't be grouped)
		scene.geometries.add(new Plane(new Point(0, -60, 0), new Vector(0, 1, 0)),
				new Plane(new Point(0, 0, -400), new Vector(0, 0, 1)));

		// Grid of many spheres (increase counts if timing is too fast)
		double r = 18;
		for (int ix = -8; ix <= 8; ix += 2) {
			for (int iy = -4; iy <= 4; iy++) {
				double x = ix * 26;
				double y = iy * 26 - 20;
				double z = -120 - (Math.abs(ix) % 3) * 15;
				scene.geometries.add(new Sphere(r, new Point(x, y, z)));
			}
		}

		// Small triangle cluster
		Point a = new Point(-120, -20, -150);
		Point b = new Point(-60, 80, -180);
		Point c = new Point(-10, -10, -130);
		Point d = new Point(90, 70, -190);
		Point e = new Point(130, -30, -160);
		scene.geometries.add(new Triangle(a, b, c), new Triangle(c, b, d), new Triangle(c, d, e),
				new Triangle(a, c, e));

		// 5+ lights
		scene.lights.addAll(List.of(new PointLight(new Color(350, 300, 240), new Point(120, 160, 60)),
				new PointLight(new Color(220, 260, 300), new Point(-140, 130, 40)),
				new SpotLight(new Color(500, 400, 300), new Point(0, 180, 120), new Vector(0, -1, -2)),
				new SpotLight(new Color(300, 350, 450), new Point(160, 40, 0), new Vector(-1, 0, -1)),
				new DirectionalLight(new Color(120, 120, 140), new Vector(-1, -1, -1))));
	}

	/**
	 * Stage 3 – BVH Auto ON vs OFF on the same flat scene. Goal: show Auto BVH
	 * lowers render time without changing the image. Steps: CBR=true, BVH
	 * Manual=false; run with Auto=false and Auto=true; save and print ms.
	 */
	@Test
	void stage3_BVH_auto_on_off_same_scene() {
		Scene scene = new Scene("MP2-BVH-AUTO");
		buildHeavyFlatScene(scene); // no manual groups!

		// Common camera settings
		Camera.Builder camBuilder = Camera.getBuilder().setLocation(new Point(0, 60, 250)) // camera position
				.setDirection(new Point(0, 0, -200)) // look-at (up=(0,1,0))
				.setVpSize(400, 400).setVpDistance(300).setResolution(800, 800)
				.setRayTracer(scene, RayTracerType.SIMPLE);

		// --- Auto BVH OFF (CBR ON, Manual OFF) ---
		scene.setConfig(new AccelConfig().setCBR(true).setBVHManual(false).setBVHAuto(false));
		long t0 = System.nanoTime();
		var camOff = camBuilder.build().renderImage();
		long offMs = (System.nanoTime() - t0) / 1_000_000;
		camOff.writeToImage("mp2_stage3_bvh_auto_off");
		System.out.println("BVH AUTO OFF: " + offMs + " ms");

		// --- Auto BVH ON (CBR ON, Manual OFF) ---
		scene.setConfig(new AccelConfig().setCBR(true).setBVHManual(false).setBVHAuto(true).setBvhMaxDepth(20) // tweak
																												// if
																												// needed
				.setBvhLeafSize(8)); // tweak if needed
		long t1 = System.nanoTime();
		var camOn = camBuilder.build().renderImage();
		long onMs = (System.nanoTime() - t1) / 1_000_000;
		camOn.writeToImage("mp2_stage3_bvh_auto_on");
		System.out.println("BVH AUTO ON:  " + onMs + " ms");
	}

}
