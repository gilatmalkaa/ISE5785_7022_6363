# 🎨 Java Ray Tracer

A feature-rich **ray tracing engine developed and extended in Java** as part of a Software Engineering course at **Jerusalem College of Technology**.

The renderer implements the **Phong shading model** together with recursive reflections and refractions, transparency, multiple light sources, anti-aliasing, BVH acceleration, and multithreaded rendering.

The project focuses on object-oriented design, software architecture, rendering algorithms, testing, and performance optimization.

---

## ✨ Showcase Renders

| Glass Showroom | Solar System |
|:---:|:---:|
| <img width="1000" height="1000" alt="showcase_glass_showroom" src="https://github.com/user-attachments/assets/ae75ce5f-ea68-4f4e-8e3c-43eec238d170" /> | <img width="1200" height="800" alt="showcase_solar_system" src="https://github.com/user-attachments/assets/d46975b2-2997-43f6-a885-f7dfe2860709" /> |
| Chrome mirrors · glass spheres · multiple light sources | Planetary system · star field · Phong shading |

---

## 🔬 Core Features

| Feature | Details |
|---|---|
| **Geometry** | Sphere, Triangle, Plane, Polygon, Cylinder, Tube |
| **Shading** | Phong model — ambient, diffuse, and specular lighting |
| **Global Effects** | Recursive reflections and refractions |
| **Shadows** | Hard shadows with partial transparency |
| **Lighting** | Ambient, Directional, Point, and Spot lights |
| **Anti-Aliasing** | Multi-ray sampling per pixel |
| **Performance** | BVH acceleration using AABB and multithreaded rendering |
| **Materials** | Diffuse, specular, reflection, transparency, and shininess properties |
| **Camera** | Configurable position, direction, field of view, and resolution |

---

## 🏗️ Architecture

```text
src/
├── primitives/      # Point, Vector, Ray, Color, Double3, Material, AABB
├── geometries/      # Intersectable, Geometry, Sphere, Plane, Triangle
│                    # Polygon, Cylinder, Tube, Geometries
├── lighting/        # Light, AmbientLight, DirectionalLight, PointLight, SpotLight
├── scene/           # Scene and acceleration configuration
├── renderer/        # Camera, SimpleRayTracer, ImageWriter
│                    # JitterSampler, PixelManager
└── showcase/        # Demo scenes

unittests/           # JUnit 5 test suite
```

---

## 🔄 Ray-Tracing Pipeline

```text
Camera
   │
   │ Rays
   ▼
Scene
   │
   │ Intersections
   ▼
SimpleRayTracer
   │
   ├────────────── Local Effects
   │               Phong shading
   │               Diffuse + Specular
   │               Shadow rays
   │
   └────────────── Global Effects
                   Reflection
                   Refraction
                   Recursive rays
```

The renderer traces rays from the camera through each pixel into the scene.

For every intersection, the engine calculates local lighting effects and can recursively generate additional rays for reflection and transparency.

---

## 💡 Lighting and Shading

The renderer uses the **Phong reflection model** to calculate lighting.

Supported lighting components include:

- Ambient lighting
- Diffuse reflection
- Specular reflection
- Multiple light sources
- Directional lights
- Point lights
- Spot lights
- Transparent shadow effects

Material properties control the appearance of each object, including:

- Diffuse coefficient
- Specular coefficient
- Shininess
- Reflection
- Transparency

---

## 🪞 Reflections and Refractions

The ray tracer supports recursive global effects.

When a ray hits a reflective or transparent surface, additional rays are generated to calculate:

- Reflected color
- Refracted / transmitted color
- Combined contribution to the final pixel

The recursion depth is limited to prevent unnecessary computation.

---

## ⚡ Performance Optimization

### BVH Acceleration

The project includes a **Bounding Volume Hierarchy (BVH)** using Axis-Aligned Bounding Boxes (AABB).

BVH reduces the number of intersection calculations required when rendering complex scenes.

Instead of checking every object in the scene, the renderer can first determine whether a ray intersects a bounding region.

### Multithreading

Rendering can be performed using multiple threads.

Pixels are distributed between worker threads, allowing rendering work to be executed concurrently and improving performance on multi-core processors.

---

## 🎯 Anti-Aliasing

Anti-aliasing is implemented by tracing multiple rays through each pixel.

The final pixel color is calculated from the sampled rays, helping reduce jagged edges and improving image quality.

---

## 🧩 Design Patterns

The project applies several software design patterns.

### Builder Pattern

`Camera.Builder` is used to construct and validate camera objects.

### Composite Pattern

`Geometries` represents collections of geometric objects and allows them to be handled through a common interface.

### Strategy Pattern

The ray-tracing implementation is separated behind the `RayTracerBase` abstraction, allowing different tracing strategies to be used.

---

## 🧪 Testing

The project includes a **JUnit 5** test suite covering major parts of the rendering engine.

Examples include:

| Test Class | Coverage |
|---|---|
| `ReflectionRefractionTests` | Reflection, transparency, and glass effects |
| `ShadowTests` | Shadow and transparency behavior |
| `LightsTests` | Different light sources |
| `RenderTests` | Rendering pipeline |
| `PerformanceTests` | BVH and performance behavior |

Tests are also included for geometric intersections and other core components.

---

## 🚀 Running the Showcase

The showcase scenes can be compiled and executed directly.

### 1. Compile

```bash
find src -name "*.java" | xargs javac -d bin -sourcepath src
```

### 2. Create the output directory

```bash
mkdir -p images
```

### 3. Run the showcase

```bash
java -cp bin showcase.ShowcaseRenderer
```

Generated images will be saved in:

```text
images/
```

Example outputs:

```text
showcase_glass_showroom.png
showcase_solar_system.png
```

---

## 🧪 Running the Tests

The test suite requires **JUnit 5**.

Open the project in **IntelliJ IDEA** or **Eclipse** and run the tests inside:

```text
unittests/
```

---

## 📚 Development Highlights

This project demonstrates experience with:

- Java
- Object-Oriented Programming
- Software Engineering
- Ray-Tracing Algorithms
- Computer Graphics
- Recursive Algorithms
- Design Patterns
- Multithreading
- Performance Optimization
- Bounding Volume Hierarchy
- Unit Testing with JUnit
- Software Architecture

---

## 👩‍💻 Authors

Developed as a pair project by:

- **Gilat Malka**
- **Shira Amar**

as part of the **Software Engineering course at Jerusalem College of Technology**.
