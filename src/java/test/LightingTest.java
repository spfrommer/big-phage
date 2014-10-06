package test;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class LightingTest extends PhysicsGame {

	public LightingTest() {
		super("Lighting Test");
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		Entity wall = factory.createTexturedSolid(new Vector2f(3.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(wall);

		Entity light = new Entity(getWorld());
		light.addComponent(new LightComponent());
		light.setData("sys_light", new PointLight(new Vector3f(0f, 0f, 3f), new Vector3f(0f, 0f, 0.05f), new Color(0f,
				1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f)));
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
