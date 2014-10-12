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
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class LayerTest extends SimplePhysicsGame {
	public LayerTest() {
		super("Layer Test");
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("metalplate", MaterialFactory.createBasicMaterial("Textures/metalplate.png"));
		MaterialPool.materials.put("grassbackground",
				MaterialFactory.createBasicMaterial("Textures/grassbackground.png", false, true));
	}

	// LAYER 0 IS RESERVED FOR THE CAMERA, 1 FOR LIGHTS, 2 IS USUALLY
	// BACKGROUND, AND 3/4 ARE ANY ADDITIONAL OBJECTS

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the background
		Entity background = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(8f, 8f),
				BodyType.STATIC, MaterialPool.materials.get("grassbackground"));
		this.getPhysicsManager().getCollisionFilter().addFilter(background, new NoCollisionFilter());
		background.setUpdateOrder(2);
		background.setData("sys_repeatMaterial", true);
		background.setData("sys_repeatCount", 2f);
		getWorld().addEntity(background);

		// make the wall
		Entity wall = factory.createTexturedSolid(new Vector2f(1.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.STATIC, MaterialPool.materials.get("metalplate"));
		wall.setUpdateOrder(3);
		getWorld().addEntity(wall);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(5f, 5f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		light.setUpdateOrder(1);
		getWorld().addEntity(light);
	}

	@Override
	public void preUpdate() {

	}

	@Override
	public void postUpdate() {

	}

	public static void main(String[] args) {
		LayerTest test = new LayerTest();
		test.start();
	}
}
