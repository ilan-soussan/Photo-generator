/**
 *
 */
package unitTests.renderer;

import geometries.Tube;
import org.junit.Test;

import elements.*;
import geometries.Sphere;
import geometries.Triangle;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
	private Scene scene = new Scene("Test scene");

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheres() {
		Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(150, 150).setDistance(1000);

		scene.geometries.add( //
				new Sphere(50, new Point3D(0, 0, -50)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setkT(0.3)),
				new Sphere(25, new Point3D(0, 0, -50)) //
						.setEmission(new Color(java.awt.Color.RED)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
		scene.lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKl(0.0004).setKq(0.0000006));

		RenderMultiThread render = new RenderMultiThread() //
				.setImageWriter(new ImageWriter("refractionTwoSpheres", 200,200 )) //
				.setCamera(camera) //
				.setRayTracerBase(new RayTracerBasic(scene))
				;
		render.renderImage();
		//render.renderImageSuperSampling(3);
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheresOnMirrors() {
		Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(2500, 2500).setDistance(10000); //

		scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

		scene.geometries.add( //
				new Sphere(400, new Point3D(-950, -900, -1000)) //
						.setEmission(new Color(0, 0, 100)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20).setkT(0.5)),
				new Sphere(200, new Point3D(-950, -900, -1000)) //
						.setEmission(new Color(100, 20, 20)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
						new Point3D(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(1)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
						new Point3D(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(0.5)));

		scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKl(0.00001).setKq(0.000005));

		ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
		RenderMultiThread render = new RenderMultiThread() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracerBase(new RayTracerBasic(scene));

		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	public void trianglesTransparentSphere() {
		Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(200, 200).setDistance(1000);

		scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

		scene.geometries.add( //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
				new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
				new Sphere(30, new Point3D(60, 50, -50)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setkT(0.6)));

		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1)) //
				.setKl(4E-5).setKq(2E-7));

		ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
		RenderMultiThread render = new RenderMultiThread() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracerBase(new RayTracerBasic(scene));

		render.renderImage();
		render.writeToImage();
	}
	@Test
	public void trianglesTransparentSphereT1() {
		Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setViewPlaneSize(2500, 2500).setDistance(10000); //

		scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.black), 0.30));


		scene.geometries.add( //
				new Sphere(400, new Point3D(-950, -900, -1000)) //
						.setEmission(new Color(0, 0, 100)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20).setkT(0.5)),
				new Sphere(200, new Point3D(-900, -800, -900)) //
						.setEmission(new Color(20, 60, 90)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
					new Point3D(670, 670, 3000)) //
					.setEmission(new Color(20, 20, 20)) //
					.setMaterial(new Material().setkR(1)),
				new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
						new Point3D(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setkR(0.001)),
		new Triangle(new Point3D(-700, -1150, -1000), new Point3D(-1000, -900, -1000), new Point3D(-1200, -1100, -800)) //
				.setEmission(new Color(java.awt.Color.black)) //
				.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
		new Sphere(200, new Point3D(-600, -700, -1000)) //
				.setEmission(new Color(java.awt.Color.BLUE)) //
				.setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setkT(0.6)));

		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(-600, -700, -1000), new Vector(0, 0, -1)) //
				.setKl(4E-5).setKq(2E-7));
		scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(600, 700, 1000), new Vector(0, 0, -1)) //
				.setKl(4E-5).setKq(2E-7));

		scene.lights.add(new DirectionalLight(new Color(700, 400, 500), new Vector(0, 0, -1))); //

		ImageWriter imageWriter = new ImageWriter("refractionShadowT1", 600, 600);
		RenderMultiThread render = new RenderMultiThread() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracerBase(new RayTracerBasic(scene));

		render.renderImage();
		render.writeToImage();
	}
	@Test
	public void pintest() {
		Camera camera = new Camera(new Point3D(-10, 0, 0), new Vector(1, 0, 0), new Vector(0, 0, -1)) //
				.setViewPlaneSize(2500, 2500).setDistance(100); //

		scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.black), 0.30));


		scene.geometries.add( //
				new Sphere(10, new Point3D(3, 0, 15)) //
						.setEmission(new Color(java.awt.Color.WHITE)) //
						.setMaterial(new Material().setKd(0.0).setKs(0.0).setShininess(20)),
				new Sphere(7.5, new Point3D(3, 0, 7.5)) //
						.setEmission(new Color(java.awt.Color.BLUE)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
				new Sphere(6, new Point3D(3, 0, 3.8)) //
						.setEmission(new Color(java.awt.Color.RED)) //
						.setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30)),
				new Sphere(7.5, new Point3D(3, 0, 22.5)) //
						.setEmission(new Color(java.awt.Color.YELLOW)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
				new Sphere(6, new Point3D(3, 0, 26.3)) //
						.setEmission(new Color(java.awt.Color.PINK)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
				new Sphere(5, new Point3D(3, 0, 50)) //
						.setEmission(new Color(java.awt.Color.GREEN)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
				new Tube(5,new Ray(new Vector(0,0,5),new Point3D(30,0,0)))
						.setEmission(new Color(java.awt.Color.WHITE)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)));

		scene.lights.add(new DirectionalLight(new Color(java.awt.Color.YELLOW), new Vector(0, 1,0))); //

		ImageWriter imageWriter = new ImageWriter("pin", 1500, 1500);
		RenderMultiThread render = new RenderMultiThread() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracerBase(new RayTracerBasic(scene));

		render.renderImage();
		render.writeToImage();
	}


}
