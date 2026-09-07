package showcase;

import static java.awt.Color.*;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Standalone showcase renderer — produces high-quality demo images that
 * highlight the full capabilities of this ray-tracing engine:
 * reflections, refractions (glass), soft shadows, and multi-source lighting.
 *
 * Run with:  java -cp bin showcase.ShowcaseRenderer
 */
public class ShowcaseRenderer {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Ray Tracer Showcase ===");

        renderGlassShowroom();
        renderSolarSystem();

        System.out.println("Done! Images saved to ./images/");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SCENE 1 – Glass Showroom
    //  A mirror floor, glass/chrome spheres, coloured spotlights.
    // ─────────────────────────────────────────────────────────────────────────
    private static void renderGlassShowroom() {
        System.out.println("Rendering: Glass Showroom...");

        Scene scene = new Scene("Glass Showroom");
        scene.setBackground(new Color(5, 5, 15));
        scene.setAmbientLight(new AmbientLight(new Color(15, 15, 25)));

        // ── Floor (large mirror plane made of two triangles) ─────────────────
        Material floorMat = new Material()
                .setKD(0.15).setKS(0.15).setShininess(100)
                .setKR(new Double3(0.65, 0.65, 0.7));   // chrome-mirror

        Point FL = new Point(-600, -120, -800);
        Point FR = new Point( 600, -120, -800);
        Point BL = new Point(-600, -120,  400);
        Point BR = new Point( 600, -120,  400);

        scene.geometries.add(
                new Triangle(FL, FR, BR).setEmission(new Color(8, 8, 12)).setMaterial(floorMat),
                new Triangle(FL, BR, BL).setEmission(new Color(8, 8, 12)).setMaterial(floorMat)
        );

        // ── Back wall (subtle dark plane) ────────────────────────────────────
        Material wallMat = new Material().setKD(0.3).setKS(0.05).setShininess(10);
        scene.geometries.add(
                new Plane(new Point(0, 0, -800), new Vector(0, 0, 1))
                        .setEmission(new Color(10, 10, 20)).setMaterial(wallMat)
        );

        // ── Central large glass sphere ────────────────────────────────────────
        scene.geometries.add(
                new Sphere(90, new Point(0, -30, -350))
                        .setEmission(new Color(5, 25, 40))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.4).setShininess(300)
                                .setKT(new Double3(0.85, 0.85, 0.85))
                                .setKR(new Double3(0.1, 0.1, 0.1)))
        );

        // ── Left chrome sphere ───────────────────────────────────────────────
        scene.geometries.add(
                new Sphere(65, new Point(-220, -55, -420))
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.9).setShininess(1000)
                                .setKR(new Double3(0.85, 0.85, 0.85)))
        );

        // ── Right gold sphere ────────────────────────────────────────────────
        scene.geometries.add(
                new Sphere(65, new Point(220, -55, -420))
                        .setEmission(new Color(80, 50, 5))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.7).setShininess(500)
                                .setKR(new Double3(0.4, 0.3, 0.05)))
        );

        // ── Small accent spheres ─────────────────────────────────────────────
        // ruby
        scene.geometries.add(
                new Sphere(35, new Point(-370, -85, -280))
                        .setEmission(new Color(120, 5, 5))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.6).setShininess(200)
                                .setKT(new Double3(0.3, 0.0, 0.0)))
        );
        // sapphire
        scene.geometries.add(
                new Sphere(35, new Point(370, -85, -280))
                        .setEmission(new Color(5, 20, 130))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.6).setShininess(200)
                                .setKT(new Double3(0.0, 0.05, 0.4)))
        );
        // emerald (back-centre)
        scene.geometries.add(
                new Sphere(28, new Point(100, -92, -200))
                        .setEmission(new Color(5, 100, 30))
                        .setMaterial(new Material()
                                .setKD(0.35).setKS(0.55).setShininess(150)
                                .setKT(new Double3(0.05, 0.35, 0.1)))
        );
        // pearl (small, front)
        scene.geometries.add(
                new Sphere(20, new Point(-100, -100, -160))
                        .setEmission(new Color(200, 195, 190))
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.5).setShininess(80)
                                .setKR(new Double3(0.15, 0.15, 0.15)))
        );

        // ── Lighting ─────────────────────────────────────────────────────────
        // Key light — warm white from upper-left
        scene.lights.add(
                new SpotLight(new Color(900, 800, 700),
                        new Point(-400, 400, 200),
                        new Vector(0.6, -1, -1.2))
                        .setKl(0.000005).setKq(0.000001)
        );
        // Fill light — cool blue from upper-right
        scene.lights.add(
                new SpotLight(new Color(200, 300, 700),
                        new Point(500, 350, 100),
                        new Vector(-0.8, -1, -1.1))
                        .setKl(0.00001).setKq(0.000003)
        );
        // Rim light — purple from behind
        scene.lights.add(
                new PointLight(new Color(180, 50, 250), new Point(0, 200, -700))
                        .setKl(0.00002).setKq(0.000005)
        );
        // Ground bounce — subtle warm from below
        scene.lights.add(
                new PointLight(new Color(60, 40, 20), new Point(0, -400, -300))
                        .setKl(0.0001).setKq(0.00005)
        );

        Camera.getBuilder()
                .setLocation(new Point(0, 80, 600))
                .setDirection(new Point(0, -50, -350), Vector.AXIS_Y)
                .setVpDistance(600)
                .setVpSize(600, 600)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("showcase_glass_showroom");

        System.out.println("  -> showcase_glass_showroom.png");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SCENE 2 – Solar System
    //  A glowing "sun", planets with different materials, starfield backdrop.
    // ─────────────────────────────────────────────────────────────────────────
    private static void renderSolarSystem() {
        System.out.println("Rendering: Solar System...");

        Scene scene = new Scene("Solar System");
        scene.setBackground(new Color(2, 2, 8));
        scene.setAmbientLight(new AmbientLight(new Color(8, 8, 15)));

        // ── Sun (large glowing sphere, no shadows cast on it) ─────────────────
        scene.geometries.add(
                new Sphere(140, new Point(-300, 0, -600))
                        .setEmission(new Color(255, 200, 50))
                        .setMaterial(new Material().setKD(0.1).setKS(0.0).setShininess(1))
        );

        // ── Mercury — small, dark grey ────────────────────────────────────────
        scene.geometries.add(
                new Sphere(18, new Point(-80, 20, -350))
                        .setEmission(new Color(80, 75, 70))
                        .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(40))
        );

        // ── Venus — slightly larger, yellowish ────────────────────────────────
        scene.geometries.add(
                new Sphere(30, new Point(60, -10, -380))
                        .setEmission(new Color(200, 160, 80))
                        .setMaterial(new Material()
                                .setKD(0.5).setKS(0.3).setShininess(30)
                                .setKT(new Double3(0.05, 0.04, 0.0)))
        );

        // ── Earth — blue/green ────────────────────────────────────────────────
        scene.geometries.add(
                new Sphere(38, new Point(200, 0, -450))
                        .setEmission(new Color(20, 60, 180))
                        .setMaterial(new Material()
                                .setKD(0.6).setKS(0.4).setShininess(80)
                                .setKR(new Double3(0.08, 0.08, 0.15)))
        );
        // Moon
        scene.geometries.add(
                new Sphere(12, new Point(250, 45, -410))
                        .setEmission(new Color(160, 160, 155))
                        .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(10))
        );

        // ── Mars — red ────────────────────────────────────────────────────────
        scene.geometries.add(
                new Sphere(25, new Point(340, -15, -500))
                        .setEmission(new Color(180, 60, 30))
                        .setMaterial(new Material().setKD(0.65).setKS(0.25).setShininess(30))
        );

        // ── Saturn — large, with ring (triangle approximation) ────────────────
        scene.geometries.add(
                new Sphere(60, new Point(130, -10, -700))
                        .setEmission(new Color(200, 175, 100))
                        .setMaterial(new Material().setKD(0.5).setKS(0.4).setShininess(60))
        );
        // Ring — flat triangles forming a disc
        Color ringColor = new Color(180, 155, 90);
        Material ringMat = new Material().setKD(0.6).setKS(0.2).setShininess(20)
                .setKT(new Double3(0.4, 0.35, 0.2));
        scene.geometries.add(
                new Triangle(new Point(40, -25, -700), new Point(220, -25, -700), new Point(130, -25, -620))
                        .setEmission(ringColor).setMaterial(ringMat),
                new Triangle(new Point(40, -25, -700), new Point(220, -25, -700), new Point(130, -25, -780))
                        .setEmission(ringColor).setMaterial(ringMat),
                new Triangle(new Point(40, 5, -700), new Point(220, 5, -700), new Point(130, 5, -620))
                        .setEmission(ringColor).setMaterial(ringMat),
                new Triangle(new Point(40, 5, -700), new Point(220, 5, -700), new Point(130, 5, -780))
                        .setEmission(ringColor).setMaterial(ringMat)
        );

        // ── Ice giant (Neptune-like) ──────────────────────────────────────────
        scene.geometries.add(
                new Sphere(45, new Point(420, 30, -750))
                        .setEmission(new Color(20, 80, 200))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.5).setShininess(120)
                                .setKR(new Double3(0.08, 0.08, 0.2)))
        );

        // ── Stars — tiny bright spheres scattered in the background ───────────
        int[][] stars = {
                {-500, 280, -900}, {400, 300, -950}, {-200, -280, -850},
                {500, -200, -900}, {-450, 100, -920}, {300, -300, -880},
                {-100, 350, -870}, {480, 150, -860}, {-380, -150, -910},
                {150, -350, -930}, {-260, 220, -875}, {350, 260, -895}
        };
        for (int[] s : stars) {
            double brightness = 150 + Math.random() * 105;
            scene.geometries.add(
                    new Sphere(4 + Math.random() * 5, new Point(s[0], s[1], s[2]))
                            .setEmission(new Color((int) brightness, (int) brightness, (int) (brightness * 0.95)))
                            .setMaterial(new Material().setKD(0.0).setKS(0.0).setShininess(1))
            );
        }

        // ── Lighting ─────────────────────────────────────────────────────────
        // The Sun's light — strong warm point light
        scene.lights.add(
                new PointLight(new Color(2000, 1600, 900), new Point(-300, 0, -600))
                        .setKl(0.000001).setKq(0.0000002)
        );
        // Faint cool ambient from "space"
        scene.lights.add(
                new DirectionalLight(new Color(20, 20, 50), new Vector(1, -0.3, -0.5))
        );

        Camera.getBuilder()
                .setLocation(new Point(0, 200, 700))
                .setDirection(new Point(80, -30, -500), Vector.AXIS_Y)
                .setVpDistance(700)
                .setVpSize(700, 500)
                .setResolution(1200, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("showcase_solar_system");

        System.out.println("  -> showcase_solar_system.png");
    }
}
