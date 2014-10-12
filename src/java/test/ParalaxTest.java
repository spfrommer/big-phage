package test;

import java.util.Arrays;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.physics.collision.NoCollisionFilter;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.imp.render.ParalaxRenderComponent;
import engine.core.imp.render.StationaryCameraControllerComponent;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class ParalaxTest extends SimplePhysicsGame {
	private Entity m_camera;

	public ParalaxTest() {
		super("Paralax Test");
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("starbackground1",
				MaterialFactory.createBasicMaterial("Textures/starbackground1.png", false, true));
		MaterialPool.materials.put("starbackground2",
				MaterialFactory.createBasicMaterial("Textures/starbackground2.png", false, true));
	}

	// LAYER 0 IS RESERVED FOR THE CAMERA, 1 FOR LIGHTS, 2 IS USUALLY
	// BACKGROUND, AND 3/4 ARE ANY ADDITIONAL OBJECTS

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the background
		Entity stars1 = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(15f, 15f), BodyType.STATIC,
				MaterialPool.materials.get("starbackground1"), new ParalaxRenderComponent());
		stars1.setUpdateOrder(2);
		this.getPhysicsManager().getCollisionFilter().addFilter(stars1, new NoCollisionFilter());
		// stars1.setData("sys_repeatMaterial", true);
		stars1.setData("sys_repeatCount", 10f);
		stars1.setData("sys_paralaxDepth", 0f);
		getWorld().addEntity(stars1);

		// make the wall
		Entity stars2 = factory.createTexturedSolid(new Vector2f(1.75f, 1.5f), 0f, new Vector2f(15f, 15f),
				BodyType.STATIC, MaterialPool.materials.get("starbackground2"), new ParalaxRenderComponent());
		stars2.setUpdateOrder(2);
		this.getPhysicsManager().getCollisionFilter().addFilter(stars2, new NoCollisionFilter());
		stars2.setData("sys_repeatMaterial", true);
		stars2.setData("sys_repeatCount", 10f);
		stars2.setData("sys_paralaxDepth", 2f);
		getWorld().addEntity(stars2);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(5f, 5f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		light.setUpdateOrder(1);
		getWorld().addEntity(light);

		// add the camera
		m_camera = new Entity(getWorld());
		m_camera.setData("sys_camPosition", new Vector2f(3f, 3f));
		m_camera.setData("sys_camScale", new Vector2f(1f, 1f));
		m_camera.setData("sys_camRotation", 0f);
		m_camera.setUpdateOrder(0);
		m_camera.addComponent(new StationaryCameraControllerComponent());
		getWorld().addEntity(m_camera);
	}

	private Vector2f camPosition = new Vector2f(0f, 0f);

	@Override
	public void preUpdate() {
		camPosition.x += 0.01f;
		m_camera.setData("sys_camPosition", camPosition);
	}

	@Override
	public void postUpdate() {

	}

	public static void main(String[] args) {
		ParalaxTest test = new ParalaxTest();
		test.start();
	}
}
