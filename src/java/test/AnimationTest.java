package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.animation.AnimationComponent;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glextra.material.Material;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class AnimationTest extends PhysicsGame {
	public AnimationTest() {
		super("Animation Test");
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the background
		Entity background = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(10f, 10f),
				BodyType.STATIC, MaterialFactory.createBasicMaterial("Textures/grassbackground.png"));
		background.setUpdateOrder(0);
		getWorld().addEntity(background);

		Entity animated = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(1f, 1f), BodyType.STATIC,
				MaterialFactory.createBasicMaterial(new Color(1, 1, 1)));
		List<Material> frames = new ArrayList<Material>();
		for (float i = 0; i < 1f; i += 0.01f) {
			Material material = MaterialFactory.createBasicMaterial(new Color(i, 0f, 0f));
			frames.add(material);
		}
		animated.setData("sys_frames", frames);
		animated.setData("sys_timePerFrame", 1f / 60f);
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
