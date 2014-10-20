package test;

import java.util.Arrays;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.renderer.Light.PointLight;

public class LightingTest extends SimplePhysicsGame {

	public LightingTest() {
		super("Lighting Test");
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("metalplate", MaterialFactory.createBasicMaterial("Textures/metalplate.png"));
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		Entity wall = factory.createTexturedSolid(new Vector2f(0f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialPool.materials.get("metalplate"));
		getWorld().addEntity(wall);

		Entity light = new Entity(getWorld());
		light.addComponent(new LightComponent());
		PointLight light1 = new PointLight(new Vector3f(-2f, 0f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(0f, 0f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f));
		PointLight light2 = new PointLight(new Vector3f(2f, 0f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 0f, 0f), new Color(0.1f, 0.1f, 0.1f, 0.1f));
		light.setData("sys_lights", Arrays.asList(light1, light2));
		getWorld().addEntity(light);
	}

	@Override
	public void preUpdate() {

	}

	@Override
	public void postUpdate() {

	}

	public static void main(String[] args) {
		LightingTest test = new LightingTest();
		test.start();
	}
}
