package test.collision;

import java.util.Arrays;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.control.KeyboardControlComponent;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.renderer.Light.PointLight;

public class CollisionJitterTest extends SimplePhysicsGame {

	public CollisionJitterTest() {
		super("Collision Test");

		this.getPhysicsManager().setGravity(new Vector2f(0f, 0f));
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("metalplate", MaterialFactory.createBasicMaterial("Textures/metalplate.png"));
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the ground
		Entity ground = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(2f, 1f), BodyType.STATIC,
				MaterialPool.materials.get("metalplate"));
		getWorld().addEntity(ground);

		// make the brick
		Entity brick = factory.createTexturedSolid(new Vector2f(0f, 3f), (float) Math.PI / 4, new Vector2f(1f, 1f),
				BodyType.DYNAMIC, MaterialPool.materials.get("metalplate"));
		brick.addComponent(new KeyboardControlComponent());
		getWorld().addEntity(brick);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(0f, 1f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		getWorld().addEntity(light);
	}

	@Override
	protected void preUpdate() {

	}

	@Override
	protected void postUpdate() {

	}

	public static void main(String[] args) {
		CollisionJitterTest test = new CollisionJitterTest();
		test.start();
	}
}
