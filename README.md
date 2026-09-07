# 🎨 Ray Tracer — ISE5785

A full-featured **physically-based ray tracer** built from scratch in Java as part of the Software Engineering course (ISE, Bar-Ilan University).  
The engine implements the classic Phong shading model extended with global illumination — recursive reflections, refractions, soft shadows, and multi-source lighting.

---

## ✨ Showcase Renders

| Glass Showroom | Solar System |
|:---:|:---:|
| <img width="1000" height="1000" alt="showcase_glass_showroom" src="https://github.com/user-attachments/assets/ae75ce5f-ea68-4f4e-8e3c-43eec238d170" />
 | <img width="1200" height="800" alt="showcase_solar_system" src="https://github.com/user-attachments/assets/d46975b2-2997-43f6-a885-f7dfe2860709" />
 |
| Chrome mirrors · glass spheres · 4-light rig | Planetary system · star field · Phong shading |

---

## 🔬 Core Features

| Feature | Details |
|---|---|
| **Geometry** | Sphere, Triangle, Plane, Polygon, Cylinder, Tube |
| **Shading** | Phong model — ambient · diffuse · specular |
| **Global illumination** | Recursive reflections & refractions (configurable depth) |
| **Shadow** | Hard shadows with partial transparency (soft kT) |
| **Lights** | Ambient · Directional · Point · SpotLight (with narrow-beam) |
| **Anti-aliasing** | Multi-ray per pixel (jitter sampling) |
| **Performance** | BVH acceleration (AABB) · multi-threaded rendering |
| **Materials** | kD · kS · kA · kR (reflection) · kT (transparency) · shininess |
| **Camera** | Configurable position, direction, FOV, resolution |

---

## 🏗️ Architecture

```
src/
├── primitives/      # Point · Vector · Ray · Color · Double3 · Material · AABB
├── geometries/      # Intersectable · Geometry · Sphere · Plane · Triangle
│                    # Polygon · Cylinder · Tube · Geometries (composite + BVH)
├── lighting/        # Light · AmbientLight · DirectionalLight · PointLight · SpotLight
├── scene/           # Scene (PDS) · AccelConfig
├── renderer/        # Camera (Builder) · SimpleRayTracer · ImageWriter
│                    # JitterSampler · PixelManager
└── showcase/        # Standalone demo scenes (no JUnit required)

unittests/           # JUnit 5 test suite — geometries, lighting, renderer
```

### Ray-tracing pipeline

```
Camera  ──(rays)──►  Scene  ──(intersections)──►  SimpleRayTracer
                                                        │
                                          ┌─────────────┴──────────────┐
                                    Local Effects               Global Effects
                                  (Phong: kD, kS)         (reflection kR / refraction kT)
                                          │                             │
                                    Shadow rays                  Recursive rays
                                   (transparency)               (depth ≤ MAX=10)
```

---

## 🚀 Running the Showcase

No build tool required — compile and run with plain `javac`/`java`:

```bash
# 1. Compile
find src -name "*.java" | xargs javac -d bin -sourcepath src

# 2. Create output directory
mkdir -p images

# 3. Render
java -cp bin showcase.ShowcaseRenderer
# → images/showcase_glass_showroom.png
# → images/showcase_solar_system.png
```

---

## 🧪 Running the Tests

The test suite requires **JUnit 5**.  Open in Eclipse or IntelliJ and run all tests in `unittests/`.

Key test classes:

| Test class | What it covers |
|---|---|
| `ReflectionRefractionTests` | Mirror spheres · glass · partial transparency |
| `ShadowTests` | Hard & soft (transparent) shadows |
| `LightsTests` | All light types on spheres & triangles |
| `RenderTests` | Basic scene rendering pipeline |
| `PerformanceTests` | BVH acceleration benchmark |

---

## 📐 Design Highlights

* **Builder pattern** — `Camera.Builder` validates and constructs immutable cameras.
* **Composite pattern** — `Geometries` acts as both a leaf and a container (BVH tree).
* **Strategy pattern** — `RayTracerBase` / `SimpleRayTracer` are swappable.
* **Intersection caching** — `Intersectable.Intersection` carries cached dot-products and normals to avoid recomputation in Phong shading.

---

## 👩‍💻 Authors

**Gilat Kedem & Shira Amar** 
