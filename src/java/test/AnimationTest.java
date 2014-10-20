package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.animation.AnimationComponent;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.material.Material;
import glextra.renderer.Light.PointLight;

public class AnimationTest extends SimplePhysicsGame {
	public AnimationTest() {
		super("Animation Test");
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("grassbackground",
				MaterialFactory.createBasicMaterial("Textures/grassbackground.png"));

		for (int r = 0; r < 100; r++) {
			Material material = MaterialFactory.createBasicMaterial(new Color(r / 100f, 0f, 0f));
			MaterialPool.materials.put("red" + r, material);
		}
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the background
		Entity background = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(10f, 10f),
				BodyType.STATIC, MaterialPool.materials.get("grassbackground"));
		background.setUpdateOrder(0);
		getWorld().addEntity(background);

		Entity animated = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f), BodyType.STATIC,
				MaterialPool.materials.get("red0"));
		List<Material> frames = new ArrayList<Material>();
		for (int i = 0; i < 100; i++) {
			Material material = MaterialPool.materials.get("red" + i);
			frames.add(material);
		}
		animated.setData("sys_frames", frames);
		animated.setData("sys_timePerFrame", 1f / 30f);
		animated.setData("sys_repeatAnimation", true);
		animated.addComponent(new AnimationComponent());
		getWorld().addEntity(animated);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(5f, 5f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));

		light.addComponent(new LightComponent());
		getWorld().addEntity(light);
	}

	@Override
	public void preUpdate() {

	}

	@Override
	public void postUpdate() {

	}

	public static void main(String[] args) {
		AnimationTest test = new AnimationTest();
		test.start();
	}
}
